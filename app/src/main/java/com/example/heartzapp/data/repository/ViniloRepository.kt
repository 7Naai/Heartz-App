package com.example.heartzapp.data.repository

import com.example.heartzapp.data.dao.ViniloDao
import com.example.heartzapp.data.model.Vinilo

class ViniloRepository(private val viniloDao: ViniloDao) {

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
}
