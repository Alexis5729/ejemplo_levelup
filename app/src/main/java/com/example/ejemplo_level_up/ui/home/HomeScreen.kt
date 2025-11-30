package com.example.ejemplo_level_up.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.ejemplo_level_up.ui.components.MainTopBar
import com.example.ejemplo_level_up.ui.profile.UserProfile
import com.example.ejemplo_level_up.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onOpenDetail: (String) -> Unit,
    onOpenFavs: () -> Unit,
    onOpenQr: () -> Unit,          // 👉 ahora se usará desde la vista "Más"
    onOpenCategories: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenCart: () -> Unit,        // ✅ Abre el carrito
    onOpenMore: () -> Unit,        // 🆕 Navega a la pantalla "Más"
    user: UserProfile?,
    isLoggedIn: Boolean,
    onLogout: () -> Unit,
    vm: HomeViewModel = viewModel()
) {
    LaunchedEffect(Unit) { vm.seed() }

    val games by vm.games.collectAsState(initial = emptyList())

    // Estado UI
    var query by rememberSaveable { mutableStateOf("") }
    val categories = listOf("Periféricos", "Accesorios", "Juegos", "Consolas", "PC", "Ofertas")
    var selectedCat by rememberSaveable { mutableStateOf(0) }
    var bottomSelected by rememberSaveable { mutableStateOf(0) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,

        // ✅ TopBar con carrito funcional
        topBar = {
            MainTopBar(
                user = user,
                isLoggedIn = isLoggedIn,
                onLogout = onLogout,
                onCartClick = onOpenCart // ✅ AHORA FUNCIONA DESDE HOME
            )
        },

        // ✅ Barra de navegación inferior
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceVariant) {

                // 1) INICIO
                NavigationBarItem(
                    selected = bottomSelected == 0,
                    onClick = { bottomSelected = 0 },
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") }
                )

                // 2) CATEGORÍAS
                NavigationBarItem(
                    selected = bottomSelected == 1,
                    onClick = {
                        bottomSelected = 1
                        onOpenCategories()
                    },
                    icon = { Icon(Icons.Filled.Category, contentDescription = "Categorías") },
                    label = { Text("Categorías") }
                )

                // 3) FAVORITOS
                NavigationBarItem(
                    selected = bottomSelected == 2,
                    onClick = {
                        bottomSelected = 2
                        onOpenFavs()
                    },
                    icon = { Icon(Icons.Filled.Favorite, contentDescription = "Favoritos") },
                    label = { Text("Favoritos") }
                )

                // 4) PERFIL
                NavigationBarItem(
                    selected = bottomSelected == 3,
                    onClick = {
                        bottomSelected = 3
                        onOpenProfile()
                    },
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") }
                )

                // 5) MÁS (usa ic_more.xml)
                NavigationBarItem(
                    selected = bottomSelected == 4,
                    onClick = {
                        bottomSelected = 4
                        onOpenMore()
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_more),
                            contentDescription = "Más"
                        )
                    },
                    label = { Text("Más") }
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
            // --- Buscador ---
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("¿Qué quieres buscar?") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
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

            // --- Destacados ---
            SectionTitle("Destacados")
            LazyRow(
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(games) { g ->
                    ProductCardHorizontal(g = g, onOpenDetail = onOpenDetail)
                }
            }

            // --- Catálogo ---
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

// ---------- Estilos del catálogo ----------

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
            containerColor = MaterialTheme.colorScheme.surfaceBright
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
                    .size(64.dp)
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
