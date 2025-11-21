package com.example.heartzapp.data.repository

import com.example.heartzapp.data.api.ApiService   // <-- ESTE ES EL CORRECTO
import com.example.heartzapp.data.model.ItemCarrito
import com.example.heartzapp.data.model.Vinilo

class ViniloRepository {

    // ------------------------------------------------
    //  API: CRUD DE VINILOS
    // ------------------------------------------------

    suspend fun getAllVinilos(): List<Vinilo>? {
        return try {
            ApiService.vinilos.getVinilos()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun insertVinilo(vinilo: Vinilo): Boolean {
        return try {
            ApiService.vinilos.insertVinilo(vinilo)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun updateVinilo(vinilo: Vinilo): Boolean {
        return try {
            ApiService.vinilos.updateVinilo(vinilo.idVin, vinilo)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun deleteVinilo(vinilo: Vinilo): Boolean {
        return try {
            ApiService.vinilos.deleteVinilo(vinilo.idVin)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    // ------------------------------------------------
    //  API: CARRITO
    // ------------------------------------------------

    suspend fun getCarrito(): List<ItemCarrito>? {
        return try {
            ApiService.carrito.getCarrito()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun agregarAlCarrito(item: ItemCarrito): Boolean {
        return try {
            ApiService.carrito.agregarAlCarrito(item)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun eliminarItemCarrito(id: Int): Boolean {
        return try {
            ApiService.carrito.eliminarItem(id)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun vaciarCarrito(): Boolean {
        return try {
            ApiService.carrito.vaciarCarrito()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
