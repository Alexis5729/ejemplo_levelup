package com.example.ejemplo_level_up.ui.cart

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ejemplo_level_up.R
import com.example.ejemplo_level_up.ui.components.MainTopBar
import com.example.ejemplo_level_up.ui.profile.UserProfile
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    user: UserProfile?,
    isLoggedIn: Boolean,
    onBack: () -> Unit,
    onLoginHere: () -> Unit,
    onLogout: () -> Unit,
    onCartClick: () -> Unit
) {
    // ---------- Estado de alerta temporal ----------
    var showWelcomeAlert by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(4500)
        showWelcomeAlert = false
    }

    Scaffold(
        containerColor = Color(0xFF0A0A0A),
        topBar = {
            Column {
                // 🔹 Barra superior con carrito siempre visible y logout opcional
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

            // ---------- CONTENIDO PRINCIPAL ----------
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Sin artículos en tu carrito",
                color = Color(0xFFB0B0B0),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

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
