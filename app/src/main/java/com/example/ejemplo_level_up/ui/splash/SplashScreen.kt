package com.example.ejemplo_level_up.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import com.example.ejemplo_level_up.R
import com.example.ejemplo_level_up.navigation.Routes
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    var startAnimation by remember { mutableStateOf(false) }

    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1800),
        label = "alphaAnim"
    )

    // ⏳ Animación + navegación
    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2500)
        navController.navigate(Routes.HOME) {
            popUpTo(Routes.SPLASH) { inclusive = true }
            launchSingleTop = true
        }
    }

    // 🎨 Fondo y contenido
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A)), // fondo oscuro para contraste
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // ✅ Logo con transparencia (mantiene canal alfa del PNG)
            Image(
                painter = painterResource(id = R.drawable.splash_screen),
                contentDescription = "Logo Level-Up Gamer",
                modifier = Modifier
                    .size(220.dp)
                    .alpha(alphaAnim.value),
                contentScale = ContentScale.Fit // evita fondo blanco o estiramiento
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Texto principal
            Text(
                text = "POWER TO THE PLAYERS!",
                color = Color(0xFF00C8FF),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(alphaAnim.value)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Subtítulo
            Text(
                text = "Cargando experiencia...",
                color = Color(0xFFB0B0B0),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(alphaAnim.value)
            )
        }
    }
}

