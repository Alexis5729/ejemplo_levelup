package com.example.ejemplo_level_up.ui.profile

import androidx.compose.animation.AnimatedVisibility
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
fun EditProfileScreen(
    userData: UserProfile,
    onCancel: () -> Unit,
    onSave: (UserProfile) -> Unit
) {
    var editedUser by remember { mutableStateOf(userData.copy()) }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    // 🔹 Oculta el mensaje de error automáticamente
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
                        text = "Modificar datos del cliente",
                        color = Color(0xFF00C8FF),
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onCancel() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Volver",
                            tint = Color(0xFF00C8FF)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0A0A0A))
            )
        },
        containerColor = Color(0xFF0A0A0A)
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .background(Color(0xFF0A0A0A)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            // ---------- CAMPOS ----------
            EditableField("Nombres", editedUser.firstName) { editedUser = editedUser.copy(firstName = it) }
            EditableField("Apellidos", editedUser.lastName) { editedUser = editedUser.copy(lastName = it) }
            EditableField("RUT", editedUser.rut) { editedUser = editedUser.copy(rut = it) }
            EditableField("Dirección", editedUser.address) { editedUser = editedUser.copy(address = it) }
            EditableField("Comuna", editedUser.comuna) { editedUser = editedUser.copy(comuna = it) }
            EditableField("Teléfono", editedUser.phone, KeyboardType.Phone) { editedUser = editedUser.copy(phone = it) }
            EditableField("Correo electrónico", editedUser.email, KeyboardType.Email) { editedUser = editedUser.copy(email = it) }

            // ---------- CONTRASEÑA ----------
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Nueva contraseña", color = Color(0xFFB0B0B0)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xFF00C8FF),
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = Color(0xFF00C8FF)
                )
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar nueva contraseña", color = Color(0xFFB0B0B0)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xFF00C8FF),
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = Color(0xFF00C8FF)
                )
            )

            Spacer(Modifier.height(24.dp))

            // ---------- BOTONES ----------
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00C8FF)),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .padding(end = 6.dp)
                ) {
                    Text("Cancelar", fontSize = 16.sp)
                }

                Button(
                    onClick = {
                        when {
                            editedUser.firstName.isBlank() || editedUser.lastName.isBlank() ||
                                    editedUser.rut.isBlank() || editedUser.address.isBlank() ||
                                    editedUser.comuna.isBlank() || editedUser.phone.isBlank() ||
                                    editedUser.email.isBlank() -> {
                                errorMessage = "Por favor completa todos los campos."
                                showError = true
                            }

                            !editedUser.email.matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) -> {
                                errorMessage = "El correo electrónico no es válido."
                                showError = true
                            }

                            password.isNotEmpty() && password.length < 6 -> {
                                errorMessage = "La contraseña debe tener al menos 6 caracteres."
                                showError = true
                            }

                            password != confirmPassword -> {
                                errorMessage = "Las contraseñas no coinciden."
                                showError = true
                            }

                            else -> {
                                //  Si se cambia la contraseña, la ignoramos para demo (no persistente)
                                showError = false
                                onSave(editedUser)
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C8FF)),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .padding(start = 6.dp)
                ) {
                    Text("Guardar", color = Color.Black, fontSize = 16.sp)
                }
            }

            Spacer(Modifier.height(16.dp))

            // ---------- BANNER DE ERROR ----------
            AnimatedVisibility(visible = showError) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFF3D3D).copy(alpha = 0.15f))
                        .padding(12.dp)
                ) {
                    Text(
                        text = errorMessage,
                        color = Color(0xFFFF6B6B),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
private fun EditableField(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        label = { Text(label, color = Color(0xFFB0B0B0)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color(0xFF00C8FF),
            unfocusedIndicatorColor = Color.Gray,
            cursorColor = Color(0xFF00C8FF)
        )
    )
}
