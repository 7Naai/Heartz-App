package com.example.heartzapp.data.api

import com.example.heartzapp.data.model.ItemCarrito
import retrofit2.http.*

interface CarritoApi {

    @GET("carrito")
    suspend fun getCarrito(): List<ItemCarrito>

    @POST("carrito")
    suspend fun agregarAlCarrito(@Body item: ItemCarrito)

    @DELETE("carrito/{id}")
    suspend fun eliminarItem(@Path("id") id: Int)

    @DELETE("carrito/vaciar")
    suspend fun vaciarCarrito()
}
