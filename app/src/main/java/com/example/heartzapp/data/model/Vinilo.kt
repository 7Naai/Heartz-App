package com.example.heartzapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vinilos")
data class Vinilo(
    @PrimaryKey(autoGenerate = true)
    val idVin: Int = 0,

    val nombre: String,
    val artista: String,
    val genero: String,
    val anno: Int,
    val precio: Int,
    val formato: String,
    val colorVinilo: String,
    val stock: Int,
    val sello: String,
    val pais: String,
    val edicion: String,
    val duracion: String,
    val descripcion: String,
    val img: String
)
