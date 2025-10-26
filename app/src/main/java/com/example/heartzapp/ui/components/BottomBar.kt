package com.example.heartzapp.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavHostController
import com.example.heartzapp.R

@Composable
fun BottomBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Inicio", "inicio", R.drawable.home_icon_opsz24),
        BottomNavItem("Productos", "productos", R.drawable.local_mall_icon_opsz24),
        BottomNavItem("Perfil", "perfil", R.drawable.account_box_icon_opsz24)
    )

    NavigationBar(
        containerColor = Color.Black,
        contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = item.title,
                        modifier = Modifier.size(32.dp) // Tamaño ajustado
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        color = if (currentRoute == item.route)
                            Color(0xFFB388FF)
                        else
                            Color.White
                    )
                },
                selected = currentRoute == item.route,
                onClick = { navController.navigate(item.route) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

data class BottomNavItem(
    val title: String,
    val route: String,
    val iconRes: Int
)
