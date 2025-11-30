package com.example.ejemplo_level_up.navigation

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ejemplo_level_up.ui.cart.CartScreen
import com.example.ejemplo_level_up.ui.categories.CategoriesScreen
import com.example.ejemplo_level_up.ui.detail.DetailScreen
import com.example.ejemplo_level_up.ui.favorites.FavoritesScreen
import com.example.ejemplo_level_up.ui.home.HomeScreen
import com.example.ejemplo_level_up.ui.login.LoginScreen
import com.example.ejemplo_level_up.ui.map.MapScreen
import com.example.ejemplo_level_up.ui.mas.MasScreen
import com.example.ejemplo_level_up.ui.profile.EditProfileScreen
import com.example.ejemplo_level_up.ui.profile.ProfileScreen
import com.example.ejemplo_level_up.ui.profile.UserProfile
import com.example.ejemplo_level_up.ui.qr.QrScannerScreen
import com.example.ejemplo_level_up.ui.register.RegisterScreen
import com.example.ejemplo_level_up.ui.splash.SplashScreen
import com.example.ejemplo_level_up.viewmodel.CartViewModel   // 👈 NUEVO

// ---------- Definición de rutas ----------
object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val DETAIL = "detail/{id}"
    const val CATEGORIES = "categories"
    const val FAVS = "favs"
    const val QR = "qr"
    const val PROFILE = "profile"
    const val EDIT_PROFILE = "edit_profile"
    const val CART = "cart"
    const val MAS = "mas"
    const val MAP = "map"
}

@Composable
fun AppNav(nav: NavHostController) {
    val ctx = LocalContext.current

    // ---------- Estado de sesión ----------
    var isLoggedIn by rememberSaveable { mutableStateOf(false) }

    // ---------- Datos del usuario ----------
    var user by remember {
        mutableStateOf(
            UserProfile(
                firstName = "Juan",
                lastName = "Pérez",
                rut = "12.345.678-9",
                address = "Av. Los Leones 123",
                comuna = "Providencia",
                phone = "+56 9 8765 4321",
                email = "juan@duocuc.cl"
            )
        )
    }

    // ---------- CartViewModel compartido ----------
    val cartVm: CartViewModel = viewModel()

    // ---------- Controlador principal ----------
    NavHost(
        navController = nav,
        startDestination = Routes.SPLASH
    ) {

        // ---------- SPLASH ----------
        composable(Routes.SPLASH) {
            SplashScreen(navController = nav)
        }

        // ---------- LOGIN ----------
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    isLoggedIn = true
                    nav.navigate(Routes.PROFILE) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onRegisterClick = { nav.navigate(Routes.REGISTER) },
                onBack = {
                    if (!nav.popBackStack()) nav.navigate(Routes.HOME)
                }
            )
        }

        // ---------- REGISTRO ----------
        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = { newUser ->
                    isLoggedIn = true
                    user = newUser
                    nav.navigate(Routes.PROFILE) {
                        popUpTo(Routes.HOME) { inclusive = false }
                        launchSingleTop = true
                        restoreState = false
                    }
                },
                onBackToLogin = { nav.popBackStack() }
            )
        }

        // ---------- HOME ----------
        composable(Routes.HOME) {
            HomeScreen(
                onOpenDetail = { nav.navigate("detail/$it") },
                onOpenFavs = { nav.navigate(Routes.FAVS) },
                onOpenQr = { nav.navigate(Routes.QR) },
                onOpenCategories = { nav.navigate(Routes.CATEGORIES) },
                onOpenProfile = {
                    if (isLoggedIn) nav.navigate(Routes.PROFILE)
                    else nav.navigate(Routes.LOGIN)
                },
                onOpenCart = { nav.navigate(Routes.CART) },
                onOpenMore = { nav.navigate(Routes.MAS) },
                user = user,
                isLoggedIn = isLoggedIn,
                onLogout = {
                    isLoggedIn = false
                    user = UserProfile()
                    nav.navigate(Routes.SPLASH) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                }
            )
        }

        // ---------- CATEGORÍAS ----------
        composable(Routes.CATEGORIES) {
            val vm: com.example.ejemplo_level_up.viewmodel.HomeViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel()
            val games by vm.games.collectAsState(initial = emptyList())

            CategoriesScreen(
                products = games,
                onOpenDetail = { id -> nav.navigate("detail/$id") },
                onBack = { nav.popBackStack() },
                onOpenCart = { nav.navigate(Routes.CART) }
            )
        }

        // ---------- FAVORITOS ----------
        composable(Routes.FAVS) {
            FavoritesScreen(
                onOpenDetail = { nav.navigate("detail/$it") },
                onBack = { nav.popBackStack() },
                onOpenCart = { nav.navigate(Routes.CART) }
            )
        }

        // ---------- CARRITO ----------
        composable(Routes.CART) {
            CartScreen(
                user = user,
                isLoggedIn = isLoggedIn,
                onBack = { nav.popBackStack() },
                onLoginHere = { nav.navigate(Routes.LOGIN) },
                onLogout = {
                    isLoggedIn = false
                    user = UserProfile()
                    nav.navigate(Routes.CART)
                },
                onCartClick = { /* ya estás en el carrito */ },
                cartViewModel = cartVm          // 👈 usa el VM compartido
            )
        }

        // ---------- QR ----------
        composable(Routes.QR) {
            QrScannerScreen(
                onResult = { value ->
                    if (value.startsWith("http")) {
                        ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(value)))
                    } else {
                        nav.navigate("detail/$value")
                    }
                },
                onBack = { nav.popBackStack() }
            )
        }

        // ---------- DETALLE ----------
        composable(Routes.DETAIL) { backStack ->
            val id = backStack.arguments?.getString("id") ?: return@composable
            DetailScreen(
                id = id,
                onBack = { nav.popBackStack() },
                cvm = cartVm        // 👈 le pasas el mismo CartViewModel
            )
        }

        // ---------- PERFIL ----------
        composable(Routes.PROFILE) {
            if (isLoggedIn) {
                ProfileScreen(
                    userData = user,
                    onEditProfile = { nav.navigate(Routes.EDIT_PROFILE) },
                    onBack = {
                        if (!nav.popBackStack()) nav.navigate(Routes.HOME)
                    },
                    onOpenCart = { nav.navigate(Routes.CART) },
                    user = user,
                    isLoggedIn = isLoggedIn,
                    onLogout = {
                        user = UserProfile()
                        isLoggedIn = false
                        nav.navigate(Routes.LOGIN) {
                            popUpTo(Routes.HOME) { inclusive = true }
                            launchSingleTop = true
                            restoreState = false
                        }
                    }
                )
            }
        }

        // ---------- EDITAR PERFIL ----------
        composable(Routes.EDIT_PROFILE) {
            EditProfileScreen(
                userData = user,
                onCancel = { nav.popBackStack() },
                onSave = { updated ->
                    user = updated
                    nav.popBackStack()
                }
            )
        }

        // ---------- MÁS ----------
        composable(Routes.MAS) {
            MasScreen(
                onOpenQr = { nav.navigate(Routes.QR) },
                onOpenSettings = { /* TODO */ },
                onOpenAbout = { /* TODO */ },
                onOpenMap = { nav.navigate(Routes.MAP) },
                onBack = { nav.popBackStack() }
            )
        }

        // ---------- MAPA DE SUCURSALES ----------
        composable(Routes.MAP) {
            MapScreen(
                onBack = { nav.popBackStack() }
            )
        }
    }
}
