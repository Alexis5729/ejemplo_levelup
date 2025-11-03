package com.example.ejemplo_level_up.ui.register

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ejemplo_level_up.data.UserManager
import com.example.ejemplo_level_up.ui.profile.UserProfile
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: (UserProfile) -> Unit,
    onBackToLogin: () -> Unit
) {
    val context = LocalContext.current
    val userManager = remember { UserManager(context) }

    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var comuna by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }

    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showSuccess by remember { mutableStateOf(false) }

    // 🔹 Cierra alerta automáticamente después de unos segundos
    LaunchedEffect(showError, showSuccess) {
        if (showError || showSuccess) {
            delay(4000)
            showError = false
            showSuccess = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de Cliente", color = Color(0xFF00C8FF)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0A0A0A))
            )
        },
        containerColor = Color(0xFF0A0A0A)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔹 Campos del formulario
            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre", color = Color(0xFFB0B0B0)) }, singleLine = true, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = apellido, onValueChange = { apellido = it }, label = { Text("Apellido", color = Color(0xFFB0B0B0)) }, singleLine = true, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = rut, onValueChange = { rut = it }, label = { Text("RUT", color = Color(0xFFB0B0B0)) }, singleLine = true, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = direccion, onValueChange = { direccion = it }, label = { Text("Dirección", color = Color(0xFFB0B0B0)) }, singleLine = true, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = comuna, onValueChange = { comuna = it }, label = { Text("Comuna", color = Color(0xFFB0B0B0)) }, singleLine = true, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono", color = Color(0xFFB0B0B0)) }, singleLine = true, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = correo, onValueChange = { correo = it }, label = { Text("Correo electrónico", color = Color(0xFFB0B0B0)) }, singleLine = true, modifier = Modifier.fillMaxWidth())

            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña", color = Color(0xFFB0B0B0)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = confirmarContrasena,
                onValueChange = { confirmarContrasena = it },
                label = { Text("Confirmar contraseña", color = Color(0xFFB0B0B0)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 🔹 Botón de registro
            Button(
                onClick = {
                    when {
                        nombre.isBlank() || apellido.isBlank() || rut.isBlank() || direccion.isBlank() ||
                                comuna.isBlank() || telefono.isBlank() || correo.isBlank() ||
                                contrasena.isBlank() || confirmarContrasena.isBlank() -> {
                            errorMessage = "Por favor completa todos los campos."
                            showError = true
                        }

                        !correo.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) -> {
                            errorMessage = "El correo electrónico no es válido."
                            showError = true
                        }

                        contrasena.length < 6 -> {
                            errorMessage = "La contraseña debe tener al menos 6 caracteres."
                            showError = true
                        }

                        contrasena != confirmarContrasena -> {
                            errorMessage = "Las contraseñas no coinciden."
                            showError = true
                        }

                        userManager.isUserRegistered(correo) -> {
                            errorMessage = "Este correo ya está registrado. Intenta iniciar sesión."
                            showError = true
                        }

                        else -> {
                            // Guardar usuario
                            userManager.registerUser(correo, contrasena)

                            val nuevoUsuario = UserProfile(
                                firstName = nombre,
                                lastName = apellido,
                                rut = rut,
                                address = direccion,
                                comuna = comuna,
                                phone = telefono,
                                email = correo
                            )

                            showSuccess = true
                            onRegisterSuccess(nuevoUsuario)
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C8FF)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Guardar", color = Color.Black, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 🔹 Botón cancelar
            OutlinedButton(
                onClick = { onBackToLogin() },
                border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00C8FF)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Cancelar", fontSize = 16.sp)
            }

            // 🔹 Alerta de error
            if (showError) {
                AlertDialog(
                    onDismissRequest = { showError = false },
                    confirmButton = {
                        TextButton(onClick = { showError = false }) {
                            Text("Aceptar", color = Color(0xFF00C8FF))
                        }
                    },
                    title = { Text("Error en el registro") },
                    text = { Text(errorMessage) },
                    containerColor = Color(0xFF1A1A1A),
                    titleContentColor = Color(0xFFFF6B6B),
                    textContentColor = Color.White
                )
            }

            // 🔹 Alerta de éxito
            if (showSuccess) {
                AlertDialog(
                    onDismissRequest = { showSuccess = false },
                    confirmButton = {
                        TextButton(onClick = { showSuccess = false }) {
                            Text("Aceptar", color = Color(0xFF00C8FF))
                        }
                    },
                    title = { Text("Registro exitoso") },
                    text = { Text("Tu cuenta ha sido creada correctamente. ¡Ahora puedes iniciar sesión!") },
                    containerColor = Color(0xFF1A1A1A),
                    titleContentColor = Color(0xFF4CAF50),
                    textContentColor = Color.White
                )
            }
        }
    }
}
