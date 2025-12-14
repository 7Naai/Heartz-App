package com.example.heartzapp.ui.screens

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.heartzapp.ui.components.BottomBar
import com.example.heartzapp.ui.components.ProximosProductos
import com.example.heartzapp.ui.components.TarjetaVinilo
import com.example.heartzapp.viewmodel.CarritoViewModel
import com.example.heartzapp.viewmodel.ViniloViewModel

@Composable
fun PantallaInicio(
    navController: NavHostController,
    carritoVM: CarritoViewModel
) {
    val context = LocalContext.current

    val viniloVM: ViniloViewModel = viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
            .getInstance(context.applicationContext as Application)
    )

    val vinilos by viniloVM.vinilos.collectAsState()
    val isLoading by viniloVM.isLoading.collectAsState()

    val productosDestacados = vinilos.take(4)

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2A004E))
                    .padding(16.dp)
            ) {
                Text(
                    text = "HeartzApp",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        },
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color(0xFF7E57C2),
                            Color(0xFFF3E5F5)
                        )
                    )
                ),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // ─────────────────────────────
            // TÍTULO
            // ─────────────────────────────
            item {
                Text(
                    text = "Productos destacados",
                    style = MaterialTheme.typography.titleLarge.copy(color = Color.White)
                )
            }

            // ─────────────────────────────
            // VINILOS DESTACADOS (4 FIJOS)
            // ─────────────────────────────
            item {
                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color.White)
                        }
                    }

                    productosDestacados.size < 4 -> {
                        Text(
                            text = "No hay suficientes vinilos destacados",
                            color = Color.White
                        )
                    }

                    else -> {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                productosDestacados.take(2).forEach { vinilo ->
                                    Box(modifier = Modifier.weight(1f)) {
                                        TarjetaVinilo(
                                            vinilo = vinilo,
                                            carritoVM = carritoVM,
                                            onVerDetalle = {
                                                navController.navigate("detalle/${vinilo.idVin}")
                                            }
                                        )
                                    }
                                }
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                productosDestacados.drop(2).take(2).forEach { vinilo ->
                                    Box(modifier = Modifier.weight(1f)) {
                                        TarjetaVinilo(
                                            vinilo = vinilo,
                                            carritoVM = carritoVM,
                                            onVerDetalle = {
                                                navController.navigate("detalle/${vinilo.idVin}")
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ─────────────────────────────
            // PRÓXIMOS PRODUCTOS (API EXTERNA)
            // ─────────────────────────────
            item {
                ProximosProductos()
            }
        }
    }
}
