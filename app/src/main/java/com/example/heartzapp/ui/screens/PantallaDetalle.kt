package com.example.heartzapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.heartzapp.data.model.Vinilo
import com.example.heartzapp.viewmodel.ViniloViewModel
import coil.compose.AsyncImage // <-- Componente Coil para cargar imágenes

private val AccentColor = Color(0xFF9C27B0)
private val SecondaryColor = Color(0xFF4A148C)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalle(navController: NavHostController, viniloId: String?) {
    val viewModel: ViniloViewModel = viewModel()

    val vinilos by viewModel.vinilos.collectAsState()

    // 1. Convertimos el ID de String (ruta de navegación) a Int
    val viniloIntId = viniloId?.toIntOrNull()

    // 2. Buscamos el vinilo en la lista por su ID
    val vinilo = vinilos.find { it.idVin == viniloIntId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(vinilo?.nombre ?: "Detalle del Vinilo", color = Color.White) },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .clickable { navController.popBackStack() },
                        tint = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2A004E)
                )
            )
        }
    ) { paddingValues ->
        // 3. Chequeo de seguridad: si el vinilo es nulo, mostramos un error
        if (vinilo == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFF2A004E)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Vinilo no encontrado.",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        } else {
            // 4. Si el vinilo existe, mostramos el contenido
            DetalleContenido(
                vinilo = vinilo,
                viewModel = viewModel,
                paddingValues = paddingValues
            )
        }
    }
}

@Composable
fun DetalleContenido(
    vinilo: Vinilo,
    viewModel: ViniloViewModel,
    paddingValues: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- BLOQUE CLAVE PARA LA IMAGEN ---
            AsyncImage(
                model = vinilo.img, // <-- Aquí se usa la URL de la imagen
                contentDescription = "Portada de ${vinilo.nombre}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(300.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Gray) // Fondo gris mientras carga
            )
            // ------------------------------------

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = vinilo.nombre,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = vinilo.artista,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFE0E0E0)
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$${"%,d".format(vinilo.precio)}",
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.Black,
                        color = AccentColor,
                        fontSize = 36.sp
                    )
                )

                Button(
                    onClick = { viewModel.agregarViniloACarrito(vinilo) },
                    colors = ButtonDefaults.buttonColors(containerColor = SecondaryColor),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(56.dp)
                ) {
                    Text("Añadir al Carrito", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xAAFFFFFF).copy(alpha = 0.2f)),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Descripción",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = vinilo.descripcion,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Stock: ${vinilo.stock} unidades",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = if (vinilo.stock > 0) AccentColor else Color.Red
                        )
                    )
                }
            }
        }
    }
}
