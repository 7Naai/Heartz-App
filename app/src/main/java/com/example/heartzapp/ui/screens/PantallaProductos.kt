package com.example.heartzapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold // Importación de Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.heartzapp.ui.components.BottomBar
import com.example.heartzapp.ui.components.TarjetaVinilo
import com.example.heartzapp.viewmodel.ViniloViewModel

@Composable
fun PantallaProductos(navController: NavHostController) {
    // Nota: viewModel() con paréntesis vacíos usa el ViewModelProvider por defecto,
    // que es correcto si el ViniloViewModel no requiere argumentos.
    val viewModel: ViniloViewModel = viewModel()

    val vinilos by viewModel.vinilos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Color del encabezado utilizado anteriormente
    val headerColor = Color(0xFF2A004E)

    // Usamos Scaffold para estructurar la pantalla y manejar el padding de las barras
    Scaffold(
        topBar = {
            // Encabezado movido al topBar del Scaffold para cubrir la barra de estado
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(headerColor)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Catálogo de Vinilos",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        },
        bottomBar = {
            // BottomBar en el slot 'bottomBar'
            BottomBar(navController)
        }
    ) { paddingValues -> // paddingValues contiene el relleno seguro
        Box(
            modifier = Modifier
                .fillMaxSize()
                // Aplicamos el padding proporcionado por el Scaffold
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
                // ELIMINADO: .padding(bottom = 56.dp)
            ) {
                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                } else if (vinilos.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay vinilos disponibles",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        // Usamos fillMaxSize() y weight(1f) si fuera necesario,
                        // pero aquí solo fillMaxSize() funciona bien dentro del Column sin scroll
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(vinilos) { vinilo ->
                            TarjetaVinilo(
                                vinilo = vinilo,
                                onVerDetalle = { viniloSeleccionado ->
                                    navController.navigate("details/${viniloSeleccionado.idVin}")
                                },
                                onAgregarCarrito = { viniloSeleccionado ->
                                    viewModel.agregarViniloACarrito(viniloSeleccionado)
                                }
                            )
                        }
                    }
                }
            }

            // ELIMINADO: El Box que alineaba BottomBar
        }
    }
}
