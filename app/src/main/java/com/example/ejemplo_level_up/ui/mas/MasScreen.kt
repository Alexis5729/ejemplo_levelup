package com.example.ejemplo_level_up.ui.mas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MasScreen(
    onOpenQr: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenAbout: () -> Unit,
    onOpenMap: () -> Unit,
    onOpenApi: () -> Unit,   // ✅ NUEVO
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            MasTopBar(onBack = onBack)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {

            // ---------- ÍTEM: QR ----------
            MasItemRow(
                icon = { Icon(Icons.Filled.QrCode, contentDescription = "QR") },
                title = "Escanear código QR",
                subtitle = "Escanea códigos para promociones y productos",
                onClick = onOpenQr
            )

            Divider()

            // ---------- ÍTEM: Ajustes ----------
            MasItemRow(
                icon = { Icon(Icons.Filled.Settings, contentDescription = "Ajustes") },
                title = "Ajustes",
                subtitle = "Notificaciones, tema y preferencias",
                onClick = onOpenSettings
            )

            Divider()

            // ---------- ÍTEM: Información de la app ----------
            MasItemRow(
                icon = { Icon(Icons.Filled.Info, contentDescription = "Sobre la app") },
                title = "Información de la app",
                subtitle = "Versión, desarrolladores y detalles",
                onClick = onOpenAbout
            )

            Divider()

            // ---------- ÍTEM: Mapa de sucursales ----------
            MasItemRow(
                icon = { Icon(Icons.Filled.LocationOn, contentDescription = "Mapa sucursales") },
                title = "Encuentra nuestras sucursales aquí",
                subtitle = "Abrir mapa con nuestras tiendas",
                onClick = onOpenMap
            )

            Divider()

            // ---------- ÍTEM: API externa (posts) ----------
            MasItemRow(
                icon = { Icon(Icons.Filled.Info, contentDescription = "API externa") },
                title = "Noticias / Posts (API externa)",
                subtitle = "Consume datos desde internet con Retrofit",
                onClick = onOpenApi
            )

            Divider()
        }
    }
}

@Composable
private fun MasTopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Volver"
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = "Más opciones",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun MasItemRow(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
