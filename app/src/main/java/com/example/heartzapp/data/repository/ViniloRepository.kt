package com.example.heartzapp.data.repository

import com.example.heartzapp.data.dao.ItemCarritoDao
import com.example.heartzapp.data.dao.ViniloDao
import com.example.heartzapp.data.model.Vinilo
import com.example.heartzapp.data.model.ItemCarrito
import kotlinx.coroutines.flow.Flow

class ViniloRepository(
    private val viniloDao: ViniloDao,
    private val carritoDao: ItemCarritoDao // 1. Recibe el nuevo DAO del carrito
) {

    // --- MÉTODOS DE VINILO (EXISTENTES) ---

    suspend fun insertVinilo(vinilo: Vinilo) {
        viniloDao.insert(vinilo)
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

    // --- MÉTODOS DEL CARRITO (NUEVOS) ---

    // 2. Obtiene todos los ítems del carrito como un Flow (para gestión de estado)
    fun getCarritoItems(): Flow<List<ItemCarrito>> = carritoDao.getAllItems()

    // 3. Agrega un Vinilo al carrito, manejando si ya existe (incrementa cantidad)
    suspend fun addItemToCarrito(item: Vinilo) {
        val existingItem = carritoDao.getItemByViniloId(item.idVin)

        if (existingItem != null) {
            // Si ya existe, incrementamos la cantidad
            carritoDao.updateCantidad(item.idVin, existingItem.cantidad + 1)
        } else {
            // Si es nuevo, insertamos un nuevo ItemCarrito
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

    // 4. Quita un ítem del carrito, manejando la cantidad (decrementa o elimina)
    suspend fun removeItemFromCarrito(item: ItemCarrito) {
        if (item.cantidad > 1) {
            // Si hay más de uno, decrementamos la cantidad
            carritoDao.updateCantidad(item.viniloId, item.cantidad - 1)
        } else {
            // Si solo queda uno, lo eliminamos completamente
            carritoDao.delete(item)
        }
    }

    // 5. Vacía completamente la tabla del carrito
    suspend fun clearCarrito() = carritoDao.clear()
}