package com.example.heartzapp.data.api

import com.example.heartzapp.data.model.Vinilo
import com.google.gson.Gson
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

// Nota: Dado que el modelo Vinilo no está en este archivo, se define una versión simple para el test.
// En un proyecto real, usarías el archivo Vinilo.kt.

/*
data class Vinilo(
    val idVin: Int,
    val nombre: String,
    val artista: String,
    val precio: Int
    // ... otros campos
)
*/


class ViniloApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: ViniloApi
    private lateinit var gson: Gson

    // Datos de prueba
    private val viniloTest = Vinilo(
        idVin = 10,
        nombre = "Thriller",
        artista = "Michael Jackson",
        genero = "Pop",
        anno = 1982,
        precio = 20000,
        formato = "LP",
        colorVinilo = "Negro",
        stock = 10,
        sello = "Epic",
        pais = "USA",
        edicion = "Original",
        duracion = "42:19",
        descripcion = "Clásico",
        img = ""
    )

    @BeforeEach
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start() // Inicia el servidor
        gson = Gson()

        // Configura Retrofit para apuntar al puerto local del MockWebServer
        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/")) // Usa la URL del servidor mock
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ViniloApi::class.java)
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown() // Detiene el servidor después de cada test
    }

    // ---------------------------------------------------------------------
    //                         PRUEBA DE OBTENER POR ID (GET)
    // ---------------------------------------------------------------------

    @Test
    fun `getViniloById envia la ruta correcta y retorna el objeto Vinilo`() = runTest {
        val idBuscado = viniloTest.idVin
        val jsonRespuesta = gson.toJson(viniloTest)

        // 1. Configurar la respuesta del Mock Server
        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK) // Código 200
            .setBody(jsonRespuesta)

        mockWebServer.enqueue(mockResponse)

        // 2. Llamar a la función del API
        val resultado = api.getViniloById(idBuscado)

        // 3. VERIFICACIONES

        // a) Verificar que la solicitud enviada al servidor sea correcta
        val solicitud = mockWebServer.takeRequest() // Captura la solicitud HTTP enviada
        assert(solicitud.method == "GET")
        // Verificar que la URL incluye el ID en la ruta
        assert(solicitud.path == "/vinilos/$idBuscado")

        // b) Verificar que la respuesta del API sea correcta
        assert(resultado.idVin == idBuscado)
        assert(resultado.nombre == "Thriller")
    }

    // ---------------------------------------------------------------------
    //                         PRUEBA DE CREACIÓN (POST)
    // ---------------------------------------------------------------------

    @Test
    fun `createVinilo envia el cuerpo correcto y el metodo POST`() = runTest {
        val viniloACrear = viniloTest.copy(idVin = 0) // Simular un objeto sin ID
        val viniloConID = viniloTest.copy(idVin = 11) // Simular el objeto retornado por el servidor
        val jsonEnvio = gson.toJson(viniloACrear)
        val jsonRespuesta = gson.toJson(viniloConID)

        // 1. Configurar la respuesta del Mock Server (simulando éxito en la creación)
        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_CREATED) // Código 201
            .setBody(jsonRespuesta)

        mockWebServer.enqueue(mockResponse)

        // 2. Llamar a la función del API
        val resultado = api.createVinilo(viniloACrear)

        // 3. VERIFICACIONES

        // a) Verificar que la solicitud enviada al servidor sea correcta
        val solicitud = mockWebServer.takeRequest()
        assert(solicitud.method == "POST")
        assert(solicitud.path == "/vinilos")

        // Verificar que el cuerpo de la solicitud enviado es el esperado
        val cuerpoEnviado = solicitud.body.readUtf8()
        assert(cuerpoEnviado == jsonEnvio)

        // b) Verificar que la respuesta del API se deserializa correctamente (ahora tiene ID 11)
        assert(resultado.idVin == 11)
    }


}