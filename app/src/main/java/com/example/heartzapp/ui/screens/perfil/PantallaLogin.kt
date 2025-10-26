package com.example.heartzapp.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.heartzapp.ui.components.BotonVolver
import com.example.heartzapp.viewmodel.UsuarioViewModel

@Composable
fun PantallaLogin(
    navController: NavHostController,
    viewModel: UsuarioViewModel,
    onForgotPassword: () -> Unit = {},
    onRegister: () -> Unit = {}
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val estado by viewModel.estado.collectAsState()

    // Animación de waves
    val infiniteTransition = rememberInfiniteTransition()
    val waveShift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Canvas para las waves
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            val path = Path().apply {
                moveTo(0f, height * 0.8f)
                val waveHeight = 60f
                val waveLength = width / 2f
                var x = 0f
                while (x <= width) {
                    val y = waveHeight * kotlin.math.sin((x / waveLength + waveShift * 2 * Math.PI).toFloat()) + height * 0.8f
                    lineTo(x, y)
                    x += 1f
                }
                lineTo(width, height)
                lineTo(0f, height)
                close()
            }

            drawPath(
                path = path,
                color = Color(0xFFD5B6F2)  // Primera ola
            )

            val path2 = Path().apply {
                moveTo(0f, height * 0.85f)
                val waveHeight = 40f
                val waveLength = width / 1.5f
                var x = 0f
                while (x <= width) {
                    val y = waveHeight * kotlin.math.sin((x / waveLength + waveShift * 2 * Math.PI).toFloat()) + height * 0.85f
                    lineTo(x, y)
                    x += 1f
                }
                lineTo(width, height)
                lineTo(0f, height)
                close()
            }

            drawPath(
                path = path2,
                color = Color(0xFFFFFFFF) // Segunda ola, más clara
            )
        }

        BotonVolver(
            navController = navController,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp) // separación del borde
        )

        // Contenido de login
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {

            Text(
                text = "Bienvenido",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF3B006A)
            )
            Text(
                text = "Inicia sesión con tu cuenta!",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF3B006A)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Correo
            OutlinedTextField(
                value = estado.correo,
                onValueChange = viewModel::onCorreoChange,
                label = { Text("Correo") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                isError = estado.errores.correo != null,
                supportingText = {
                    estado.errores.correo?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Contraseña
            OutlinedTextField(
                value = estado.contrasena,
                onValueChange = viewModel::onContrasenaChange,
                label = { Text("Contraseña") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError = estado.errores.contrasena != null,
                supportingText = {
                    estado.errores.contrasena?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "¿Olvidaste tu contraseña?",
                    color = Color(0xFF3B006A),
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable { onForgotPassword() }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (estado.correo == "admin@heartz.cl" && estado.contrasena == "123456") {
                        navController.navigate("admin") {
                            popUpTo("login") { inclusive = true } // Para que no vuelva a login con back
                        }
                    } else if (viewModel.validarLogin()) {
                        // Redirigir a cliente o pantalla normal
                        navController.navigate("inicio") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Ingresar")
            }


            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "¿No tienes cuenta? ¡Regístrate!",
                color = Color(0xFF3B006A),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { onRegister() }
            )
        }
    }
}
