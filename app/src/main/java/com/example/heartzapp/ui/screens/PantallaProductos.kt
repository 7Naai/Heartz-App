package com.example.heartzapp.ui.screens

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.heartzapp.ui.components.BottomBar
import com.example.heartzapp.ui.components.TarjetaVinilo
import com.example.heartzapp.viewmodel.CarritoViewModel
import com.example.heartzapp.viewmodel.ViniloViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaProductos(navController: NavHostController, carritoVM: CarritoViewModel) {

    val context = LocalContext.current

    val viniloVM: ViniloViewModel = viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
            .getInstance(context.applicationContext as Application)
    )

    val vinilos by viniloVM.vinilos.collectAsState()
    val isLoading by viniloVM.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Catálogo de Vinilos",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2A004E)
                )
            )
        },
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->

        Box(
            modifier = Modifier
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

            when {
                isLoading -> {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
                vinilos.isEmpty() -> {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        Text(
                            "No hay vinilos disponibles",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        items(vinilos) { vinilo ->
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
