package com.example.heartzapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.heartzapp.data.model.Usuario

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(usuario: Usuario)

    @Query("SELECT * FROM usuarios")
    suspend fun getAllUsuarios(): List<Usuario>

    @Query("SELECT * FROM usuarios WHERE rut = :rut")
    suspend fun getUsuarioByRut(rut: String): Usuario?

    @Query("DELETE FROM usuarios")
    suspend fun deleteAll()
}
