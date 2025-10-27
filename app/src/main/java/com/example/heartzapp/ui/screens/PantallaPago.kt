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
import com.example.heartzapp.navigation.Pantallas // Asumo que usas Pantallas para las rutas principales
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

    // MOCK USER DATA (Asumiendo que estos datos vienen de un ViewModel de Usuario)
    val nombreUsuario = "Luis Fernández"
    val correoUsuario = "cliente@heartz.cl"
    val rutUsuario = "18.345.678-9"

    // ESTADOS PARA DATOS DE ENVÍO
    var direccion by remember { mutableStateOf(TextFieldValue("")) }
    var comuna by remember { mutableStateOf(TextFieldValue("")) }

    // ESTADOS PARA DROPDOWN DE REGIÓN (MOCK de Regiones de Chile)
    val regiones = listOf("Región Metropolitana", "Valparaíso", "Biobío", "Antofagasta", "O'Higgins", "Maule")
    var expanded by remember { mutableStateOf(false) }
    var selectedRegion by remember { mutableStateOf(regiones[0]) } // Pre-seleccionar la primera

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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = headerColor
                )
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
                .background(Color(0xFFF3E5F5)) // Fondo claro
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // CARD: Resumen del Pedido
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
                    Divider(color = Color(0xFFB388FF), thickness = 1.dp)
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

            Spacer(modifier = Modifier.height(8.dp))

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

                    // === DATOS PRE-RELLENADOS (SOLO LECTURA) ===
                    Text("Datos de Contacto",
                        style = MaterialTheme.typography.titleSmall.copy(color = Color.Gray),
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp))

                    // Campo: Nombre (Read-only)
                    OutlinedTextField(
                        value = nombreUsuario,
                        onValueChange = {},
                        label = { Text("Nombre Completo") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )

                    // Campo: Correo (Read-only)
                    OutlinedTextField(
                        value = correoUsuario,
                        onValueChange = {},
                        label = { Text("Correo Electrónico") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )

                    // Campo: RUT (Read-only)
                    OutlinedTextField(
                        value = rutUsuario,
                        onValueChange = {},
                        label = { Text("RUT/Identificación") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                    )

                    Divider(color = Color.LightGray.copy(alpha = 0.5f))

                    // === DATOS DE ENVÍO REQUERIDOS ===
                    Text("Dirección de Envío",
                        style = MaterialTheme.typography.titleSmall.copy(color = Color.DarkGray),
                        modifier = Modifier.padding(top = 16.dp, bottom = 4.dp))

                    // Campo: Dirección
                    OutlinedTextField(
                        value = direccion.text,
                        onValueChange = { direccion = TextFieldValue(it) },
                        label = { Text("Dirección (Calle y Número)") },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )

                    // Campo: Comuna
                    OutlinedTextField(
                        value = comuna.text,
                        onValueChange = { comuna = TextFieldValue(it) },
                        label = { Text("Comuna") },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )

                    // Dropdown: Región
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
                            regiones.forEach { selectionOption ->
                                DropdownMenuItem(
                                    text = { Text(selectionOption) },
                                    onClick = {
                                        selectedRegion = selectionOption
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón de Confirmación
                    Button(
                        onClick = {
                            // 1. Simular proceso de pago con los datos de envío
                            println("Confirmando envío a: ${direccion.text}, ${comuna.text}, $selectedRegion. Total: $carritoTotal")
                            viewModel.vaciarCarrito()

                            // 2. Navegar a la pantalla de inicio o a una pantalla de éxito
                            navController.navigate(Pantallas.Inicio.ruta) {
                                // Borra la pila de navegación hasta el inicio para evitar volver al carrito/pago
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                }
                            }
                        },
                        // Deshabilitar si falta algún campo de envío
                        enabled = carritoTotal > 0 &&
                                direccion.text.isNotEmpty() &&
                                comuna.text.isNotEmpty(), // Región siempre tiene un valor por defecto
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF9C27B0)
                        )
                    ) {
                        Text("Confirmar Datos y Pagar $${"%,d".format(carritoTotal)}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    }
                }
            }
        }
    }
}
