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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.heartzapp.data.model.Vinilo // Importar el modelo Vinilo
import com.example.heartzapp.ui.components.BottomBar
import com.example.heartzapp.ui.components.CarruselImagenes
import com.example.heartzapp.ui.components.TarjetaVinilo // Importar el componente TarjetaVinilo
import com.example.heartzapp.viewmodel.ViniloViewModel // Importar el ViewModel

@Composable
fun PantallaInicio(navController: NavHostController) {
    val context = LocalContext.current
    // Inicialización del ViewModel (ya está correcto)
    val viewModel: ViniloViewModel = viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(
            context.applicationContext as android.app.Application
        )
    )
    val vinilos by viewModel.vinilos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Tomamos los primeros 4 vinilos como "destacados" si existen
    val productosDestacados = vinilos.take(4)

    // Se mantiene una lista simple para los próximos lanzamientos
    val proximosLanzamientos = listOf(
        Pair("Próximo lanzamiento 1", "Próximamente"),
        Pair("Próximo lanzamiento 2", "Próximamente")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF7E57C2),
                        Color(0xFFF3E5F5)
                    )
                )
            )
            .padding(bottom = 56.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
        ) {
            // Carrusel
            CarruselImagenes()

            Spacer(modifier = Modifier.height(16.dp))

            // Titulo "productos destacados"
            Text(
                text = "Productos destacados",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.White
                ),
                modifier = Modifier.padding(start = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // SECCIÓN DE PRODUCTOS DESTACADOS
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
                        .heightIn(min = 300.dp), // Ajuste la altura para el contenido
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(productosDestacados) { vinilo ->
                        // USANDO TarjetaVinilo con el objeto Vinilo real
                        TarjetaVinilo(
                            vinilo = vinilo,
                            onVerDetalle = { viniloSeleccionado ->
                                // Ejemplo de navegación: debes tener una ruta 'detalle/{id}'
                                navController.navigate("detalle/${viniloSeleccionado.idVin}")
                            },
                            onAgregarCarrito = { viniloSeleccionado ->
                                println("Añadir al carrito desde Inicio: ${viniloSeleccionado.nombre}")
                                // Implementar lógica de agregar al carrito
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Seccion de Próximas novedades (mantenida como placeholder simple)
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

            Spacer(modifier = Modifier.height(16.dp))
        }
        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            BottomBar(navController)
        }
    }
}