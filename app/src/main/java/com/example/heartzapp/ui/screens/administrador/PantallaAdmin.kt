package com.example.heartzapp.ui.screens.administrador

import androidx.compose.foundation.background
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.heartzapp.ui.components.AdminDrawer
import kotlinx.coroutines.launch

// --- Clases de Datos ---
data class Pedido(val id: Int, val cliente: String, val monto: Int, val estado: String)

// Formato simple para moneda
fun Int.formatNumberWithDots(): String {
    return this.toString().reversed().chunked(3).joinToString(".").reversed()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAdmin(navController: NavHostController) {
    var cargando by remember { mutableStateOf(true) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Simular carga de datos
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(1000)
        cargando = false
    }

    // --- DATOS SIMULADOS ---
    val totalVendido = 2145000
    val totalStock = 120
    val totalPedidos = 42

    val pedidos = listOf(
        Pedido(101, "Juan P.", 45000, "Pendiente"),
        Pedido(102, "María G.", 120000, "Enviado"),
        Pedido(103, "Pedro R.", 80000, "Pendiente"),
        Pedido(104, "Ana C.", 95000, "Entregado")
    )

    val primaryColor = Color(0xFF6A1B9A)
    val lightColor = Color(0xFFE1BEE7)

    ModalNavigationDrawer(
        drawerContent = {
            AdminDrawer(
                navController = navController,
                currentRoute = null,
                closeDrawer = { scope.launch { drawerState.close() } }
            )
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Panel de Administración", color = Color.White) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = primaryColor),
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.ListAlt, contentDescription = "Menú", tint = Color.White)
                        }
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate("login") }) {
                            Icon(Icons.Default.Logout, contentDescription = "Cerrar sesión", tint = Color.White)
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
                        Text(text = "Cargando datos...", color = primaryColor)
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
                    // --- Métricas ---
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            MetricSimpleCard(
                                title = "Total Vendido",
                                value = "$${totalVendido.formatNumberWithDots()}",
                                color = Color(0xFF4CAF50),
                                icon = Icons.Default.AttachMoney,
                                modifier = Modifier.weight(1f)
                            )
                            MetricSimpleCard(
                                title = "Stock Total",
                                value = totalStock.toString(),
                                color = Color(0xFF03A9F4),
                                icon = Icons.Default.Inventory2,
                                modifier = Modifier.weight(1f)
                            )
                            MetricSimpleCard(
                                title = "Total Pedidos",
                                value = totalPedidos.toString(),
                                color = Color(0xFFFF9800),
                                icon = Icons.Default.ListAlt,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // --- Pedidos recientes ---
                    item {
                        Text(
                            text = "Pedidos Recientes",
                            style = MaterialTheme.typography.titleLarge.copy(color = primaryColor),
                            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                        )
                    }

                    items(pedidos) { pedido ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = "Pedido #${pedido.id}", fontWeight = FontWeight.SemiBold)
                                    Text(text = "Cliente: ${pedido.cliente}", color = Color.Gray)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(text = "$${pedido.monto.formatNumberWithDots()}", fontWeight = FontWeight.Bold, color = primaryColor)
                                    Text(text = pedido.estado, color = when (pedido.estado) {
                                        "Pendiente" -> Color.Red
                                        "Enviado" -> Color.Yellow
                                        "Entregado" -> Color.Green
                                        else -> Color.Gray
                                    })
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // --- Pie de página ---
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
}

@Composable
fun MetricSimpleCard(
    title: String,
    value: String,
    color: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(120.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
            Icon(icon, contentDescription = title, tint = color, modifier = Modifier.size(30.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(title, fontSize = 12.sp, color = Color.Gray)
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = color)
        }
    }
}
