package com.example.ejemplo_level_up.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ejemplo_level_up.ui.profile.UserProfile

@Composable
fun MainTopBar(
    user: UserProfile?,
    isLoggedIn: Boolean,
    onLogout: () -> Unit,
    onCartClick: () -> Unit // 🔹 Ahora obligatorio (siempre activo)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // 🔹 Texto de título o saludo
        Text(
            text = if (isLoggedIn)
                "👋 Bienvenido, ${user?.firstName ?: "Usuario"}"
            else
                "Level-Up Gamer",
            color = Color(0xFF00C8FF),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )

        // 🔹 Iconos visibles (Carrito siempre, Logout solo si hay sesión)
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onCartClick) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Carrito de compras",
                    tint = Color(0xFF00C8FF)
                )
            }


            if (isLoggedIn) {
                IconButton(onClick = onLogout) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Cerrar sesión",
                        tint = Color(0xFFFF6F61)
                    )
                }
            }
        }
    }
}
