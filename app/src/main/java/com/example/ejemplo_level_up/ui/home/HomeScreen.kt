package com.example.ejemplo_level_up.ui.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.core.content.ContextCompat
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
    onOpenQr: () -> Unit,
    onOpenCategories: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenCart: () -> Unit,
    onOpenMore: () -> Unit,
    user: UserProfile?,
    isLoggedIn: Boolean,
    onLogout: () -> Unit,
    vm: HomeViewModel = viewModel()
) {
    val context = LocalContext.current

    // Seed inicial de juegos + primera carga de comuna
    LaunchedEffect(Unit) {
        vm.seed()

        if (hasLocationPermission(context)) {
            vm.cargarComuna()
        } else {
            vm.setComunaNoDisponible()
        }
    }

    val games by vm.games.collectAsState(initial = emptyList())
    val comuna by vm.comuna.collectAsState()

    var query by rememberSaveable { mutableStateOf("") }
    val categories = listOf("Periféricos", "Accesorios", "Juegos", "Consolas", "PC", "Ofertas")
    var selectedCat by rememberSaveable { mutableStateOf(0) }
    var bottomSelected by rememberSaveable { mutableStateOf(0) }

    // Launcher para pedir permisos
    val locationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val granted =
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                        permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            if (granted) {
                vm.cargarComuna()
            } else {
                vm.setComunaNoDisponible()
            }
        }

    // Si no tenemos permisos, los pedimos una sola vez
    LaunchedEffect(Unit) {
        if (!hasLocationPermission(context)) {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            MainTopBar(
                user = user,
                isLoggedIn = isLoggedIn,
                onLogout = onLogout,
                onCartClick = onOpenCart
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
                    onClick = {
                        bottomSelected = 1
                        onOpenCategories()
                    },
                    icon = { Icon(Icons.Filled.Category, contentDescription = "Categorías") },
                    label = { Text("Categorías") }
                )
                NavigationBarItem(
                    selected = bottomSelected == 2,
                    onClick = {
                        bottomSelected = 2
                        onOpenFavs()
                    },
                    icon = { Icon(Icons.Filled.Favorite, contentDescription = "Favoritos") },
                    label = { Text("Favoritos") }
                )
                NavigationBarItem(
                    selected = bottomSelected == 3,
                    onClick = {
                        bottomSelected = 3
                        onOpenProfile()
                    },
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") }
                )
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
            // Buscador
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

            // Texto de comuna bajo el saludo
            comuna?.let { c ->
                val text = when (c) {
                    "NO_DISPONIBLE" ->
                        "No pudimos obtener tu comuna (permiso denegado o sin señal)."
                    else ->
                        "Actualmente estás en la comuna de $c"
                }

                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp)
                )
            }

            // Chips de categorías
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

            // Destacados
            SectionTitle("Destacados")
            LazyRow(
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(games) { g ->
                    ProductCardHorizontal(g = g, onOpenDetail = onOpenDetail)
                }
            }

            // Catálogo
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

// ---------- Helpers y UI auxiliares ----------

private fun hasLocationPermission(context: Context): Boolean {
    val fine = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val coarse = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    return fine || coarse
}

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
                g.offerPrice?.let {
                    Text("Oferta: $${it}", color = MaterialTheme.colorScheme.secondary)
                }
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
            g.offerPrice?.let {
                Text("Oferta: $${it}", color = MaterialTheme.colorScheme.secondary)
            }
        }
    }
}