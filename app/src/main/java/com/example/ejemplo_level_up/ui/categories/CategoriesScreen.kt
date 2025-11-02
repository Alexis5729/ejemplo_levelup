package com.example.ejemplo_level_up.ui.categories

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ejemplo_level_up.R
import com.example.ejemplo_level_up.data.model.Game
import com.example.ejemplo_level_up.ui.components.MainTopBar
import com.example.ejemplo_level_up.ui.profile.UserProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    products: List<Game>,
    onOpenDetail: (String) -> Unit,
    onBack: () -> Unit,
    onOpenCart: () -> Unit, // 🛒 nuevo parámetro agregado
) {
    Scaffold(
        containerColor = Color(0xFF0A0A0A), // Fondo oscuro consistente

        topBar = {
            TopAppBar(
                title = {
                    Text("Categorías", color = MaterialTheme.colorScheme.primary)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onOpenCart) {
                        Icon(
                            imageVector = Icons.Filled.ShoppingCart,
                            contentDescription = "Carrito",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->

        // ---------- CONTENIDO ----------
        if (products.isEmpty()) {
            // 🔸 Vista cuando no hay productos
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay productos disponibles.",
                    color = Color(0xFFB0B0B0),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            // 🔸 Vista de grilla de productos
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(padding)
            ) {
                items(products) { g ->
                    ElevatedCard(
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = Color(0xFF1A1A1A)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenDetail(g.id.toString()) }
                    ) {
                        Column(
                            Modifier
                                .padding(12.dp)
                                .fillMaxWidth()
                        ) {
                            val ctx = LocalContext.current

                            // 🔹 Carga segura de imagen con fallback
                            val resId = remember(g.imageResName, ctx) {
                                ctx.resources.getIdentifier(
                                    g.imageResName,
                                    "drawable",
                                    ctx.packageName
                                )
                            }.takeIf { it != 0 } ?: R.drawable.ic_launcher_foreground

                            Image(
                                painter = painterResource(resId),
                                contentDescription = g.title,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(90.dp)
                            )

                            Spacer(Modifier.height(8.dp))

                            Text(
                                text = g.title,
                                color = Color(0xFF00C8FF),
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 2
                            )

                            Text(
                                text = "Precio: $${g.price}",
                                color = Color(0xFFB0B0B0)
                            )

                            g.offerPrice?.let {
                                Text(
                                    text = "Oferta: $${it}",
                                    color = Color(0xFFFFC107),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
