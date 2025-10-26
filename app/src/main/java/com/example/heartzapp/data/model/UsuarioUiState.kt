package com.example.heartzapp.data.model

data class UsuarioUiState (
    val rut: String = "",
    val nombre: String = "",
    val correo: String = "",
    val contrasena: String = "",
    val aceptaTerminos: Boolean = false,
    val errores: UsuarioErrores = UsuarioErrores()
)