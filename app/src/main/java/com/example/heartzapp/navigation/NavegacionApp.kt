package com.example.heartzapp.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.heartzapp.ui.screens.PantallaCarrito
import com.example.heartzapp.ui.screens.PantallaDetalle // Importación de la pantalla de detalle
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

        composable(
            route = "detalle/{idVin}",
            arguments = listOf(
                navArgument("idVin") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            PantallaDetalle(
                navController = navController,
                viniloId = backStackEntry.arguments?.getString("idVin")
            )
        }
        // ------------------------------------------------------------------------

    }
}
