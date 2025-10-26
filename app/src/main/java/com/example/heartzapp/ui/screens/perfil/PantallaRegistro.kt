package com.example.heartzapp.ui.screens.perfil

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.heartzapp.R
import com.example.heartzapp.viewmodel.UsuarioViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import com.example.heartzapp.ui.components.BotonVolver

@Composable
fun PantallaRegistro(navController: NavController, viewModel: UsuarioViewModel) {
    val estado by viewModel.estado.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    // Animación de waves
    val infiniteTransition = rememberInfiniteTransition()
    val waveShift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing),
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

            // 1 ola
            val path = Path().apply {
                moveTo(0f, height * 0.8f)
                val waveHeight = 50f
                val waveLength = width / 2.5f
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
            drawPath(path, color = Color(0xFFD5B6F2))

            // 2 ola
            val path2 = Path().apply {
                moveTo(0f, height * 0.85f)
                val waveHeight = 35f
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
            drawPath(path2, color = Color(0xFFFFFFFF))
        }

        BotonVolver(
            navController = navController,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp) // separación del borde
        )

        // formulario
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            Text(
                text = "Regístrate",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF3B006A)
            )
            Text(
                text = "Crea tu cuenta para continuar",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF3B006A)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Rut
            OutlinedTextField(
                value = estado.rut,
                onValueChange = viewModel::onRutChange,
                label = { Text("Rut") },
                singleLine = true,
                isError = estado.errores.rut != null,
                supportingText = {
                    estado.errores.rut?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Nombre
            OutlinedTextField(
                value = estado.nombre,
                onValueChange = viewModel::onNombreChange,
                label = { Text("Nombre") },
                singleLine = true,
                isError = estado.errores.nombre != null,
                supportingText = {
                    estado.errores.nombre?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Correo
            OutlinedTextField(
                value = estado.correo,
                onValueChange = viewModel::onCorreoChange,
                label = { Text("Correo electrónico") },
                singleLine = true,
                isError = estado.errores.correo != null,
                supportingText = {
                    estado.errores.correo?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Contraseña
            var passwordVisible by remember { mutableStateOf(false) }
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
                trailingIcon = {
                    val icon = if (passwordVisible)
                        R.drawable.visibility_off_icon_login_opsz24
                    else
                        R.drawable.visibility_icon_login_opsz24
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Image(
                            painter = painterResource(id = icon),
                            contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Checkbox: aceptar términos
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = estado.aceptaTerminos,
                    onCheckedChange = viewModel::onAceptarTerminosChange
                )
                Spacer(Modifier.width(8.dp))
                Text(text = "Acepto los términos y condiciones")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Registrar
            Button(
                onClick = {
                    if (viewModel.validarFormulario()) {
                        showDialog = true // Mostrar popup
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Registrar")
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Registro exitoso") },
                    text = { Text("Tu cuenta ha sido creada correctamente.") },
                    confirmButton = {
                        Button(
                            onClick = {
                                showDialog = false
                                navController.navigate("inicio") {
                                    popUpTo("login") { inclusive = true } // Evita volver al login/registro
                                }
                            }
                        ) {
                            Text("Aceptar")
                        }
                    }
                )
            }
        }
    }
}
