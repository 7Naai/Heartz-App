package com.example.heartzapp.data.api

import com.example.heartzapp.data.model.Usuario
import retrofit2.http.*

interface UsuarioApi {

    @GET("usuarios")
    suspend fun getUsuarios(): List<Usuario>

    @GET("usuarios/{rut}")
    suspend fun getUsuarioByRut(@Path("rut") rut: String): Usuario

    @POST("usuarios")
    suspend fun crearUsuario(@Body usuario: Usuario): Usuario

    @PUT("usuarios/{rut}")
    suspend fun actualizarUsuario(@Path("rut") rut: String, @Body usuario: Usuario): Usuario

    @DELETE("usuarios/{rut}")
    suspend fun eliminarUsuario(@Path("rut") rut: String)
}
