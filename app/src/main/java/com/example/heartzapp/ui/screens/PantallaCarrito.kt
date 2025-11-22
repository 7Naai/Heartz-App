package com.example.heartzapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.heartzapp.ui.components.BottomBar
import com.example.heartzapp.ui.components.ItemCarritoCard
import com.example.heartzapp.viewmodel.CarritoViewModel

@Composable
fun PantallaCarrito(navController: NavHostController, carritoVM: CarritoViewModel) {

    val carritoItems by carritoVM.items.collectAsState()
    val carritoTotal by carritoVM.total.collectAsState()

    Scaffold(
        topBar = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2A004E))
                    .padding(16.dp)
            ) {
                Text(
                    "Mi Carrito",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        },
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->

        Box(
            Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF7E57C2),
                            Color(0xFFF3E5F5)
                        )
                    )
                )
        ) {

            Column(Modifier.fillMaxSize()) {

                if (carritoItems.isEmpty()) {
                    Box(
                        Modifier.fillMaxSize(),
                        Alignment.Center
                    ) {
                        Text(
                            "El carrito está vacío",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                } else {

                    LazyColumn(
                        Modifier
                            .weight(1f)
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(carritoItems) { item ->
                            ItemCarritoCard(
                                item = item,
                                onIncrement = { carritoVM.incrementar(it) },
                                onDecrement = { carritoVM.decrementar(it) }
                            )
                        }
                    }

                    Card(
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = CardDefaults.cardColors(Color(0xFF4A148C))
                    ) {
                        Column(Modifier.padding(16.dp)) {

                            Row(
                                Modifier.fillMaxWidth(),
                                Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Total a Pagar:",
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Text(
                                    "$${"%,d".format(carritoTotal)}",
                                    color = Color(0xFFB388FF),
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }

                            Spacer(Modifier.height(12.dp))

                            Button(
                                onClick = { navController.navigate("pago") },
                                Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                colors = ButtonDefaults.buttonColors(Color(0xFF9C27B0))
                            ) {
                                Text("Pagar Ahora")
                            }
                        }
                    }
                }
            }
        }
    }
}
