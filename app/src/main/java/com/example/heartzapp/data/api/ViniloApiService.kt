package com.example.heartzapp.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {

    private const val BASE_URL = "http://10.0.2.2:9090/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // --- ENDPOINTS DE VINILOS ---
    val vinilos: ViniloApi by lazy {
        retrofit.create(ViniloApi::class.java)
    }

    // --- ENDPOINTS DE CARRITO ---
    val carrito: CarritoApi by lazy {
        retrofit.create(CarritoApi::class.java)
    }
}
