package com.example.heartzapp.data.api

import com.example.heartzapp.data.model.DiscogsRelease
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface DiscogsApi {

    @GET("releases/{id}")
    suspend fun getRelease(
        @Path("id") id: Int,
        @Header("User-Agent") userAgent: String = "HeartzApp/1.0"
    ): DiscogsRelease
}
