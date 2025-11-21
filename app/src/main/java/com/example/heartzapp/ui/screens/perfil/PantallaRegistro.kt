package com.example.heartzapp.ui.screens.perfil

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.heartzapp.viewmodel.UsuarioViewModel
import com.example.heartzapp.R
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.heartzapp.ui.components.BotonVolver
import kotlinx.coroutines.launch

@Composable
fun PantallaRegistro(navController: NavController, viewModel: UsuarioViewModel) {

    val scope = rememberCoroutineScope()

    val rut by viewModel.rut.collectAsState()
    val nombre by viewModel.nombre.collectAsState()
    val correo by viewModel.correo.collectAsState()
    val contrasena by viewModel.contrasena.collectAsState()
    val aceptaTerminos by viewModel.aceptaTerminos.collectAsState()

    val errorMensaje by viewModel.errorMensaje.collectAsState()

    var passwordVisible by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    // … (olas decorativas se mantienen igual)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        BotonVolver(
            navController = navController,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        )

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
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = rut,
                onValueChange = viewModel::onRutChange,
                label = { Text("RUT") },
                singleLine = true,
                isError = !errorMensaje.isNullOrEmpty(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = viewModel::onNombreChange,
                label = { Text("Nombre") },
                singleLine = true,
                isError = !errorMensaje.isNullOrEmpty(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = correo,
                onValueChange = viewModel::onCorreoChange,
                label = { Text("Correo electrónico") },
                singleLine = true,
                isError = !errorMensaje.isNullOrEmpty(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = contrasena,
                onValueChange = viewModel::onContrasenaChange,
                label = { Text("Contraseña") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                visualTransformation =
                    if (passwordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (passwordVisible)
                        R.drawable.visibility_off_icon_login_opsz24
                    else
                        R.drawable.visibility_icon_login_opsz24

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Image(
                            painter = painterResource(id = icon),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                isError = !errorMensaje.isNullOrEmpty(),
                modifier = Modifier.fillMaxWidth()
            )

            if (!errorMensaje.isNullOrEmpty()) {
                Text(
                    text = errorMensaje ?: "",
                    color = Color.Red,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = aceptaTerminos,
                    onCheckedChange = viewModel::onAceptarTerminosChange
                )
                Spacer(Modifier.width(8.dp))
                Text(text = "Acepto los términos y condiciones")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (viewModel.validarRegistro()) {
                        scope.launch {
                            val ok = viewModel.registrar()
                            if (ok) showDialog = true
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Registrar")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "¿Ya tienes cuenta? ¡Inicia Sesión!",
                color = Color(0xFF3B006A),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    navController.navigate("login")
                }
            )

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
                                    popUpTo("login") { inclusive = true }
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
