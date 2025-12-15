package com.example.ejemplo_level_up.ui.api

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ejemplo_level_up.viewmodel.PostViewModel

private fun noticiaLevelUp(id: Int): Pair<String, String> {
    return when (id % 5) {
        0 -> "🔥 Ofertas Gamer de la Semana" to
                "Aprovecha descuentos especiales en audífonos, teclados y mouse gamer."
        1 -> "🎮 Nuevos Productos en Level-Up" to
                "Revisa los últimos accesorios y periféricos disponibles en nuestra tienda."
        2 -> "⚡ Promoción Especial en Consolas" to
                "Esta semana tenemos precios especiales en consolas y controles."
        3 -> "🕹️ Accesorios Imperdibles" to
                "Equipa tu setup con lo mejor en accesorios gamer seleccionados."
        else -> "🏆 Recomendados Level-Up" to
                "Nuestro equipo recomienda los productos más vendidos del mes."
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    onBack: (() -> Unit)? = null,
    vm: PostViewModel = viewModel()
) {
    val posts by vm.posts.collectAsState()
    val loading by vm.isLoading.collectAsState()
    val error by vm.error.collectAsState()

    LaunchedEffect(Unit) { vm.fetchPosts() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📰 Noticias Level-Up") },
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(onClick = onBack) { Text("←") }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(12.dp)
        ) {

            when {
                loading -> {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(12.dp))
                    Text("Cargando novedades…")
                }

                error != null -> {
                    Text("Error: $error", color = MaterialTheme.colorScheme.error)
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = { vm.fetchPosts() }) {
                        Text("Reintentar")
                    }
                }

                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(posts) { p ->

                            // ✅ Adaptación visual al contexto Level-Up (sin cambiar la API)
                            val (titulo, descripcion) = noticiaLevelUp(p.id)

                            ElevatedCard {
                                Column(Modifier.padding(12.dp)) {

                                    Text(
                                        text = "🎮 Noticia gamer #${p.id}",
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    Spacer(Modifier.height(6.dp))

                                    Text(
                                        text = titulo,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Medium
                                    )

                                    Spacer(Modifier.height(8.dp))

                                    Text(text = descripcion)

                                    Spacer(Modifier.height(10.dp))

                                    Text(
                                        text = "Ver detalles en Level-Up →",
                                        color = MaterialTheme.colorScheme.primary,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
