package com.example.heartzapp.ui.screens.perfil

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.heartzapp.ui.components.BottomBar

@Composable
fun PantallaPerfil(navController: NavHostController) {

    val infiniteTransition = rememberInfiniteTransition()  //

    val offset = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 8000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    // Gradiente animado en diagonal
    val animatedBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF6A1B9A),
            Color(0xFF9C27B0),
            Color(0xFFF3E5F5)
        ),
        start = androidx.compose.ui.geometry.Offset(offset.value, 0f),
        end = androidx.compose.ui.geometry.Offset(0f, offset.value)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = animatedBrush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .padding(bottom = 80.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bienvenido a HeartzApp 💜",
                color = Color.Gray,
                fontSize = 26.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Text(
                text = "¿No tienes una cuenta?",
                color = Color(0xFF6A1B9A),
                fontSize = 18.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Button(
                onClick = { navController.navigate("registro") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF6A1B9A)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Regístrate", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "¿Ya tienes una cuenta?",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Button(
                onClick = { navController.navigate("login") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8E24AA),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Inicia sesión", fontSize = 18.sp)
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            BottomBar(navController)
        }
    }
}
