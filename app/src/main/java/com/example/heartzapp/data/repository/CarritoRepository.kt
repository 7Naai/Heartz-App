package com.example.heartzapp.data.repository

import com.example.heartzapp.data.api.ApiService
import com.example.heartzapp.data.model.ItemCarrito

class CarritoRepository {

    suspend fun getCarrito(): List<ItemCarrito>? {
        return try {
            ApiService.carrito.getCarrito()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun agregarItem(item: ItemCarrito): Boolean {
        return try {
            ApiService.carrito.agregarAlCarrito(item)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun eliminarItem(id: Int): Boolean {
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
