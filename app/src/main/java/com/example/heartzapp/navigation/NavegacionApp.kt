package com.example.heartzapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.heartzapp.ui.screens.PantallaInicio
import com.example.heartzapp.ui.screens.PantallaProductos
import com.example.heartzapp.ui.screens.perfil.PantallaPerfil

@Composable
fun NavegacionApp() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "inicio"
    ) {
        composable("inicio") { PantallaInicio(navController) }
        composable("productos") { PantallaProductos(navController) }
        composable("perfil") { PantallaPerfil(navController) }
    }
}