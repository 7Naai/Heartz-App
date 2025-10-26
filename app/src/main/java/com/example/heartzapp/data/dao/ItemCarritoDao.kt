package com.example.heartzapp.data.dao

import androidx.room.*
import com.example.heartzapp.data.model.ItemCarrito
import kotlinx.coroutines.flow.Flow

@Dao // <-- Anotación crucial
interface ItemCarritoDao {

    @Query("SELECT * FROM carrito")
    fun getAllItems(): Flow<List<ItemCarrito>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ItemCarrito)

    @Query("UPDATE carrito SET cantidad = :nuevaCantidad WHERE viniloId = :viniloId")
    suspend fun updateCantidad(viniloId: Int, nuevaCantidad: Int)

    @Query("SELECT * FROM carrito WHERE viniloId = :viniloId LIMIT 1")
    suspend fun getItemByViniloId(viniloId: Int): ItemCarrito?

    @Delete
    suspend fun delete(item: ItemCarrito)

    @Query("DELETE FROM carrito")
    suspend fun clear()
}