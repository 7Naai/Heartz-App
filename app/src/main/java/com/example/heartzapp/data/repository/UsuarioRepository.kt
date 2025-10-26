package com.example.heartzapp.data.repository

import com.example.heartzapp.data.dao.UsuarioDao
import com.example.heartzapp.data.model.Usuario

class UsuarioRepository(private val usuarioDao: UsuarioDao) {

    suspend fun insertUsuario(usuario: Usuario) {
        usuarioDao.insert(usuario)
    }

    suspend fun getAllUsuarios(): List<Usuario> {
        return usuarioDao.getAllUsuarios()
    }

    suspend fun getUsuarioByRut(rut: String): Usuario? {
        return usuarioDao.getUsuarioByRut(rut)
    }

    suspend fun deleteAllUsuarios() {
        usuarioDao.deleteAll()
    }
}
