package com.example.heartzapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.heartzapp.data.dao.UsuarioDao
import com.example.heartzapp.data.model.Vinilo
import com.example.heartzapp.data.model.Usuario
import com.example.heartzapp.data.model.ItemCarrito
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Usuario::class], version = 8)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return getInstance(context)
        }

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "heartz_db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback(context.applicationContext))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(
        private val context: Context
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
                val database = getInstance(context)
                seedDatabase(database)
            }
        }
    }
}

private suspend fun seedDatabase(database: AppDatabase) {
    val usuarioDao = database.usuarioDao()



    usuarioDao.insert(
        Usuario(rut = "12345678-5", nombre = "Juan Pérez", correo = "juan.perez@mail.com", rol = "Cliente", contrasena = "123456")
    )
    usuarioDao.insert(
        Usuario(rut = "87654321-K", nombre = "María González", correo = "maria.gonzalez@mail.com", rol = "Empleado", contrasena = "123456")
    )
    usuarioDao.insert(
        Usuario(rut = "11223344-3", nombre = "Pedro Ramírez", correo = "pedro.ramirez@mail.com", rol = "Cliente", contrasena = "123456")
    )
    usuarioDao.insert(
        Usuario(rut = "44332211-9", nombre = "Ana Torres", correo = "ana.torres@mail.com", rol = "Empleado", contrasena = "123456")
    )
    usuarioDao.insert(
        Usuario(rut = "55667788-0", nombre = "Luis Fernández", correo = "cliente@heartz.cl", rol = "Cliente", contrasena = "123456")
    )
}
