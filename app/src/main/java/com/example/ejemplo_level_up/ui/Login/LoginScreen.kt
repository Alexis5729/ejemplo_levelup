package com.example.ejemplo_level_up.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ejemplo_level_up.R
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    onBack: (() -> Unit)? = null
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Ocultar alerta tras unos segundos
    LaunchedEffect(showError) {
        if (showError) {
            delay(4000)
            showError = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Iniciar sesión",
                        color = Color(0xFF00C8FF),
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onBack?.invoke() },
                        enabled = onBack != null
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Volver",
                            tint = if (onBack != null) Color(0xFF00C8FF) else Color(0xFF404040)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0A0A0A)
                )
            )
        },
        containerColor = Color(0xFF0A0A0A)
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFF0A0A0A))
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título principal
            Text(
                text = "Ingresa a nuestro portal!\nLevel-Up Gamer",
                fontSize = 24.sp,
                color = Color(0xFF00C8FF),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Campo de correo
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico", color = Color(0xFFB0B0B0)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xFF00C8FF),
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = Color(0xFF00C8FF)
                )
            )

            // Campo de contraseña
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña", color = Color(0xFFB0B0B0)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xFF00C8FF),
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = Color(0xFF00C8FF)
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de iniciar sesión
            Button(
                onClick = {
                    when {
                        email.isBlank() || password.isBlank() -> {
                            errorMessage = "Por favor completa ambos campos."
                            showError = true
                        }
                        !email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) -> {
                            errorMessage = "El correo electrónico no es válido."
                            showError = true
                        }
                        password.length < 6 -> {
                            errorMessage = "La contraseña debe tener al menos 6 caracteres."
                            showError = true
                        }
                        else -> {
                            onLoginSuccess()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C8FF)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Iniciar sesión", color = Color.Black, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onRegisterClick,
                border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00C8FF)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Registrarse", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Alerta de error (evita parpadeo)
            if (showError) {
                AlertDialog(
                    onDismissRequest = { showError = false },
                    confirmButton = {
                        TextButton(onClick = { showError = false }) {
                            Text("Aceptar", color = Color(0xFF00C8FF))
                        }
                    },
                    title = { Text("Error de validación") },
                    text = { Text(errorMessage) },
                    containerColor = Color(0xFF1A1A1A),
                    titleContentColor = Color(0xFFFF6B6B),
                    textContentColor = Color.White
                )
            }
        }
    }
}
