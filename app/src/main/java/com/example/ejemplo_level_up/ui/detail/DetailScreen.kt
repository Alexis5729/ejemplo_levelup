package com.example.ejemplo_level_up.ui.detail

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ejemplo_level_up.viewmodel.DetailViewModel
import com.example.ejemplo_level_up.viewmodel.FavoritesViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(id: String, dvm: DetailViewModel = viewModel(), fvm: FavoritesViewModel = viewModel()) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val game by dvm.game(id).collectAsState(initial = null)
    val favs by fvm.favIds.collectAsState(initial = emptySet())
    val isFav = id in favs

    Scaffold(topBar = { TopAppBar(title = { Text(game?.title ?: "Detalle") }) }) { padding ->
        if (game == null) {
            Text("Juego no encontrado", Modifier.padding(padding).padding(16.dp)); return@Scaffold
        }
        Column(Modifier.padding(padding).padding(16.dp)) {
            Text(game!!.description)
            Spacer(Modifier.height(12.dp))
            Text("Precio: $${game!!.price}")
            game!!.offerPrice?.let { Text("Oferta: $${it}") }
            Spacer(Modifier.height(16.dp))
            Row {
                Button(onClick = { scope.launch { fvm.toggle(id) } }) {
                    Text(if (isFav) "Quitar de Favoritos" else "Agregar a Favoritos")
                }
                Spacer(Modifier.width(12.dp))
                Button(onClick = {
                    val i = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "Mira este juego: ${game!!.title}")
                    }
                    ctx.startActivity(Intent.createChooser(i, "Compartir con…"))
                }) { Text("Compartir") }
            }
        }
    }
}