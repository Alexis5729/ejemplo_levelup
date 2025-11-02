package com.example.ejemplo_level_up.ui.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ejemplo_level_up.R
import com.example.ejemplo_level_up.ui.components.MainTopBar
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userData: UserProfile,
    onEditProfile: () -> Unit,
    onPromoCode: () -> Unit,
    onBack: () -> Unit,
    onOpenCart: () -> Unit, // 🛒 nuevo parámetro agregado
    user: UserProfile?,
    isLoggedIn: Boolean,
    onLogout: () -> Unit
) {
    // ✅ Verifica si es usuario DUOC UC
    val isDuocUser = remember(userData.email) {
        userData.email.endsWith("@duocuc.cl", ignoreCase = true)
    }

    // ✅ Estado temporal para mostrar alerta DUOC UC
    var showAlert by remember { mutableStateOf(isDuocUser) }

    LaunchedEffect(isDuocUser) {
        if (isDuocUser) {
            showAlert = true
            delay(6000)
            showAlert = false
        }
    }

    // ✅ Solo muestra el contenido si hay sesión activa
    AnimatedVisibility(visible = isLoggedIn) {
        Scaffold(
            containerColor = Color(0xFF0A0A0A),
            topBar = {
                Column {
                    // 🔝 Barra superior con saludo + carrito + cerrar sesión
                    MainTopBar(
                        user = user,
                        isLoggedIn = isLoggedIn,
                        onLogout = onLogout,
                        onCartClick = onOpenCart // ✅ ahora funcional
                    )

                    // 🔙 Título con botón volver
                    TopAppBar(
                        title = { Text("Perfil de usuario", color = Color(0xFF00C8FF)) },
                        navigationIcon = {
                            IconButton(onClick = onBack) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_arrow_back),
                                    contentDescription = "Volver",
                                    tint = Color(0xFF00C8FF)
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color(0xFF0A0A0A)
                        )
                    )
                }
            }
        ) { padding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(12.dp))

                // ---------- ALERTA DESCUENTO DUOC ----------
                AnimatedVisibility(visible = showAlert) {
                    ElevatedCard(
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = Color(0xFFFFF3B0)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Text(
                            text = "🎓 ¡Descuento del 10% aplicado por ser estudiante DUOC UC!",
                            modifier = Modifier.padding(12.dp),
                            color = Color.Black,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                // ---------- BLOQUE DE DATOS DEL PERFIL ----------
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = Color(0xFF1A1A1A)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ProfileField("Nombres", userData.firstName)
                        ProfileField("Apellidos", userData.lastName)
                        ProfileField("RUT", userData.rut)
                        ProfileField("Dirección", userData.address)
                        ProfileField("Comuna", userData.comuna)
                        ProfileField("Teléfono", userData.phone)
                        ProfileField("Email", userData.email)
                        ProfileField("Contraseña", "••••••••")
                    }
                }

                Spacer(Modifier.height(24.dp))

                // ---------- BOTONES DE ACCIÓN ----------
                Button(
                    onClick = onEditProfile,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C8FF))
                ) {
                    Text("Modificar datos", color = Color.Black)
                }

                Spacer(Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onPromoCode,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00C8FF))
                ) {
                    Text("Ingresar código promocional")
                }
            }
        }
    }

    // ---------- ESTADO SIN SESIÓN (pantalla vacía) ----------
    AnimatedVisibility(visible = !isLoggedIn) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0A0A0A)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Tu sesión ha finalizado.\nInicia sesión para acceder a tu perfil.",
                color = Color(0xFFB0B0B0),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ProfileField(label: String, value: String) {
    Column {
        Text(label, fontWeight = FontWeight.Bold, color = Color(0xFF00C8FF))
        Text(
            if (value.isBlank()) "(No especificado)" else value,
            color = Color(0xFFB0B0B0)
        )
        Divider(Modifier.padding(vertical = 6.dp))
    }
}
