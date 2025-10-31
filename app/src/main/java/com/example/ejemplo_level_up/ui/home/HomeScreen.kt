package com.example.ejemplo_level_up.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.ShoppingCart

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
    onOpenCategories: () -> Unit,
    vm: HomeViewModel = viewModel()
) {
    LaunchedEffect(Unit) { vm.seed() }

    val games by vm.games.collectAsState(initial = emptyList())

    // UI state
    var query by rememberSaveable { mutableStateOf("") }
    val categories = listOf("Periféricos", "Accesorios", "Juegos", "Consolas", "PC", "Ofertas")
    var selectedCat by rememberSaveable { mutableStateOf(0) }
    var bottomSelected by rememberSaveable { mutableStateOf(0) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.drawable.logo_levelup),
                            contentDescription = "Level-Up Gamer",
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Level-Up Gamer", color = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                actions = {
                    IconButton(onClick = onOpenFavs) {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = "Favoritos",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                    IconButton(onClick = onOpenQr) {
                        Icon(
                            Icons.Filled.QrCode,
                            contentDescription = "QR",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Icon(
                        imageVector = Icons.Filled.ShoppingCart,
                        contentDescription = "Carrito",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceVariant) {
                NavigationBarItem(
                    selected = bottomSelected == 0,
                    onClick = { bottomSelected = 0 },
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") }
                )
                NavigationBarItem(
                    selected = bottomSelected == 1,
                    onClick = { bottomSelected = 1; onOpenCategories() },
                    icon = { Icon(Icons.Filled.Category, contentDescription = "Categorías") },
                    label = { Text("Categorías") }
                )
                NavigationBarItem(
                    selected = bottomSelected == 2,
                    onClick = onOpenFavs,
                    icon = { Icon(Icons.Filled.Favorite, contentDescription = "Favoritos") },
                    label = { Text("Favoritos") }
                )
                NavigationBarItem(
                    selected = bottomSelected == 3,
                    onClick = onOpenQr,
                    icon = { Icon(Icons.Filled.QrCode, contentDescription = "QR") },
                    label = { Text("QR") }
                )
            }
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
        ) {

            // --- Buscador (estilo Falabella) ---
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("¿Qué quieres buscar?") },
                leadingIcon = { Icon(Icons.Filled.QrCode, contentDescription = null) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            )

            // --- Chips de categorías ---
            LazyRow(
                contentPadding = PaddingValues(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories.size) { idx ->
                    FilterChip(
                        selected = selectedCat == idx,
                        onClick = { selectedCat = idx },
                        label = { Text(categories[idx]) }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // --- Banner / carrusel simple (placeholder visual) ---
            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(120.dp)
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
                    Text(
                        "Promociones Especiales",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // --- Destacados (lista horizontal) ---
            SectionTitle("Destacados")
            LazyRow(
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(games) { g ->
                    ProductCardHorizontal(g = g, onOpenDetail = onOpenDetail)
                }
            }

            // --- Catálogo (grid 2 columnas) ---
            SectionTitle("Todos los productos")
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(games) { g ->
                    ProductCardGrid(g = g, onOpenDetail = onOpenDetail)
                }
            }
        }
    }
}

// ---------- Helpers UI ----------

@Composable
private fun SectionTitle(text: String) {
    Text(
        text,
        color = MaterialTheme.colorScheme.secondary,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
        modifier = Modifier.padding(start = 16.dp, top = 8.dp)
    )
}

@Composable
private fun ProductCardHorizontal(g: Game, onOpenDetail: (String) -> Unit) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier
            .width(260.dp)
            .height(120.dp)
            .clickable { onOpenDetail(g.id.toString()) }
    ) {
        Row(Modifier.padding(12.dp)) {
            val context = LocalContext.current
            val resId = remember(g.imageResName, context) {
                context.resources.getIdentifier(g.imageResName, "drawable", context.packageName)
            }.takeIf { it != 0 } ?: R.drawable.ic_launcher_foreground

            Image(
                painter = painterResource(id = resId),
                contentDescription = g.title,
                modifier = Modifier
                    .size(64.dp)              // o .height(90.dp) en la grid
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.fillMaxWidth()) {
                Text(g.title, color = MaterialTheme.colorScheme.primary, maxLines = 1)
                Text("Precio: $${g.price}")
                g.offerPrice?.let { Text("Oferta: $${it}", color = MaterialTheme.colorScheme.secondary) }
            }
        }
    }
}

@Composable
private fun ProductCardGrid(g: Game, onOpenDetail: (String) -> Unit) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenDetail(g.id.toString()) }
    ) {
        Column(Modifier.padding(12.dp)) {
            val context = LocalContext.current
            val resId = remember(g.imageResName, context) {
                context.resources.getIdentifier(g.imageResName, "drawable", context.packageName)
            }.takeIf { it != 0 } ?: R.drawable.ic_launcher_foreground

            Image(
                painter = painterResource(id = resId),
                contentDescription = g.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(g.title, color = MaterialTheme.colorScheme.primary, maxLines = 2)
            Text("Precio: $${g.price}")
            g.offerPrice?.let { Text("Oferta: $${it}", color = MaterialTheme.colorScheme.secondary) }
        }
    }
}
