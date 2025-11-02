package com.example.ejemplo_level_up.ui.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
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
    onBack: () -> Unit,                 // ← nuevo
    onOpenCart: () -> Unit,             // ← nuevo
    fvm: FavoritesViewModel = viewModel(),
    hvm: HomeViewModel = viewModel()
) {
    val favs by fvm.favIds.collectAsState(initial = emptySet())
    val games by hvm.games.collectAsState()
    val favGames = remember(favs, games) { games.filter { it.id in favs } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favoritos", color = MaterialTheme.colorScheme.primary) },
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
        if (favGames.isEmpty()) {
            Text(
                "Sin favoritos",
                Modifier.padding(padding).padding(16.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
        } else {
            LazyColumn(contentPadding = padding) {
                items(favGames) { g ->
                    ListItem(
                        headlineContent = { Text(g.title) },
                        supportingContent = { Text(g.description) },
                        modifier = Modifier.clickable { onOpenDetail(g.id) }
                    )
                    Divider()
                }
            }
        }
    }
}