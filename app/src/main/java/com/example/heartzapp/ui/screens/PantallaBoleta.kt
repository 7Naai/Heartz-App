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
import com.example.heartzapp.viewmodel.CarritoViewModel

@Composable
fun PantallaBoleta(
    navController: NavController,
    carritoVM: CarritoViewModel
) {
    val carritoItems by carritoVM.items.collectAsState()
    val carritoTotal by carritoVM.total.collectAsState()

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
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {

            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    "HEARTZ - Boleta de Compra",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                )

                Text(
                    numeroBoleta,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.DarkGray,
                        fontFamily = FontFamily.Monospace
                    )
                )

                Divider(Modifier.padding(vertical = 12.dp))

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
                                    item.nombre,
                                    fontWeight = FontWeight.Bold
                                )
                                Text("x${item.cantidad}", color = Color.Gray)
                            }
                            Text(
                                "$${"%,d".format(item.precio * item.cantidad)}"
                            )
                        }
                    }
                }

                Divider(Modifier.padding(vertical = 12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("TOTAL", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(
                        "$${"%,d".format(carritoTotal)}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp
                    )
                }

                Spacer(Modifier.height(20.dp))

                Text(
                    "Gracias por tu compra ❤️",
                    color = Color.Gray,
                    fontFamily = FontFamily.Monospace
                )

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = {
                        carritoVM.vaciar()
                        navController.navigate("inicio") {
                            popUpTo("inicio") { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF8E24AA))
                ) {
                    Text(
                        "Volver al Inicio",
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
