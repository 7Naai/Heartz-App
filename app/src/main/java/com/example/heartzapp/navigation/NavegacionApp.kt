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
import com.example.heartzapp.ui.screens.*
import com.example.heartzapp.ui.screens.administrador.PantallaAdmin
import com.example.heartzapp.ui.screens.administrador.PantallaAdminUsuarios
import com.example.heartzapp.ui.screens.administrador.PantallaAdminVinilo
import com.example.heartzapp.ui.screens.perfil.PantallaPerfil
import com.example.heartzapp.ui.screens.perfil.PantallaRegistro
import com.example.heartzapp.viewmodel.*

@Composable
fun NavegacionApp(carritoVM: CarritoViewModel) {

    val navController: NavHostController = rememberNavController()
    val context = LocalContext.current

    val usuarioViewModel: UsuarioViewModel = viewModel(
        factory = UsuarioViewModelFactory()
    )

    val viniloViewModel: ViniloViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(
            context.applicationContext as Application
        )
    )

    val viniloAdminViewModel: ViniloAdminViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(
            context.applicationContext as Application
        )
    )

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("inicio") { PantallaInicio(navController, carritoVM) }
        composable("productos") { PantallaProductos(navController, carritoVM) }
        composable("perfil") { PantallaPerfil(navController) }

        composable("login") { PantallaLogin(navController, usuarioViewModel) }
        composable("registro") { PantallaRegistro(navController, usuarioViewModel) }

        composable("admin") { PantallaAdmin(navController) }
        composable("adminVinilo") { PantallaAdminVinilo(navController, viniloAdminViewModel) }
        composable("adminUsuario") { PantallaAdminUsuarios(navController, usuarioViewModel) }

        composable("carrito") { PantallaCarrito(navController, carritoVM) }

        composable("pago") { PantallaPago(navController, carritoVM) }
        composable("boleta") { PantallaBoleta(navController, carritoVM) }

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
                carritoVM = carritoVM,
                viniloId = backStackEntry.arguments?.getString("idVin")
            )
        }
    }
}
