package com.example.ejemplo_level_up.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// 1) Define los esquemas con nombres coherentes
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = Teal80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = Teal40
)

// 2) Un único theme "oficial" para tu app
@Composable
fun EjemplolevelupTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    // Si quieres soportar Material You dinámico (Android 12+)
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val ctx = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(ctx) else dynamicLightColorScheme(ctx)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // ver nota abajo
        content = content
    )
}
