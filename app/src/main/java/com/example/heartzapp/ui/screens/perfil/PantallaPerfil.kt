package com.example.heartzapp.ui.screens.perfil

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.heartzapp.R // Importación necesaria para acceder a recursos locales
import com.example.heartzapp.ui.components.BottomBar

// Datos simulados del usuario logueado usando camelCase
private const val nombreUsuario = "Luis Fernández"
private const val correoUsuario = "cliente@heartz.cl"
private const val rolUsuario = "Cliente"

@Composable
fun PantallaPerfil(navController: NavHostController) {
    val infiniteTransition = rememberInfiniteTransition(label = "")

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
        label = "animatedOffset"
    )

    // 🎨 Gradiente animado en diagonal
    val animatedBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF6A1B9A), // Morado Oscuro
            Color(0xFF9C27B0), // Morado Medio
            Color(0xFFF3E5F5)  // Lila Claro
        ),
        start = androidx.compose.ui.geometry.Offset(offset.value, 0f),
        end = androidx.compose.ui.geometry.Offset(0f, offset.value)
    )

    // El Box principal define el ámbito de alineación para sus hijos
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = animatedBrush),
        contentAlignment = Alignment.TopCenter // Alineamos el contenido principal arriba
    ) {
        // Tarjeta principal del perfil (Centrada verticalmente en el espacio disponible)
        // Usamos un Box anidado para centrar solo la tarjeta
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp), // Dejamos espacio para el BottomBar
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(32.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Foto de Perfil: Usando painterResource para cargar el recurso local
                    Image(
                        painter = painterResource(id = R.drawable.icono_usuario),
                        contentDescription = "Foto de Perfil de $nombreUsuario",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(4.dp, Color(0xFF6A1B9A), CircleShape)
                            .background(Color.White)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "¡Bienvenido/a, $nombreUsuario!",
                        color = Color(0xFF6A1B9A),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    ProfileDetailRow(
                        label = "Correo Electrónico:",
                        value = correoUsuario
                    )
                    ProfileDetailRow(
                        label = "Rol:",
                        value = rolUsuario
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Botón para cerrar sesión
                    Button(
                        onClick = { navController.navigate("login") }, // NAVEGACIÓN A LOGIN
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE57373), // Rojo suave para 'Logout'
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(text = "Cerrar Sesión", fontSize = 18.sp)
                    }
                }
            }
        }

        // BottomBar: Usamos Modifier.align para fijarlo al fondo del Box principal
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            BottomBar(navController)
        }
    }
}

@Composable
fun ProfileDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            color = Color(0xFF6A1B9A),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
