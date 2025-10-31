package com.example.ejemplo_level_up.navigation

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ejemplo_level_up.ui.detail.DetailScreen
import com.example.ejemplo_level_up.ui.favorites.FavoritesScreen
import com.example.ejemplo_level_up.ui.home.HomeScreen
import com.example.ejemplo_level_up.ui.qr.QrScannerScreen

object Routes {
    const val HOME = "home"
    const val DETAIL = "detail/{id}"
    const val CATEGORIES = "categories"
    const val FAVS = "favs"
    const val QR = "qr"
}

@Composable
fun AppNav(nav: NavHostController) {
    val ctx = LocalContext.current
    NavHost(nav, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(
                onOpenDetail = { nav.navigate("detail/$it") },
                onOpenFavs   = { nav.navigate(Routes.FAVS) },
                onOpenQr     = { nav.navigate(Routes.QR) },
                onOpenCategories = { nav.navigate(Routes.CATEGORIES) } // ← NUEVO
            )
        }

        composable(Routes.CATEGORIES) {
            // Leemos los productos desde el mismo VM (recomendado)
            val vm: com.example.ejemplo_level_up.viewmodel.HomeViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel()
            val games by vm.games.collectAsState(initial = emptyList())

            com.example.ejemplo_level_up.ui.categories.CategoriesScreen(
                products = games,
                onOpenDetail = { id -> nav.navigate("detail/$id") },
                onBack = { nav.popBackStack() }
            )
        }

        composable(Routes.FAVS) {
            FavoritesScreen(onOpenDetail = { nav.navigate("detail/$it") },
                onBack = { nav.popBackStack()})
        }

        composable(Routes.QR) {
            QrScannerScreen(
                onResult = { value ->
                    if (value.startsWith("http")) {
                        ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(value)))
                    } else {
                        nav.navigate("detail/$value")
                    }
                },
                onBack = { nav.popBackStack() } // ← flecha vuelve atrás
            )
        }


        composable(Routes.DETAIL) { backStack ->
            val id = backStack.arguments?.getString("id") ?: return@composable
            DetailScreen(id)
        }
    }
}