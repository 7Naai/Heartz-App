package com.example.heartzapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey
    val rut: String,

    val nombre: String,
    val correo: String,
    val rol: String,
    val fechaNacimiento: String
)
