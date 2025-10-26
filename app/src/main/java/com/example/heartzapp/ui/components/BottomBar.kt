package com.example.heartzapp.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavHostController

@Composable
fun BottomBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Inicio", "inicio"),
        BottomNavItem("Productos", "productos"),
        BottomNavItem("Perfil", "perfil")
    )

    NavigationBar(
        containerColor = Color.Black, // COLOR DEL FONDO DE LA BARRA!!
        contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = {}, // aqui deberian ir los iconos lol
                label = {
                    Text(
                        text = item.title,
                        color = if (currentRoute == item.route)
                            Color(0xFFB388FF) // morado claro si está seleccionado
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
    val route: String
)
