package com.example.ejemplo_level_up.ui.detail

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ejemplo_level_up.ui.components.TopBar
import com.example.ejemplo_level_up.viewmodel.DetailViewModel
import com.example.ejemplo_level_up.viewmodel.FavoritesViewModel
import com.example.ejemplo_level_up.viewmodel.CartViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    id: String,
    onBack: () -> Unit, // 👈 nuevo parámetro para volver atrás
    dvm: DetailViewModel = viewModel(),
    fvm: FavoritesViewModel = viewModel(),
    cvm: CartViewModel
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val game by dvm.game(id).collectAsState(initial = null)
    val favs by fvm.favIds.collectAsState(initial = emptySet())
    val isFav = id in favs
    val isInCart by cvm.items.collectAsState()
    val existsInCart = isInCart.containsKey(id)


    Scaffold(
        topBar = {
            TopBar(
                title = game?.title ?: "Detalle del producto",
                onBack = onBack
            )
        }
    ) { padding ->
        if (game == null) {
            Text(
                "Juego no encontrado",
                Modifier
                    .padding(padding)
                    .padding(16.dp)
            )
            return@Scaffold
        }

        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // --- Descripción del producto ---
            Text(game!!.description)
            Spacer(Modifier.height(12.dp))
            Text("Precio: $${game!!.price}")
            game!!.offerPrice?.let {
                Text("Oferta: $${it}", color = MaterialTheme.colorScheme.secondary)
            }

            Spacer(Modifier.height(24.dp))

            // --- Botones principales ---
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                // Favoritos
                Button(onClick = { scope.launch { fvm.toggle(id) } }) {
                    Text(if (isFav) "Quitar de Favoritos" else "Agregar a Favoritos")
                }

                // 🛒 NUEVO: Agregar al carrito
                Button(
                    onClick = {
                        scope.launch {
                            if (existsInCart) cvm.remove(id) else cvm.add(id)
                        }
                    }
                ) {
                    Text(
                        text = if (existsInCart) "Quitar del carrito" else "Agregar al carrito"
                    )
                }


                // Compartir
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "Mira este artículo: ${game!!.title} — Precio: $${game!!.price}"
                            )
                        }
                        ctx.startActivity(Intent.createChooser(intent, "Compartir con…"))
                    }
                ) {
                    Text("Compartir")
                }
            }
        }
    }
}
