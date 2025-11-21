package com.example.heartzapp.data.api

import com.example.heartzapp.data.model.Vinilo
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.GET

class RepositorioVinilosApi {

    private val api = ApiClient.retrofit.create(ViniloApi::class.java)

    // ------------------------
    // GET (todos)
    // ------------------------
    suspend fun obtenerVinilos(): List<Vinilo> {
        return api.getAllVinilos()
    }

    // ------------------------
    // GET (por ID)
    // ------------------------
    suspend fun obtenerVinilo(id: Int): Vinilo {
        return api.getViniloById(id)
    }

    // ------------------------
    // POST (crear vinilo)
    // ------------------------
    suspend fun crearVinilo(vinilo: Vinilo): Vinilo {
        return api.createVinilo(vinilo)
    }

    // ------------------------
    // PUT (actualizar)
    // ------------------------
    suspend fun actualizarVinilo(id: Int, vinilo: Vinilo): Vinilo {
        return api.updateVinilo(id, vinilo)
    }

    // ------------------------
    // DELETE
    // ------------------------
    suspend fun eliminarVinilo(id: Int) {
        api.deleteVinilo(id)
    }
}
