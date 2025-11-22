package com.example.heartzapp.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// El ID del vinilo (idVin) es la clave foránea
@Entity(
    tableName = "carrito",
    foreignKeys = [
        ForeignKey(
            entity = Vinilo::class,
            parentColumns = ["idVin"],
            childColumns = ["viniloId"],
            onDelete = ForeignKey.CASCADE // Si borras el vinilo, se borra del carrito
        )
    ]
)
data class ItemCarrito(
    val id: Int? = null,
    val viniloId: Int,
    val nombre: String,
    val precio: Int,
    val img: String,
    val cantidad: Int = 1
)