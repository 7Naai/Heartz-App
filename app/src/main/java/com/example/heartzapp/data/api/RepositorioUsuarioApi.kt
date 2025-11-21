package com.example.heartzapp.data.api

import com.example.heartzapp.data.model.Usuario

class RepositorioUsuarioApi {

    private val api = ApiClient.retrofit.create(UsuarioApi::class.java)

    suspend fun obtenerUsuarios(): List<Usuario> {
        return api.getUsuarios()
    }

    suspend fun obtenerUsuarioPorRut(rut: String): Usuario {
        return api.getUsuarioByRut(rut)
    }

    suspend fun crearUsuario(usuario: Usuario): Usuario {
        return api.crearUsuario(usuario)
    }

    suspend fun actualizarUsuario(rut: String, usuario: Usuario): Usuario {
        return api.actualizarUsuario(rut, usuario)
    }

    suspend fun eliminarUsuario(rut: String) {
        api.eliminarUsuario(rut)
    }
}
