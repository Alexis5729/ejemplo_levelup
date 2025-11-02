package com.example.ejemplo_level_up.ui.theme

import android.os.Build
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary        = NeonBlue,
    onPrimary      = TextPrimary,
    secondary      = NeonGreen,
    onSecondary    = TextPrimary,
    tertiary       = NeonCyan,
    onTertiary     = TextPrimary,
    background     = DarkBg,
    onBackground   = TextSecondary,
    surface        = DarkSurface,
    onSurface      = TextPrimary,
    surfaceVariant = SurfaceDim,
    onSurfaceVariant = TextSecondary,
    outline        = OutlineNeon,
    surfaceBright    = CarrouselH
)

// (Opcional) Un claro mínimo, por si activas light
private val LightColorScheme = lightColorScheme(
    primary      = NeonBlue,
    onPrimary    = TextPrimary,
    secondary    = NeonGreen,
    onSecondary  = TextPrimary,
    background   = Color(0xFFF5F7FA),
    onBackground = Color(0xFF141414),
    surface      = Color(0xFFFFFFFF),
    onSurface    = Color(0xFF141414),
    outline      = OutlineNeon
)

@Composable
fun EjemplolevelupTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // ¡Importante! Desactivamos dynamicColor para que NO reemplace tu paleta neón
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme =
        if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val ctx = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(ctx) else dynamicLightColorScheme(ctx)
        } else {
            if (darkTheme) DarkColorScheme else LightColorScheme
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}