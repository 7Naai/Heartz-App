package com.example.heartzapp.data.api

import com.example.heartzapp.data.model.ItemCarrito
import com.example.heartzapp.data.model.Vinilo
import retrofit2.http.*

interface ViniloApi {

    // -----------------------
    // VINILOS (CRUD)
    // -----------------------

    @GET("vinilos")
    suspend fun getVinilos(): List<Vinilo>

    @POST("vinilos")
    suspend fun insertVinilo(@Body vinilo: Vinilo)

    @PUT("vinilos/{id}")
    suspend fun updateVinilo(@Path("id") id: Int, @Body vinilo: Vinilo)

    @DELETE("vinilos/{id}")
    suspend fun deleteVinilo(@Path("id") id: Int)

    // -----------------------
    // CARRITO
    // -----------------------

    @GET("carrito")
    suspend fun getCarrito(): List<ItemCarrito>

    @POST("carrito")
    suspend fun agregarAlCarrito(@Body item: ItemCarrito)

    @DELETE("carrito/{id}")
    suspend fun eliminarItemCarrito(@Path("id") id: Int)

    @DELETE("carrito/vaciar")
    suspend fun vaciarCarrito()
}
