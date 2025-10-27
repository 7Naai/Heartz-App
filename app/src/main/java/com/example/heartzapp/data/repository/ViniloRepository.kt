package com.example.heartzapp.data.repository

import com.example.heartzapp.data.dao.ItemCarritoDao
import com.example.heartzapp.data.dao.ViniloDao
import com.example.heartzapp.data.model.Vinilo
import com.example.heartzapp.data.model.ItemCarrito
import kotlinx.coroutines.flow.Flow

class ViniloRepository(
    private val viniloDao: ViniloDao,
    private val carritoDao: ItemCarritoDao
) {

    // ---------------- VINILOS ----------------

    suspend fun insertVinilo(vinilo: Vinilo) {
        viniloDao.insert(vinilo)
    }

    suspend fun updateVinilo(vinilo: Vinilo) {
        viniloDao.update(vinilo)
    }

    suspend fun deleteVinilo(vinilo: Vinilo) {
        viniloDao.delete(vinilo)
    }

    suspend fun getAllVinilos(): List<Vinilo> {
        return viniloDao.getAllVinilos()
    }

    suspend fun getViniloById(id: Int): Vinilo? {
        return viniloDao.getViniloById(id)
    }

    suspend fun deleteAllVinilos() {
        viniloDao.deleteAll()
    }

    // ---------------- CARRITO ----------------

    fun getCarritoItems(): Flow<List<ItemCarrito>> = carritoDao.getAllItems()

    suspend fun addItemToCarrito(item: Vinilo) {
        val existingItem = carritoDao.getItemByViniloId(item.idVin)

        if (existingItem != null) {
            carritoDao.updateCantidad(item.idVin, existingItem.cantidad + 1)
        } else {
            val newItem = ItemCarrito(
                viniloId = item.idVin,
                nombre = item.nombre,
                precio = item.precio,
                img = item.img,
                cantidad = 1
            )
            carritoDao.insert(newItem)
        }
    }

    suspend fun removeItemFromCarrito(item: ItemCarrito) {
        if (item.cantidad > 1) {
            carritoDao.updateCantidad(item.viniloId, item.cantidad - 1)
        } else {
            carritoDao.delete(item)
        }
    }

    suspend fun clearCarrito() = carritoDao.clear()

    // ---------------- NUEVAS FUNCIONES ----------------

    /** Actualiza la cantidad de un ítem del carrito */
    suspend fun updateItemCarrito(item: ItemCarrito) {
        carritoDao.updateCantidad(item.viniloId, item.cantidad)
    }

    /** Elimina un ítem del carrito */
    suspend fun deleteItemCarrito(item: ItemCarrito) {
        carritoDao.delete(item)
    }
}
