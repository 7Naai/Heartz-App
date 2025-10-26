package com.example.heartzapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.heartzapp.data.model.Vinilo
import com.example.heartzapp.ui.components.BottomBar
import com.example.heartzapp.ui.components.CarruselImagenes
import com.example.heartzapp.ui.components.TarjetaVinilo
import com.example.heartzapp.viewmodel.ViniloViewModel

// Color para el encabezado, tomado del diseño del carrito
val HeaderColor = Color(0xFF2A004E)

@Composable
fun PantallaInicio(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel: ViniloViewModel = viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(
            context.applicationContext as android.app.Application
        )
    )
    val vinilos by viewModel.vinilos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val productosDestacados = vinilos.take(4)

    val proximosLanzamientos = listOf(
        Pair("Próximo lanzamiento 1", "Próximamente"),
        Pair("Próximo lanzamiento 2", "Próximamente")
    )

    // 1. Implementamos Scaffold para manejar las barras de forma segura
    Scaffold(
        // 2. TopBar para asegurar que el color oscuro suba a la barra de estado
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(HeaderColor)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Inicio - HeartZApp",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        },
        // 3. BottomBar para el menú de navegación inferior
        bottomBar = {
            BottomBar(navController)
        }
    ) { paddingValues -> // 4. El Scaffold nos da el padding seguro
        // El contenido principal se coloca en un Box que utiliza el padding seguro del Scaffold
        Box(
            modifier = Modifier
                .fillMaxSize()
                // APLICAMOS EL PADDING DEL SCAFFOLD AQUÍ
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF7E57C2), // Morado oscuro superior
                            Color(0xFFF3E5F5)  // Lila claro inferior
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)
            ) {
                CarruselImagenes()

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Productos destacados",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color.White
                    ),
                    modifier = Modifier.padding(start = 4.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(300.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                } else if (productosDestacados.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(300.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay vinilos destacados disponibles",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 300.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(productosDestacados) { vinilo ->
                            TarjetaVinilo(
                                vinilo = vinilo,
                                onVerDetalle = { viniloSeleccionado ->
                                    navController.navigate("detalle/${viniloSeleccionado.idVin}")
                                },
                                onAgregarCarrito = { viniloSeleccionado ->
                                    println("Añadir al carrito desde Inicio: ${viniloSeleccionado.nombre}")
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Próximas novedades",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color.White
                    ),
                    modifier = Modifier.padding(start = 4.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        // Quitamos el height fijo para que el diseño sea más flexible,
                        // o lo envolvemos en un peso si es necesario
                        .height(180.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(proximosLanzamientos) { producto ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF2A004E))
                                .padding(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF6A1B9A)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("💿", fontSize = MaterialTheme.typography.titleLarge.fontSize)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = producto.first, // Nombre
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = producto.second, // Próximamente
                                color = Color(0xFF9C27B0),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Boton de catalogo
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(onClick = { navController.navigate("productos") }) {
                        Text(text = "Consultar catálogo completo")
                    }
                }

                // Eliminamos el Spacer(modifier = Modifier.height(16.dp)) final
                // para que el contenido no intente empujar más allá del BottomBar
            }
            // 5. ELIMINAMOS EL BOX DEL BOTTOMBAR MANUAL
        }
    }
}
