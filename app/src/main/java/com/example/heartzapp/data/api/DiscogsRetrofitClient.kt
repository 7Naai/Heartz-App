package com.example.heartzapp.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DiscogsRetrofitClient {

    private const val BASE_URL = "https://api.discogs.com/"

    val api: DiscogsApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DiscogsApi::class.java)
    }
}
