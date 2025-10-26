package com.example.heartzapp.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.heartzapp.ui.screens.PantallaCarrito // Asegúrate de que esta línea exista
import com.example.heartzapp.ui.screens.PantallaInicio
import com.example.heartzapp.ui.screens.PantallaLogin
import com.example.heartzapp.ui.screens.PantallaProductos
import com.example.heartzapp.ui.screens.perfil.PantallaPerfil
import com.example.heartzapp.ui.screens.perfil.PantallaRegistro
import com.example.heartzapp.viewmodel.UsuarioViewModel

@Composable
fun NavegacionApp() {
    val navController: NavHostController = rememberNavController()
    val usuarioViewModel: UsuarioViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "inicio"
    ) {
        composable("inicio") { PantallaInicio(navController) }
        composable("productos") { PantallaProductos(navController) }
        composable("perfil") { PantallaPerfil(navController) }

        composable(route = "login") { PantallaLogin(navController) }
        composable(route = "registro") { PantallaRegistro(navController, usuarioViewModel) }

        composable(route = "carrito") { PantallaCarrito(navController) }

    }
}
