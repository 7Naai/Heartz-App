package com.example.heartzapp.data.api

import com.example.heartzapp.data.model.ItemCarrito

class RepositorioCarritoApi {

    private val api = ApiClient.retrofit.create(CarritoApi::class.java)

    suspend fun agregarAlCarrito(
        viniloId: Int,
        nombre: String,
        precio: Int,
        img: String
    ) = api.agregarItem(
        ItemCarrito(
            viniloId = viniloId,
            nombre = nombre,
            precio = precio,
            img = img,
            cantidad = 1
        )
    )

    suspend fun obtenerCarrito() = api.obtenerCarrito()

    suspend fun eliminarItem(id: Int) = api.eliminarItem(id)

    suspend fun limpiarCarrito() = api.limpiarCarrito()
}
