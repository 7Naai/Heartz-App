package com.example.heartzapp.navigation

sealed class Pantallas(val ruta: String) {
    object Inicio : Pantallas("inicio")
    object Productos : Pantallas("productos")
    object Perfil : Pantallas("perfil")
    //object Login : Pantallas("login")
    //object Registro : Pantallas("registro")
}