package com.example.heartzapp.data.model

data class ItemCarrito(
    val id: Int = 0,
    val viniloId: Int,
    val nombre: String,
    val precio: Int,
    val img: String,
    val cantidad: Int = 1
)
