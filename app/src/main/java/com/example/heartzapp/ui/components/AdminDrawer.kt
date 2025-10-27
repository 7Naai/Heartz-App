package com.example.heartzapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.heartzapp.R

sealed class AdminDrawerItem(val route: String, val title: String, val iconRes: Int) {
    data object Tienda : AdminDrawerItem("inicio", "Volver a la Tienda", R.drawable.local_mall_icon_opsz24)
    data object Vinilos : AdminDrawerItem("adminVinilo", "Gestión de Vinilos", R.drawable.album_icon_opsz24)
    data object Usuarios : AdminDrawerItem("adminUsuario", "Gestión de Usuarios", R.drawable.group_icon_opsz24)
}

@Composable
fun AdminDrawer(
    navController: NavController,
    currentRoute: String?,
    closeDrawer: () -> Unit
) {
    val items = listOf(
        AdminDrawerItem.Vinilos,
        AdminDrawerItem.Usuarios,
    )

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.7f)
            .background(Color(0xFF3B006A))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.account_circle_admin_opsz24),
                contentDescription = "Icono de Administrador",
                modifier = Modifier.size(40.dp)
            )
            Column {
                Text(
                    text = "Panel de Administración",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "Heartz App",
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(Modifier.height(24.dp))
        Divider(color = Color(0xFF6A1B9A))
        Spacer(Modifier.height(8.dp))

        items.forEach { item ->
            NavigationDrawerItem(
                label = { Text(item.title) },
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = item.title,
                        modifier = Modifier.size(24.dp)
                    )
                },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route)
                    closeDrawer()
                },
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = Color(0xFF6A1B9A),
                    unselectedContainerColor = Color.Transparent,
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.7f),
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.White.copy(alpha = 0.9f)
                ),
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        Spacer(Modifier.weight(1f))

        Divider(color = Color(0xFF6A1B9A))

        NavigationDrawerItem(
            label = { Text(AdminDrawerItem.Tienda.title) },
            icon = {
                Icon(
                    painter = painterResource(id = AdminDrawerItem.Tienda.iconRes),
                    contentDescription = AdminDrawerItem.Tienda.title,
                    modifier = Modifier.size(24.dp)
                )
            },
            selected = false,
            onClick = {
                navController.navigate(AdminDrawerItem.Tienda.route) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                }
                closeDrawer()
            },
            colors = NavigationDrawerItemDefaults.colors(
                unselectedContainerColor = Color(0xFFE57373).copy(alpha = 0.2f),
                unselectedIconColor = Color(0xFFE57373),
                unselectedTextColor = Color(0xFFE57373),
            ),
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}
