package com.example.heartzapp.data.api

import com.example.heartzapp.data.model.ItemCarrito
import retrofit2.http.*

interface CarritoApi {

    @GET("carrito")
    suspend fun obtenerCarrito(): List<ItemCarrito>

    @POST("carrito")
    suspend fun agregarItem(@Body item: ItemCarrito): ItemCarrito

    @DELETE("carrito/{id}")
    suspend fun eliminarItem(@Path("id") id: Int)

    @DELETE("carrito")
    suspend fun limpiarCarrito()
}
