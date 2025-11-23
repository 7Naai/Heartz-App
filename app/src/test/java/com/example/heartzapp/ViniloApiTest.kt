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

class ViniloApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: ViniloApi
    private lateinit var gson: Gson

    private val viniloSample = Vinilo(
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
        mockWebServer.start()

        gson = Gson()

        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ViniloApi::class.java)
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getViniloById retorna el vinilo y usa la ruta correcta`() = runTest {
        val json = gson.toJson(viniloSample)

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(json)
        )

        val resultado = api.getViniloById(10)

        val req = mockWebServer.takeRequest()
        assert(req.method == "GET")
        assert(req.path == "/vinilos/10") // ← RUTA EXACTA

        assert(resultado.nombre == "Thriller")
    }

    @Test
    fun `createVinilo envia POST con el cuerpo correcto`() = runTest {
        val paraEnviar = viniloSample.copy(idVin = 0)
        val jsonEnvio = gson.toJson(paraEnviar)

        val creado = viniloSample
        val jsonRespuesta = gson.toJson(creado)

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_CREATED)
                .setBody(jsonRespuesta)
        )

        val resultado = api.createVinilo(paraEnviar)

        val req = mockWebServer.takeRequest()
        assert(req.method == "POST")
        assert(req.path == "/vinilos") // ← IGUAL A TU API REAL
        assert(req.body.readUtf8() == jsonEnvio)

        assert(resultado.idVin == 10)
    }
}
