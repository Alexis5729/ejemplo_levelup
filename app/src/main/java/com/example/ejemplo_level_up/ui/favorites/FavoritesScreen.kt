package com.example.ejemplo_level_up.ui.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ejemplo_level_up.ui.components.MainTopBar
import com.example.ejemplo_level_up.ui.profile.UserProfile
import com.example.ejemplo_level_up.viewmodel.FavoritesViewModel
import com.example.ejemplo_level_up.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    onOpenDetail: (String) -> Unit,
    onBack: () -> Unit,
    onOpenCart: () -> Unit, // 🛒 nuevo parámetro agregado
    user: UserProfile?,
    isLoggedIn: Boolean,
    onLogout: () -> Unit,
    fvm: FavoritesViewModel = viewModel(),
    hvm: HomeViewModel = viewModel()
) {
    val favs by fvm.favIds.collectAsState(initial = emptySet())
    val games by hvm.games.collectAsState()

    // 🔹 Mantiene actualizada la lista de favoritos sin parpadeo
    val favGames = remember(favs, games) { games.filter { it.id in favs } }

    Scaffold(
        containerColor = Color(0xFF0A0A0A), // Fondo oscuro coherente con tu app

        // ✅ Barra superior: saludo + carrito + logout + botón volver
        topBar = {
            Column {
                MainTopBar(
                    user = user,
                    isLoggedIn = isLoggedIn,
                    onLogout = onLogout,
                    onCartClick = onOpenCart // 🛒 conecta el icono del carrito
                )

                TopAppBar(
                    title = { Text("Favoritos", color = Color(0xFF00C8FF)) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
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

        // ---------- CONTENIDO ----------
        if (favGames.isEmpty()) {
            Text(
                text = "No tienes productos en favoritos.",
                color = Color(0xFFB0B0B0),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            )
        } else {
            LazyColumn(contentPadding = padding) {
                items(favGames) { g ->
                    ListItem(
                        headlineContent = {
                            Text(
                                text = g.title,
                                color = Color(0xFF00C8FF),
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        supportingContent = {
                            Text(
                                text = g.description,
                                color = Color(0xFFB0B0B0),
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
                        modifier = Modifier.clickable { onOpenDetail(g.id) }
                    )
                    Divider(color = Color(0xFF1A1A1A))
                }
            }
        }
    }
}
