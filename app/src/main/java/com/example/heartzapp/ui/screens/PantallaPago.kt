package com.example.heartzapp.ui.screens

import android.app.Application
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
import androidx.navigation.NavHostController
import com.example.heartzapp.viewmodel.CarritoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPago(
    navController: NavHostController,
    carritoVM: CarritoViewModel
) {
    val carritoTotal by carritoVM.total.collectAsState()

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
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF4A148C)),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Text(
                        text = "Resumen del Pedido",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total a Pagar:", color = Color.White)
                        Text(
                            "$${"%,d".format(carritoTotal)}",
                            color = Color(0xFFB388FF),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        "Información del Cliente y Envío",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = nombreUsuario,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Nombre Completo") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = correoUsuario,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Correo Electrónico") },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )

                    OutlinedTextField(
                        value = rutUsuario,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("RUT / Identificación") },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )

                    OutlinedTextField(
                        value = direccion,
                        onValueChange = { direccion = it },
                        label = { Text("Dirección") },
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                    )

                    OutlinedTextField(
                        value = comuna,
                        onValueChange = { comuna = it },
                        label = { Text("Comuna") },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    ) {
                        OutlinedTextField(
                            readOnly = true,
                            value = selectedRegion,
                            onValueChange = {},
                            label = { Text("Región") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
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

                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = {
                            navController.navigate("boleta") {
                                popUpTo("pago") { inclusive = true }
                            }
                        },
                        enabled = carritoTotal > 0 &&
                                direccion.text.isNotEmpty() &&
                                comuna.text.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))
                    ) {
                        Text("Confirmar y Pagar $${"%,d".format(carritoTotal)}")
                    }
                }
            }
        }
    }
}
