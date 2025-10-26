package com.example.heartzapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.heartzapp.ui.components.BottomBar
import com.example.heartzapp.ui.components.CarruselImagenes

data class ProductoPlaceholder(
    val nombre: String,
    val precio: String
)

@Composable
fun PantallaInicio(navController: NavHostController) {  // ← Añadido parámetro
    val productosDestacados = listOf(
        ProductoPlaceholder("Álbum destacado 1", "$19.990"),
        ProductoPlaceholder("Álbum destacado 2", "$21.490"),
        ProductoPlaceholder("Álbum destacado 3", "$24.990"),
        ProductoPlaceholder("Álbum destacado 4", "$22.990"),
    )

    val proximosLanzamientos = listOf(
        ProductoPlaceholder("Próximo lanzamiento 1", "Próximamente"),
        ProductoPlaceholder("Próximo lanzamiento 2", "Próximamente")
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

            // Seccion de productos destacados
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(productosDestacados) { producto ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF2A004E))
                            .padding(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF4A148C)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🎵", fontSize = MaterialTheme.typography.headlineMedium.fontSize)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = producto.nombre,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = producto.precio,
                            color = Color(0xFFB388FF),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Seccion de Próximas novedades
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
                            text = producto.nombre,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = producto.precio,
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
                Button(onClick = { navController.navigate("productos") }) {  // ← Ahora navega correctamente
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