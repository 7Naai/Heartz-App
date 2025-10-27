package com.example.heartzapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.heartzapp.viewmodel.ViniloViewModel

@Composable
fun PantallaBoleta(
    navController: NavController,
    viewModel: ViniloViewModel
) {
    val carritoItems by viewModel.carritoItems.collectAsState()
    val carritoTotal by viewModel.carritoTotal.collectAsState()

    // Número de boleta fijo
    val numeroBoleta = "#4AG4GRCC"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp)
                .border(1.dp, Color.LightGray),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "HEARTZ - Boleta de Compra",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontFamily = FontFamily.Monospace
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = numeroBoleta,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.DarkGray,
                        fontFamily = FontFamily.Monospace
                    )
                )

                Divider(modifier = Modifier.padding(vertical = 12.dp))

                // Lista de productos
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp)
                ) {
                    items(carritoItems) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = item.nombre,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = "x${item.cantidad}",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                                )
                            }
                            Text(
                                text = "$${"%,d".format(item.precio * item.cantidad)}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 12.dp))

                // Total
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "TOTAL",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "$${"%,d".format(carritoTotal)}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Gracias por tu compra ❤️",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.Gray,
                        fontFamily = FontFamily.Monospace
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.vaciarCarrito()
                        navController.navigate("inicio") {
                            popUpTo("inicio") { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8E24AA))
                ) {
                    Text(
                        text = "Volver al Inicio",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}
