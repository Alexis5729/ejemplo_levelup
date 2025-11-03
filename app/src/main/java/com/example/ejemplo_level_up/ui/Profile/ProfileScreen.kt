package com.example.ejemplo_level_up.ui.profile

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.ejemplo_level_up.R
import com.example.ejemplo_level_up.data.UserManager
import com.example.ejemplo_level_up.ui.components.MainTopBar
import com.example.ejemplo_level_up.viewmodel.ProfileViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userData: UserProfile,
    onEditProfile: () -> Unit,
    onBack: () -> Unit,
    onOpenCart: () -> Unit,
    user: UserProfile?,
    isLoggedIn: Boolean,
    onLogout: () -> Unit,
    vm: ProfileViewModel = viewModel()
) {
    val context = LocalContext.current
    val userManager = remember { UserManager(context) }

    //  Estados de imagen conectados al ViewModel
    var imageBitmap by remember { mutableStateOf(vm.imageBitmap) }
    var imageUri by remember { mutableStateOf(vm.imageUri) }

    //  Estado del diálogo de selección
    var showImageDialog by remember { mutableStateOf(false) }

    //  Launchers
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            imageBitmap = it
            imageUri = null
            vm.imageBitmap = it
            vm.imageUri = null
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            imageUri = it
            imageBitmap = null
            vm.imageUri = it
            vm.imageBitmap = null
        }
    }

    //  Estados del código promocional
    var showPromoDialog by remember { mutableStateOf(false) }
    var promoCode by remember { mutableStateOf("") }
    var promoMessage by remember { mutableStateOf<String?>(null) }

    // Alerta DUOC UC
    val isDuocUser = remember(userData.email) {
        userData.email.endsWith("@duocuc.cl", ignoreCase = true)
    }
    var showAlert by remember { mutableStateOf(isDuocUser) }

    LaunchedEffect(isDuocUser) {
        if (isDuocUser) {
            showAlert = true
            delay(6000)
            showAlert = false
        }
    }

    //  Diálogo de confirmación de cierre de sesión
    var showLogoutDialog by remember { mutableStateOf(false) }

    //  Interfaz principal
    AnimatedVisibility(visible = isLoggedIn) {
        Scaffold(
            containerColor = Color(0xFF0A0A0A),
            topBar = {
                Column {
                    MainTopBar(
                        user = user,
                        isLoggedIn = isLoggedIn,
                        onLogout = { showLogoutDialog = true },
                        onCartClick = onOpenCart
                    )

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
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(16.dp))

                // 🧍 Imagen cuadrada de perfil
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1A1A1A)),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        imageBitmap != null -> Image(
                            bitmap = imageBitmap!!.asImageBitmap(),
                            contentDescription = "Foto de perfil",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        imageUri != null -> Image(
                            painter = rememberAsyncImagePainter(imageUri),
                            contentDescription = "Foto de perfil",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        else -> Icon(
                            painter = painterResource(id = R.drawable.ic_person),
                            contentDescription = "Sin imagen",
                            tint = Color.Gray,
                            modifier = Modifier.size(80.dp)
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                //  Botones de imagen
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = { showImageDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C8FF))
                    ) {
                        Text("Cambiar imagen", color = Color.Black)
                    }

                    OutlinedButton(
                        onClick = {
                            imageBitmap = null
                            imageUri = null
                            vm.imageBitmap = null
                            vm.imageUri = null
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00C8FF))
                    ) {
                        Text("Eliminar imagen")
                    }
                }

                Spacer(Modifier.height(20.dp))

                // 🎓 Alerta DUOC UC
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

                //  Datos del perfil
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFF1A1A1A)),
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
                    onClick = { showPromoDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00C8FF))
                ) {
                    Text("Ingresar código promocional")
                }

                promoMessage?.let { msg ->
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = msg,
                        color = if (msg.contains("correcto")) Color(0xFF4CAF50) else Color(0xFFFF4444),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }

    //  Diálogo de selección de imagen
    if (showImageDialog) {
        AlertDialog(
            onDismissRequest = { showImageDialog = false },
            title = { Text("Seleccionar imagen", color = Color(0xFF00C8FF)) },
            text = { Text("Elige una opción para tu foto de perfil", color = Color(0xFFB0B0B0)) },
            confirmButton = {
                Column {
                    TextButton(onClick = {
                        cameraLauncher.launch(null)
                        showImageDialog = false
                    }) { Text("Desde cámara", color = Color(0xFF00C8FF)) }

                    TextButton(onClick = {
                        galleryLauncher.launch("image/*")
                        showImageDialog = false
                    }) { Text("Desde mi almacenamiento", color = Color(0xFF00C8FF)) }
                }
            },
            dismissButton = {
                TextButton(onClick = { showImageDialog = false }) {
                    Text("Cancelar", color = Color(0xFFB0B0B0))
                }
            },
            containerColor = Color(0xFF1A1A1A)
        )
    }

    //  Diálogo promocional
    if (showPromoDialog) {
        AlertDialog(
            onDismissRequest = { showPromoDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (promoCode.trim().equals("duocuc2025", ignoreCase = true)) {
                            promoMessage = " Código correcto. ¡Has ganado un 30% de descuento en tu próxima compra!"
                        } else {
                            promoMessage = " Código inválido. Intenta nuevamente."
                        }
                        showPromoDialog = false
                    }
                ) {
                    Text("Validar", color = Color(0xFF00C8FF))
                }
            },
            dismissButton = {
                TextButton(onClick = { showPromoDialog = false }) {
                    Text("Cancelar", color = Color(0xFFB0B0B0))
                }
            },
            title = { Text("Ingresar código promocional", color = Color(0xFF00C8FF)) },
            text = {
                OutlinedTextField(
                    value = promoCode,
                    onValueChange = { promoCode = it },
                    label = { Text("Código de promoción") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00C8FF),
                        focusedLabelColor = Color(0xFF00C8FF),
                        cursorColor = Color(0xFF00C8FF)
                    )
                )
            },
            containerColor = Color(0xFF1A1A1A)
        )
    }

    //  Diálogo de cierre de sesión
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        userManager.clearAllUsers() //  Borra los datos almacenados
                        showLogoutDialog = false
                        onLogout()
                    }
                ) { Text("Cerrar sesión", color = Color(0xFFFF4444)) }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar", color = Color(0xFF00C8FF))
                }
            },
            title = { Text("Cerrar sesión", color = Color(0xFF00C8FF)) },
            text = { Text("¿Estás seguro de que deseas cerrar sesión?", color = Color(0xFFB0B0B0)) },
            containerColor = Color(0xFF1A1A1A)
        )
    }
}

// 🔹 Campos de perfil
@Composable
private fun ProfileField(label: String, value: String) {
    Column {
        Text(label, fontWeight = FontWeight.Bold, color = Color(0xFF00C8FF))
        Text(if (value.isBlank()) "(No especificado)" else value, color = Color(0xFFB0B0B0))
        Divider(Modifier.padding(vertical = 6.dp))
    }
}
