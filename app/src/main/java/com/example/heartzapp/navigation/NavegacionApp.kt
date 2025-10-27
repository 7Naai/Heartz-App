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
import com.example.heartzapp.data.AppDatabase
import com.example.heartzapp.data.repository.UsuarioRepository
import com.example.heartzapp.ui.screens.PantallaBoleta
import com.example.heartzapp.ui.screens.PantallaCarrito
import com.example.heartzapp.ui.screens.PantallaDetalle
import com.example.heartzapp.ui.screens.PantallaInicio
import com.example.heartzapp.ui.screens.PantallaLogin
import com.example.heartzapp.ui.screens.PantallaPago
import com.example.heartzapp.ui.screens.PantallaProductos
import com.example.heartzapp.ui.screens.administrador.PantallaAdmin
import com.example.heartzapp.ui.screens.administrador.PantallaAdminUsuarios
import com.example.heartzapp.ui.screens.administrador.PantallaAdminVinilo
import com.example.heartzapp.ui.screens.perfil.PantallaPerfil
import com.example.heartzapp.ui.screens.perfil.PantallaRegistro
import com.example.heartzapp.viewmodel.UsuarioViewModel
import com.example.heartzapp.viewmodel.UsuarioViewModelFactory
import com.example.heartzapp.viewmodel.ViniloViewModel
import com.example.heartzapp.viewmodel.ViniloAdminViewModel

@Composable
fun NavegacionApp() {
    val navController: NavHostController = rememberNavController()
    val context = LocalContext.current

    // --- Configurar repositorio y factory para UsuarioViewModel ---
    val db = AppDatabase.getInstance(context)
    val usuarioRepository = UsuarioRepository(db.usuarioDao())
    val usuarioViewModel: UsuarioViewModel = viewModel(
        factory = UsuarioViewModelFactory(usuarioRepository)
    )

    // --- ViniloViewModel con AndroidViewModelFactory ---
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

    // --- NavHost ---
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("inicio") { PantallaInicio(navController) }
        composable("productos") { PantallaProductos(navController) }
        composable("perfil") { PantallaPerfil(navController) }

        composable("login") { PantallaLogin(navController, usuarioViewModel) }
        composable("registro") { PantallaRegistro(navController, usuarioViewModel) }

        composable("admin") { PantallaAdmin(navController) }
        composable("adminVinilo") { PantallaAdminVinilo(navController, viniloAdminViewModel) }
        composable("adminUsuario") { PantallaAdminUsuarios(navController, usuarioViewModel) }

        composable("carrito") { PantallaCarrito(navController) }
        composable("pago") { PantallaPago(navController) }
        composable("boleta") { PantallaBoleta(navController, viniloViewModel) }

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
