package com.example.heartzapp.data.repository

import com.example.heartzapp.data.api.DiscogsRetrofitClient
import com.example.heartzapp.data.model.ProximoProducto

class DiscogsRepository {

    private val ids = listOf(32538570,35209387,35383381)

    suspend fun getProximosProductos(): List<ProximoProducto> {
        return ids.mapNotNull { id ->
            try {
                val res = DiscogsRetrofitClient.api.getRelease(id)
                ProximoProducto(
                    id = res.id,
                    titulo = res.title,
                    artista = res.artists.firstOrNull()?.name ?: "Desconocido",
                    anio = res.year,
                    imagen = res.images?.firstOrNull()?.uri ?: ""
                )
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}
