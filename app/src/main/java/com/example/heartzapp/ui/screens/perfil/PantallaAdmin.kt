package com.example.heartzapp.ui.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

// --- Clases de Datos (Manteniendo las estructuras originales y añadiendo Pedido) ---
data class Producto(val nombre: String, val stock: Int)
data class Usuario(val nombre: String, val correo: String, val rol: String)
data class Pedido(val id: Int, val cliente: String, val monto: Int, val estado: String)

// Función auxiliar simple para formatear números con puntos como separadores de miles
// (sin usar NumberFormat, como se solicitó)
fun Int.formatNumberWithDots(): String {
    return this.toString()
        .reversed()
        .chunked(3)
        .joinToString(".")
        .reversed()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAdmin(navController: NavHostController) {
    var cargando by remember { mutableStateOf(true) }

    // Simular carga de datos
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(1000) // Reducido a 1s para mejor experiencia
        cargando = false
    }

    // --- DATOS SIMULADOS ---
    val totalVendido = 2145000 // Monto en pesos chilenos (Cambiado a 2.145.000)
    val totalStock = 120
    val totalPedidos = 42

    val pedidos = listOf(
        Pedido(101, "Juan P.", 45000, "Pendiente"),
        Pedido(102, "María G.", 120000, "Enviado"),
        Pedido(103, "Pedro R.", 80000, "Pendiente"),
        Pedido(104, "Ana C.", 95000, "Entregado"),
    )

    // Formato simple para moneda (usando la función auxiliar)
    val totalVendidoConPuntos = totalVendido.formatNumberWithDots()

    val primaryColor = Color(0xFF6A1B9A) // Púrpura Oscuro
    val lightColor = Color(0xFFE1BEE7)  // Lavanda Claro

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel de Administración", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = primaryColor),
                actions = {
                    IconButton(onClick = { navController.navigate("login") }) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Cerrar sesión",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (cargando) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(lightColor)
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = primaryColor)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Cargando datos...",
                        color = primaryColor,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(lightColor)
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // --- 1. Tarjetas de Métricas (Total Vendido, Stock, Pedidos) ---
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        MetricCard(
                            icon = Icons.Default.AttachMoney,
                            title = "Total Vendido",
                            value = "$$totalVendidoConPuntos",
                            color = Color(0xFF4CAF50), // Verde
                            modifier = Modifier.weight(1f)
                        )
                        MetricCard(
                            icon = Icons.Default.Inventory2,
                            title = "Stock Total",
                            value = totalStock.toString(),
                            color = Color(0xFF03A9F4), // Azul
                            modifier = Modifier.weight(1f)
                        )
                        MetricCard(
                            icon = Icons.Default.ListAlt,
                            title = "Total Pedidos",
                            value = totalPedidos.toString(),
                            color = Color(0xFFFF9800), // Naranja
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // --- 2. Listado de Pedidos Recientes ---
                item {
                    Text(
                        text = "Pedidos Recientes",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = primaryColor
                        ),
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                }

                items(pedidos) { pedido ->
                    PedidoItemCard(pedido = pedido)
                }

                // --- Pie de página (Separador visual) ---
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "Panel de control v1.0",
                        color = Color.Gray,
                        modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

// --- Componente para las Tarjetas de Métrica ---
@Composable
fun MetricCard(icon: ImageVector, title: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(120.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 1
            )
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = color,
                maxLines = 1
            )
        }
    }
}

// --- Componente para cada Ítem de Pedido ---
@Composable
fun PedidoItemCard(pedido: Pedido) {
    val estadoColor = when (pedido.estado) {
        "Pendiente" -> Color(0xFFD32F2F) // Rojo
        "Enviado" -> Color(0xFFFFC107)    // Amarillo
        "Entregado" -> Color(0xFF4CAF50) // Verde
        else -> Color.Gray
    }

    val montoConPuntos = pedido.monto.formatNumberWithDots() // Renombrado

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Indicador de Estado (Círculo)
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(estadoColor)
                    .align(Alignment.Top)
                    .offset(y = 4.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Pedido #${pedido.id}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Text(
                    text = "Cliente: ${pedido.cliente}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$$montoConPuntos",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6A1B9A)
                )
                Text(
                    text = pedido.estado,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = estadoColor,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
