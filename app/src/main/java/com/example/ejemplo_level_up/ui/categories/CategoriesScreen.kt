package com.example.ejemplo_level_up.ui.categories

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ejemplo_level_up.R
import com.example.ejemplo_level_up.data.model.Game

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    products: List<Game>,
    onOpenDetail: (String) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Categorías", color = MaterialTheme.colorScheme.primary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        if (products.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("No hay productos disponibles", color = MaterialTheme.colorScheme.onBackground)
            }
        } else {
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
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenDetail(g.id.toString()) }
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            val ctx = LocalContext.current
                            val resId = remember(g.imageResName, ctx) {
                                ctx.resources.getIdentifier(
                                    g.imageResName, "drawable", ctx.packageName
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
                                g.title,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 2
                            )
                            Text("Precio: $${g.price}")
                            g.offerPrice?.let {
                                Text("Oferta: $${it}", color = MaterialTheme.colorScheme.secondary)
                            }
                        }
                    }
                }
            }
        }
    }
}
