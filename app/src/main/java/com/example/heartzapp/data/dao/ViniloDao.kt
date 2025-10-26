package com.example.heartzapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.heartzapp.data.model.Vinilo

@Dao
interface ViniloDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vinilo: Vinilo)

    @Query("SELECT * FROM vinilos")
    suspend fun getAllVinilos(): List<Vinilo>

    @Query("SELECT * FROM vinilos WHERE idVin = :id")
    suspend fun getViniloById(id: Int): Vinilo?

    @Query("DELETE FROM vinilos")
    suspend fun deleteAll()
}
