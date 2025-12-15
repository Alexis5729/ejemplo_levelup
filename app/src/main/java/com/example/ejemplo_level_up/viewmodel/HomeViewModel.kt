package com.example.ejemplo_level_up.viewmodel

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ejemplo_level_up.data.database.GameDatabase
import com.example.ejemplo_level_up.data.model.Game
import com.example.ejemplo_level_up.data.repository.GameRepository
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

class HomeViewModel(app: Application) : AndroidViewModel(app) {

    // ---------- Repositorio y juegos ----------
    private val repo = GameRepository(GameDatabase.getInstance(app).gameDao())

    val games = repo
        .allGames()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // ---------- Estado de la comuna ----------
    // null            -> aún cargando / no se ha pedido
    // otro String     -> nombre de la comuna (incluye fallback "Mountain View")
    private val _comuna = MutableStateFlow<String?>(null)
    val comuna: StateFlow<String?> = _comuna

    // Si quieres seguir usando este helper, lo dejo
    fun setComunaNoDisponible() {
        _comuna.value = "Mountain View"
    }

    fun cargarComuna() {
        val context = getApplication<Application>()

        // ✅ 1. Revisar permisos
        val hasFine = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarse = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasFine && !hasCoarse) {
            // ❌ Sin permisos → usamos comuna por defecto del emulador
            _comuna.value = "Mountain View"
            return
        }

        val fusedClient = LocationServices.getFusedLocationProviderClient(context)

        // ✅ 2. Pedir última ubicación conocida
        fusedClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    try {
                        val geocoder = Geocoder(context, Locale.getDefault())
                        @Suppress("DEPRECATION")
                        val results = geocoder.getFromLocation(
                            location.latitude,
                            location.longitude,
                            1
                        )

                        val address = results?.firstOrNull()
                        val comunaName =
                            address?.subLocality ?:   // muchas comunas vienen aquí
                            address?.locality ?:
                            address?.subAdminArea

                        // ✅ 3. Si no se pudo determinar, usamos fallback
                        _comuna.value = comunaName ?: "Mountain View"
                    } catch (e: Exception) {
                        // Error con geocoder → fallback
                        _comuna.value = "Mountain View"
                    }
                } else {
                    // Sin ubicación → fallback
                    _comuna.value = "Mountain View"
                }
            }
            .addOnFailureListener {
                // Error al obtener ubicación → fallback
                _comuna.value = "Mountain View"
            }
    }

    // ---------- Seed de juegos ----------
    fun seed() = viewModelScope.launch {
        val seed = listOf(
            Game(
                "g101",
                "HyperX Cloud II Wireless",
                59990,
                49990,
                "Audífonos inalámbricos con sonido 7.1.",
                "hyperx_cloud2"
            ),
            Game(
                "g102",
                "Catan – El Juego",
                34990,
                29990,
                "Juego de mesa clásico de estrategia.",
                "catan"
            ),
            Game(
                "g103",
                "ASUS ROG Strix 16 RTX 4070",
                1999990,
                1799990,
                "Notebook gamer de alto rendimiento.",
                "rog_strix_4070"
            ),
            Game(
                "g104",
                "Polera Level-Up Gamer",
                12990,
                9990,
                "Polera temática Level-Up Gamer.",
                "polera_levelup"
            ),
            Game(
                "g105",
                "PlayStation 5 (Slim)",
                649990,
                599990,
                "Consola de última generación.",
                "ps5_box"
            ),
            Game(
                "g106",
                "Control Xbox – Carbon Black",
                49990,
                44990,
                "Control inalámbrico Xbox.",
                "xbox_controller"
            ),
            Game(
                "g107",
                "Logitech G502 HERO",
                39990,
                34990,
                "Mouse gamer con sensor HERO 25K.",
                "logitech_g502"
            )
        )
        repo.seedIfEmpty(seed)
    }
}
