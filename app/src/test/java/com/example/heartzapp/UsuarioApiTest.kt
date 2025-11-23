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
        mockWebServer.start() // Inicia el servidor
        gson = Gson()

        // Configura Retrofit para apuntar al puerto local del MockWebServer
        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/")) // Usa la URL del servidor mock
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(UsuarioApi::class.java)
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown() // Detiene el servidor después de cada test
    }

    // ---------------------------------------------------------------------
    //                         PRUEBA DE CREACIÓN (POST)
    // ---------------------------------------------------------------------

    @Test
    fun `crearUsuario envia el cuerpo correcto y retorna el usuario creado`() = runTest {
        // 1. Datos de prueba
        val nuevoUsuario = Usuario(rut = "12345678-9", nombre = "John Doe", correo = "j.doe@test.com", contrasena = "password", rol="Cliente")
        val jsonRespuesta = gson.toJson(nuevoUsuario)

        // 2. Configurar la respuesta del Mock Server
        // El servidor responderá con un código 201 (Created) y el JSON del usuario.
        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_CREATED) // Código 201
            .setBody(jsonRespuesta)

        mockWebServer.enqueue(mockResponse)

        // 3. Llamar a la función del API
        val resultado = api.crearUsuario(nuevoUsuario)

        // 4. VERIFICACIONES

        // a) Verificar que la solicitud enviada al servidor sea correcta
        val solicitud = mockWebServer.takeRequest() // Captura la solicitud HTTP enviada
        assert(solicitud.method == "POST")
        assert(solicitud.path == "/usuarios")

        // Verificar que el cuerpo de la solicitud es el esperado
        val cuerpoEnviado = solicitud.body.readUtf8()
        assert(cuerpoEnviado == jsonRespuesta)

        // b) Verificar que la respuesta del API sea correcta
        assert(resultado.rut == "12345678-9")
        assert(resultado.nombre == "John Doe")
    }

    // ---------------------------------------------------------------------
    //                         PRUEBA DE OBTENER (GET)
    // ---------------------------------------------------------------------

    @Test
    fun `getUsuarioByRut envia la ruta correcta y retorna el usuario`() = runTest {
        // 1. Datos de prueba
        val usuarioEncontrado = Usuario(rut = "11223344-5", nombre = "Jane Smith", correo = "j.smith@test.com", contrasena = "password", rol="Cliente")
        val jsonRespuesta = gson.toJson(usuarioEncontrado)
        val rutBuscado = "11223344-5"

        // 2. Configurar la respuesta del Mock Server (Código 200 OK)
        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK) // Código 200
            .setBody(jsonRespuesta)

        mockWebServer.enqueue(mockResponse)

        // 3. Llamar a la función del API
        val resultado = api.getUsuarioByRut(rutBuscado)

        // 4. VERIFICACIONES

        // a) Verificar que la solicitud enviada al servidor sea correcta
        val solicitud = mockWebServer.takeRequest()
        assert(solicitud.method == "GET")
        // Verificar que la URL incluye el parámetro de la ruta
        assert(solicitud.path == "/usuarios/$rutBuscado")

        // b) Verificar que la respuesta del API sea correcta
        assert(resultado.rut == rutBuscado)
        assert(resultado.nombre == "Jane Smith")
    }
}