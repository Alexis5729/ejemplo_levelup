package com.example.ejemplo_level_up.ui.cart

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ejemplo_level_up.R
import com.example.ejemplo_level_up.ui.components.MainTopBar
import com.example.ejemplo_level_up.ui.profile.UserProfile
import com.example.ejemplo_level_up.viewmodel.CartViewModel
import com.example.ejemplo_level_up.viewmodel.HomeViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    user: UserProfile?,
    isLoggedIn: Boolean,
    onBack: () -> Unit,
    onLoginHere: () -> Unit,
    onLogout: () -> Unit,
    onCartClick: () -> Unit,
    cartViewModel: CartViewModel
) {
    // ---------- Estado de alerta temporal ----------
    var showWelcomeAlert by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(4500)
        showWelcomeAlert = false
    }

    // ---------- ViewModel de productos ----------
    val hvm: HomeViewModel = viewModel()
    val games by hvm.games.collectAsState(initial = emptyList())

    // ---------- Estado del carrito ----------
    val cartItems by cartViewModel.items.collectAsState()

    // id + cantidad  ->  Game + cantidad
    val detailedItems = remember(cartItems, games) {
        cartItems.mapNotNull { (id, qty) ->
            games.find { it.id == id }?.let { game -> game to qty }
        }
    }

    val total = remember(detailedItems) {
        detailedItems.sumOf { (game, qty) ->
            (game.offerPrice ?: game.price) * qty
        }
    }

    Scaffold(
        containerColor = Color(0xFF0A0A0A),
        topBar = {
            Column {
                // 🔹 Barra superior con carrito y logout
                MainTopBar(
                    user = user,
                    isLoggedIn = isLoggedIn,
                    onLogout = onLogout,
                    onCartClick = onCartClick
                )

                // 🔹 Título y botón de volver
                TopAppBar(
                    title = {
                        Text(
                            "Carrito de Compras",
                            color = Color(0xFF00C8FF),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_back),
                                contentDescription = "Volver",
                                tint = Color(0xFF00C8FF)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF0A0A0A)
                    )
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            // ---------- ALERTA TEMPORAL (solo si hay sesión) ----------
            AnimatedVisibility(
                visible = showWelcomeAlert && isLoggedIn,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = Color(0xFF1C1C1C)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Text(
                        text = "🛍️ Para una mejor experiencia en tu compra, te invitamos a recorrer el catálogo de productos.",
                        color = Color(0xFF00C8FF),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(12.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ---------- LISTA DEL CARRITO / MENSAJE VACÍO ----------
            if (detailedItems.isEmpty()) {
                Text(
                    text = "Sin artículos en tu carrito",
                    color = Color(0xFFB0B0B0),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(detailedItems) { (game, qty) ->
                        ElevatedCard(
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = Color(0xFF1C1C1C)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = game.title,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Precio: $${game.offerPrice ?: game.price}",
                                    color = Color(0xFF00C8FF)
                                )

                                Spacer(Modifier.height(8.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    // Controles de cantidad
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconButton(
                                            onClick = {
                                                cartViewModel.setQuantity(
                                                    game.id,
                                                    qty - 1
                                                )
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Remove,
                                                contentDescription = "Disminuir",
                                                tint = Color.White
                                            )
                                        }
                                        Text(
                                            text = qty.toString(),
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 4.dp)
                                        )
                                        IconButton(
                                            onClick = {
                                                cartViewModel.setQuantity(
                                                    game.id,
                                                    qty + 1
                                                )
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = "Aumentar",
                                                tint = Color.White
                                            )
                                        }
                                    }

                                    // Eliminar ítem
                                    IconButton(
                                        onClick = { cartViewModel.remove(game.id) }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Eliminar",
                                            tint = Color(0xFFFF6B6B)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                // ---------- TOTAL Y ACCIONES ----------
                Text(
                    text = "Total: $${total}",
                    color = Color(0xFF00C8FF),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.End)
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = { cartViewModel.clear() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Vaciar carrito", color = Color(0xFFFF6B6B))
                    }

                    Button(
                        onClick = { /* TODO: flujo de pago */ },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00C8FF)
                        )
                    ) {
                        Text("Comprar", color = Color.Black)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ---------- BLOQUE DE LOGIN (solo si NO hay sesión) ----------
            AnimatedVisibility(visible = !isLoggedIn, enter = fadeIn(), exit = fadeOut()) {
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = Color(0xFF1A1A1A)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Inicia sesión para completar el proceso de compra",
                            color = Color(0xFF00C8FF),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 10.dp),
                            textAlign = TextAlign.Center
                        )
                        Button(
                            onClick = onLoginHere,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF00C8FF)
                            ),
                            modifier = Modifier.height(45.dp)
                        ) {
                            Text("Login aquí", color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}
