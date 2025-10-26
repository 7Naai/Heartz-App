package com.example.heartzapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.heartzapp.data.dao.ViniloDao
import com.example.heartzapp.data.dao.UsuarioDao
import com.example.heartzapp.data.model.Vinilo
import com.example.heartzapp.data.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Vinilo::class, Usuario::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun viniloDao(): ViniloDao
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "heartz_db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    seedDatabase(database)
                }
            }
        }
    }
}

private suspend fun seedDatabase(database: AppDatabase) {
    val viniloDao = database.viniloDao()
    val usuarioDao = database.usuarioDao()

    viniloDao.insert(
        Vinilo(
            idVin = 1,
            nombre = "Saturday Night Wrist",
            artista = "Deftones",
            genero = "Metal",
            anno = 2006,
            precio = 44900,
            formato = "2LP",
            colorVinilo = "Negro - Dorado",
            stock = 10,
            sello = "Maverick Records",
            pais = "EE.UU.",
            edicion = "Original",
            duracion = "61:36",
            descripcion = "El quinto álbum de Deftones mezcla metal alternativo con atmósferas etéreas, consolidando su sonido más experimental.",
            img = "saturday_night_wrist_cover"
        )
    )
    viniloDao.insert(
        Vinilo(
            idVin = 2,
            nombre = "Blonde",
            artista = "Frank Ocean",
            genero = "R&B",
            anno = 2016,
            precio = 49900,
            formato = "2LP",
            colorVinilo = "Beige",
            stock = 8,
            sello = "Boys Don't Cry",
            pais = "EE.UU.",
            edicion = "Edición Limitada",
            duracion = "60:08",
            descripcion = "Obra maestra introspectiva de Frank Ocean, fusionando R&B, soul y electrónica con letras profundamente personales.",
            img = "blonde_cover"
        )
    )
    viniloDao.insert(
        Vinilo(
            idVin = 3,
            nombre = "The Bends",
            artista = "Radiohead",
            genero = "Rock",
            anno = 1995,
            precio = 45900,
            formato = "LP",
            colorVinilo = "Negro - Blanco",
            stock = 12,
            sello = "Parlophone",
            pais = "Reino Unido",
            edicion = "Original",
            duracion = "48:37",
            descripcion = "El segundo álbum de Radiohead marca su transición hacia un sonido más emocional y alternativo con himnos inolvidables.",
            img = "the_bends_cover"
        )
    )
    viniloDao.insert(
        Vinilo(
            idVin = 4,
            nombre = "Imaginal Disk",
            artista = "Magdalena Bay",
            genero = "Electropop",
            anno = 2021,
            precio = 38900,
            formato = "LP",
            colorVinilo = "Transparente",
            stock = 7,
            sello = "Labrador",
            pais = "EE.UU.",
            edicion = "Original",
            duracion = "42:12",
            descripcion = "Electropop brillante y nostálgico de Magdalena Bay con un sonido synthpop fresco y pegadizo.",
            img = "imaginal_disk_cover"
        )
    )
    viniloDao.insert(
        Vinilo(
            idVin = 5,
            nombre = "Thriller",
            artista = "Michael Jackson",
            genero = "Pop",
            anno = 1982,
            precio = 52900,
            formato = "LP",
            colorVinilo = "Negro",
            stock = 15,
            sello = "Epic",
            pais = "EE.UU.",
            edicion = "Original",
            duracion = "42:19",
            descripcion = "El icónico álbum de Michael Jackson que revolucionó el pop y estableció récords de ventas en todo el mundo.",
            img = "thriller_cover"
        )
    )
    viniloDao.insert(
        Vinilo(
            idVin = 6,
            nombre = "The Dark Side of the Moon",
            artista = "Pink Floyd",
            genero = "Rock Progresivo",
            anno = 1973,
            precio = 59900,
            formato = "LP",
            colorVinilo = "Negro",
            stock = 9,
            sello = "Harvest Records",
            pais = "Reino Unido",
            edicion = "Original",
            duracion = "43:00",
            descripcion = "Álbum conceptual legendario de Pink Floyd, explorando temas de locura, tiempo y avaricia con un sonido atmosférico único.",
            img = "the_dark_side_of_the_moon_cover"
        )
    )
    viniloDao.insert(
        Vinilo(
            idVin = 7,
            nombre = "Whole Lotta Red",
            artista = "Playboi Carti",
            genero = "Hip Hop",
            anno = 2020,
            precio = 41900,
            formato = "LP",
            colorVinilo = "Rojo",
            stock = 6,
            sello = "AWGE / Interscope",
            pais = "EE.UU.",
            edicion = "Original",
            duracion = "52:35",
            descripcion = "Álbum de Playboi Carti caracterizado por su estilo trap experimental y energías crudas en cada pista.",
            img = "whole_lotta_red_cover"
        )
    )
    viniloDao.insert(
        Vinilo(
            idVin = 8,
            nombre = "La Espada y la Pared",
            artista = "Los Tres",
            genero = "Rock",
            anno = 1995,
            precio = 39900,
            formato = "LP",
            colorVinilo = "Negro",
            stock = 11,
            sello = "Sony",
            pais = "Chile",
            edicion = "Original",
            duracion = "49:21",
            descripcion = "Álbum emblemático de Los Tres, mezcla rock, jazz y folklore chileno con letras profundas y cargadas de sentimiento.",
            img = "la_espada_y_la_pared_cover"
        )
    )
    viniloDao.insert(
        Vinilo(
            idVin = 9,
            nombre = "Casiopea",
            artista = "Casiopea",
            genero = "Jazz Fusion",
            anno = 1985,
            precio = 45900,
            formato = "LP",
            colorVinilo = "Azul",
            stock = 5,
            sello = "Alfa Records",
            pais = "Japón",
            edicion = "Original",
            duracion = "45:50",
            descripcion = "Jazz fusion instrumental de alto nivel con virtuosismo y melodías cautivadoras, clásico del jazz japonés.",
            img = "casiopea_cover"
        )
    )

    usuarioDao.insert(
        Usuario(
            rut = "12345678-5",
            nombre = "Juan Pérez",
            correo = "juan.perez@mail.com",
            rol = "Cliente",
            fechaNacimiento = "1990-05-12"
        )
    )
    usuarioDao.insert(
        Usuario(
            rut = "87654321-K",
            nombre = "María González",
            correo = "maria.gonzalez@mail.com",
            rol = "Empleado",
            fechaNacimiento = "1985-11-23"
        )
    )
    usuarioDao.insert(
        Usuario(
            rut = "11223344-3",
            nombre = "Pedro Ramírez",
            correo = "pedro.ramirez@mail.com",
            rol = "Cliente",
            fechaNacimiento = "1992-07-01"
        )
    )
    usuarioDao.insert(
        Usuario(
            rut = "44332211-9",
            nombre = "Ana Torres",
            correo = "ana.torres@mail.com",
            rol = "Empleado",
            fechaNacimiento = "1988-02-17"
        )
    )
    usuarioDao.insert(
        Usuario(
            rut = "55667788-0",
            nombre = "Luis Fernández",
            correo = "luis.fernandez@mail.com",
            rol = "Cliente",
            fechaNacimiento = "1995-09-30"
        )
    )
}