package com.example.heartzapp.ui.screens

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.heartzapp.ui.components.BotonVolver
import com.example.heartzapp.viewmodel.CarritoViewModel
import com.example.heartzapp.viewmodel.ViniloViewModel

@Composable
fun PantallaDetalle(
    navController: NavHostController,
    carritoVM: CarritoViewModel,
    viniloId: String?
) {
    val context = LocalContext.current

    val viniloVM: ViniloViewModel = viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
            .getInstance(context.applicationContext as Application)
    )

    val vinilos by viniloVM.vinilos.collectAsState()

    val vinilo = vinilos.find { it.idVin.toString() == viniloId }

    if (vinilo == null) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Vinilo no encontrado", color = Color.White)
        }
        return
    }

    val nombreSinExt = vinilo.img.substringBeforeLast(".")

    val imageResId = context.resources.getIdentifier(
        nombreSinExt,
        "drawable",
        context.packageName
    )

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2A004E))
                    .padding(16.dp)
            ) {
                Text(
                    text = vinilo.nombre,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF7E57C2), Color(0xFFF3E5F5))
                    )
                )
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                BotonVolver(navController)

                Spacer(Modifier.height(16.dp))

                if (imageResId != 0) {
                    Image(
                        painter = painterResource(imageResId),
                        contentDescription = vinilo.nombre,
                        modifier = Modifier
                            .size(260.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(Modifier.height(20.dp))

                Text(
                    text = vinilo.nombre,
                    style = MaterialTheme.typography.headlineSmall
                        .copy(color = Color.White, fontWeight = FontWeight.Bold)
                )

                Text(
                    text = vinilo.artista,
                    style = MaterialTheme.typography.titleMedium
                        .copy(color = Color(0xFFB388FF))
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "$${"%,d".format(vinilo.precio)}",
                    style = MaterialTheme.typography.headlineMedium
                        .copy(color = Color(0xFFE91E63), fontWeight = FontWeight.Bold)
                )

                Spacer(Modifier.height(30.dp))

                Button(
                    onClick = { carritoVM.agregar(vinilo) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF9C27B0))
                ) {
                    Text("Añadir al carrito", color = Color.White)
                }
            }
        }
    }
}
