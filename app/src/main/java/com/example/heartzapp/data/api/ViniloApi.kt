package com.example.heartzapp.data.api

import com.example.heartzapp.data.model.Vinilo
import retrofit2.http.*

interface ViniloApi {

    @GET("vinilos")
    suspend fun getAllVinilos(): List<Vinilo>

    @GET("vinilos/{id}")
    suspend fun getViniloById(@Path("id") id: Int): Vinilo

    @POST("vinilos")
    suspend fun createVinilo(@Body vinilo: Vinilo): Vinilo

    @PUT("vinilos/{id}")
    suspend fun updateVinilo(@Path("id") id: Int, @Body vinilo: Vinilo): Vinilo

    @DELETE("vinilos/{id}")
    suspend fun deleteVinilo(@Path("id") id: Int)
}
