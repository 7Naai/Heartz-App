package com.example.heartzapp.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.heartzapp.ui.screens.PantallaBoleta
import com.example.heartzapp.ui.screens.PantallaCarrito
import com.example.heartzapp.ui.screens.PantallaDetalle
import com.example.heartzapp.ui.screens.PantallaInicio
import com.example.heartzapp.ui.screens.PantallaLogin
import com.example.heartzapp.ui.screens.PantallaPago
import com.example.heartzapp.ui.screens.PantallaProductos
import com.example.heartzapp.ui.screens.perfil.PantallaAdmin
import com.example.heartzapp.ui.screens.perfil.PantallaPerfil
import com.example.heartzapp.ui.screens.perfil.PantallaRegistro
import com.example.heartzapp.viewmodel.UsuarioViewModel
import com.example.heartzapp.viewmodel.ViniloViewModel

@Composable
fun NavegacionApp() {
    val navController: NavHostController = rememberNavController()
    val usuarioViewModel: UsuarioViewModel = viewModel()

    val context = LocalContext.current
    val viniloViewModel: ViniloViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(
            context.applicationContext as Application
        )
    )

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("inicio") { PantallaInicio(navController) }
        composable("productos") { PantallaProductos(navController) }
        composable("perfil") { PantallaPerfil(navController) }

        composable(route = "login") { PantallaLogin(navController, usuarioViewModel) }
        composable(route = "registro") { PantallaRegistro(navController, usuarioViewModel) }
        composable("admin") { PantallaAdmin(navController) }
        composable(route = "carrito") { PantallaCarrito(navController) }
        composable(route = "pago") { PantallaPago(navController) }

        composable(route = "boleta") {
            PantallaBoleta(navController, viniloViewModel)
        }

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
    }
}
