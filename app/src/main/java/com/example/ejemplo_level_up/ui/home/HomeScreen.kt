package com.example.ejemplo_level_up.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ejemplo_level_up.R
import com.example.ejemplo_level_up.data.model.Game
import com.example.ejemplo_level_up.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onOpenDetail: (String) -> Unit,
    onOpenFavs: () -> Unit,
    onOpenQr: () -> Unit,
    vm: HomeViewModel = viewModel()
) {
    LaunchedEffect(Unit) { vm.seed() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Level Up Gamer") },
                actions = {
                    TextButton(onClick = onOpenQr) { Text("QR") }
                    TextButton(onClick = onOpenFavs) { Text("Favoritos") }
                }
            )
        }
    ) { padding ->
        val games by vm.games.collectAsState(initial = emptyList())

        LazyColumn(contentPadding = padding) {
            items(games) { g: Game ->
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .clickable { onOpenDetail(g.id.toString()) }
                ) {
                    Row(Modifier.padding(12.dp)) {
                        val context = LocalContext.current

                        val resId = remember(g.imageResName, context) {
                            context.resources.getIdentifier(
                                g.imageResName,
                                "drawable",
                                context.packageName
                            )
                        }.takeIf { it != 0 } ?: R.drawable.ic_launcher_foreground

                        Image(
                            painter = painterResource(id = resId),
                            contentDescription = g.title,
                            modifier = Modifier.size(72.dp)
                        )

                        Spacer(Modifier.width(12.dp))

                        Column {
                            Text(g.title, style = MaterialTheme.typography.titleMedium)
                            Text("Precio: $${g.price}")
                            g.offerPrice?.let {
                                Text("Oferta: $${it}")
                            }
                        }
                    }
                }
            }
        }
    } // ← ESTA ES LA LLAVE FINAL QUE FALTABA
}
