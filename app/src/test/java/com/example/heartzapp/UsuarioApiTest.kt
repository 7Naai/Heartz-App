package com.example.heartzapp.data.api

import com.example.heartzapp.data.model.Usuario
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

class UsuarioApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: UsuarioApi
    private lateinit var gson: Gson

    @BeforeEach
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        gson = Gson()

        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/")) // IMPORTANTÍSIMO
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(UsuarioApi::class.java)
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `crearUsuario envia POST a la ruta correcta y retorna el usuario`() = runTest {
        val nuevoUsuario = Usuario("123", "John", "j@test.com", "123", "Cliente")
        val jsonRespuesta = gson.toJson(nuevoUsuario)

        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_CREATED)
            .setBody(jsonRespuesta)

        mockWebServer.enqueue(mockResponse)

        val resultado = api.crearUsuario(nuevoUsuario)

        val req = mockWebServer.takeRequest()
        assert(req.method == "POST")
        assert(req.path == "/usuarios") // ← LA RUTA REAL
        assert(req.body.readUtf8() == jsonRespuesta)

        assert(resultado.rut == "123")
    }

    @Test
    fun `getUsuarioByRut usa GET y retorna el usuario`() = runTest {
        val usuario = Usuario("45", "Ana", "a@test.com", "pass", "Cliente")
        val json = gson.toJson(usuario)

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(json)
        )

        val resultado = api.getUsuarioByRut("45")

        val req = mockWebServer.takeRequest()
        assert(req.method == "GET")
        assert(req.path == "/usuarios/45") // ← EXACTAMENTE ASÍ

        assert(resultado.nombre == "Ana")
    }
}
