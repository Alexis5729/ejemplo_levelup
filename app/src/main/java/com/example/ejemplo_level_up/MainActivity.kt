package com.example.ejemplo_level_up

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.compose.rememberNavController
import com.example.ejemplo_level_up.navigation.AppNav
import com.example.ejemplo_level_up.ui.theme.EjemplolevelupTheme

class MainActivity : ComponentActivity() {

    // Launcher para solicitar permisos de ubicación
    private val locationPermissionRequest =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { /* Si quieres, puedes revisar el resultado aquí más adelante */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 📍 Pedimos permisos de ubicación al abrir la app
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        setContent {
            EjemplolevelupTheme(darkTheme = true, dynamicColor = false) {
                val nav = rememberNavController()
                AppNav(nav)
            }
        }
    }
}
