package com.example.heartzapp.ui.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

data class Producto(val nombre: String, val stock: Int)
data class Usuario(val nombre: String, val correo: String, val rol: String)

@Composable
fun PantallaAdmin(navController: NavHostController) {
    var cargando by remember { mutableStateOf(true) }

    // Simular carga de datos por 2 segundos
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2000)
        cargando = false
    }

    if (cargando) {
        // Loader centrado
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF3E5F5)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = Color(0xFF3B006A))
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Cargando panel de administrador...",
                    color = Color(0xFF3B006A),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    } else {
        // Contenido real del panel
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF3E5F5))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Button(
                    onClick = { navController.navigate("inicio") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8E24AA))
                ) {
                    Text("Cerrar sesión", color = Color.White)
                }
            }

            val totalVentas = 42
            val totalStock = 120

            val productos = listOf(
                Producto("Vinilo A", 12),
                Producto("Vinilo B", 5),
                Producto("Vinilo C", 20)
            )

            val usuarios = listOf(
                Usuario("Juan Pérez", "juan@mail.com", "Cliente"),
                Usuario("María González", "maria@mail.com", "Empleado"),
                Usuario("Pedro Ramírez", "pedro@mail.com", "Cliente")
            )

            // Resumen
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF8E24AA))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Resumen", fontSize = 20.sp, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Total de ventas: $totalVentas", color = Color.White, fontSize = 16.sp)
                        Text("Stock total: $totalStock", color = Color.White, fontSize = 16.sp)
                    }
                }
            }

            // Productos
            @Composable
            fun productsList(productos: List<Producto>) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    productos.forEach { producto ->
                        Text("- ${producto.nombre} | Stock: ${producto.stock}", fontSize = 14.sp)
                    }
                }
            }

            @Composable
            fun usersList(usuarios: List<Usuario>) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    usuarios.forEach { usuario ->
                        Text("- ${usuario.nombre} | ${usuario.rol} | ${usuario.correo}", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}
