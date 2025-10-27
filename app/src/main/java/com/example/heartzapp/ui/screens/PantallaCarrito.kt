package com.example.heartzapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
// Eliminamos la importación de Routes si no la estás usando. Si la necesitas para otros
// propósitos, mantenla, pero aquí la quitamos para limpiar.
// import com.example.heartzapp.navigation.Routes
import com.example.heartzapp.ui.components.BottomBar
import com.example.heartzapp.ui.components.ItemCarritoCard
import com.example.heartzapp.viewmodel.ViniloViewModel


@Composable
fun PantallaCarrito(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel: ViniloViewModel = viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(
            context.applicationContext as android.app.Application
        )
    )

    val carritoItems by viewModel.carritoItems.collectAsState()
    val carritoTotal by viewModel.carritoTotal.collectAsState()

    val headerColor = Color(0xFF2A004E)

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(headerColor)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Mi Carrito",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        },
        bottomBar = {
            BottomBar(navController)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF7E57C2),
                            Color(0xFFF3E5F5)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (carritoItems.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "El carrito de compras está vacío.",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(carritoItems) { item ->
                            ItemCarritoCard(
                                item = item,
                                onIncrement = { viewModel.incrementarItem(it) },
                                onDecrement = { viewModel.decrementarItem(it) }
                            )
                        }
                    }

                    // Resumen del Carrito y Botón de Pagar
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF4A148C)
                        ),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Total a Pagar:",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = "$${"%,d".format(carritoTotal)}",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        color = Color(0xFFB388FF),
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = {
                                    // *** REDIRECCIÓN A LA PANTALLA DE PAGO, USANDO LA RUTA LITERAL "pago" ***
                                    navController.navigate("pago")
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF9C27B0)
                                )
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
