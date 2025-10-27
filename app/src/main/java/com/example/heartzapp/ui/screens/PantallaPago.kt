package com.example.heartzapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.heartzapp.ui.components.BottomBar
import com.example.heartzapp.viewmodel.ViniloViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPago(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel: ViniloViewModel = viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(
            context.applicationContext as android.app.Application
        )
    )
    val carritoTotal by viewModel.carritoTotal.collectAsState()

    val nombreUsuario = "Luis Fernández"
    val correoUsuario = "cliente@heartz.cl"
    val rutUsuario = "18.345.678-9"

    var direccion by remember { mutableStateOf(TextFieldValue("")) }
    var comuna by remember { mutableStateOf(TextFieldValue("")) }

    val regiones = listOf("Región Metropolitana", "Valparaíso", "Biobío", "Antofagasta", "O'Higgins", "Maule")
    var expanded by remember { mutableStateOf(false) }
    var selectedRegion by remember { mutableStateOf(regiones[0]) }

    val headerColor = Color(0xFF2A004E)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Datos de Envío y Pago", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = headerColor)
            )
        },
        bottomBar = {
            BottomBar(navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF3E5F5))
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF4A148C)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Resumen del Pedido",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total a Pagar:",
                            style = MaterialTheme.typography.headlineSmall.copy(color = Color.White)
                        )
                        Text(
                            text = "$${"%,d".format(carritoTotal)}",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                color = Color(0xFFB388FF),
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                    }
                }
            }

            // CARD: Formulario de Datos del Cliente y Envío
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Información del Cliente y Envío",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // === DATOS PRE-RELLENADOS ===
                    OutlinedTextField(
                        value = nombreUsuario,
                        onValueChange = {},
                        label = { Text("Nombre Completo") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = correoUsuario,
                        onValueChange = {},
                        label = { Text("Correo Electrónico") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = rutUsuario,
                        onValueChange = {},
                        label = { Text("RUT/Identificación") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                    )


                    OutlinedTextField(
                        value = direccion.text,
                        onValueChange = { direccion = TextFieldValue(it) },
                        label = { Text("Dirección (Calle y Número)") },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = comuna.text,
                        onValueChange = { comuna = TextFieldValue(it) },
                        label = { Text("Comuna") },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    ) {
                        OutlinedTextField(
                            readOnly = true,
                            value = selectedRegion,
                            onValueChange = { },
                            label = { Text("Región") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            regiones.forEach { region ->
                                DropdownMenuItem(
                                    text = { Text(region) },
                                    onClick = {
                                        selectedRegion = region
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            println("Pago realizado. Enviando a boleta...")
                            navController.navigate("boleta") {
                                popUpTo("pago") { inclusive = true } // Borra la pantalla de pago del stack
                            }
                        },
                        enabled = carritoTotal > 0 &&
                                direccion.text.isNotEmpty() &&
                                comuna.text.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))
                    ) {
                        Text(
                            "Confirmar Datos y Pagar $${"%,d".format(carritoTotal)}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}
