package com.example.heartzapp.viewmodel

import com.example.heartzapp.data.api.RepositorioUsuarioApi
import com.example.heartzapp.data.model.Usuario
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.assertFalse

@OptIn(ExperimentalCoroutinesApi::class)
class UsuarioViewModelTest {

    private val repository = mockk<RepositorioUsuarioApi>()
    private lateinit var viewModel: UsuarioViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    private val usuarioEjemplo = Usuario(
        rut = "12345678-9",
        nombre = "Alan",
        correo = "alan@mail.com",
        contrasena = "123456",
        rol = "Cliente"
    )

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = UsuarioViewModel(repository)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ------------------- VALIDACIONES -------------------

    @Test
    fun `validarLogin correcto`() {
        viewModel.onCorreoChange("alan@mail.com")
        viewModel.onContrasenaChange("123456")
        assertTrue(viewModel.validarLogin())
        assertNull(viewModel.errorMensaje.value)
    }

    @Test
    fun `validarLogin correo inválido`() {
        viewModel.onCorreoChange("correo.invalido")
        viewModel.onContrasenaChange("123456")
        assertFalse(viewModel.validarLogin())
        assertEquals("Correo inválido", viewModel.errorMensaje.value)
    }

    @Test
    fun `validarLogin contraseña corta`() {
        viewModel.onCorreoChange("alan@mail.com")
        viewModel.onContrasenaChange("123")
        assertFalse(viewModel.validarLogin())
        assertEquals("La contraseña debe tener mínimo 6 caracteres", viewModel.errorMensaje.value)
    }

    @Test
    fun `validarRegistro datos completos`() {
        viewModel.onRutChange("12345678-9")
        viewModel.onNombreChange("Alan")
        viewModel.onCorreoChange("alan@mail.com")
        viewModel.onContrasenaChange("123456")
        viewModel.onAceptarTerminosChange(true)
        assertTrue(viewModel.validarRegistro())
        assertNull(viewModel.errorMensaje.value)
    }

    @Test
    fun `validarRegistro falla por datos incompletos`() {
        viewModel.onRutChange("")
        viewModel.onNombreChange("")
        viewModel.onCorreoChange("correo.invalido")
        viewModel.onContrasenaChange("123")
        viewModel.onAceptarTerminosChange(false)

        assertFalse(viewModel.validarRegistro())
        assertEquals("El RUT no puede estar vacío", viewModel.errorMensaje.value)
    }

    @Test
    fun `validarRegistro falla por nombre vacío`() {
        viewModel.onRutChange("12345678-9")
        viewModel.onNombreChange("")
        viewModel.onCorreoChange("alan@mail.com")
        viewModel.onContrasenaChange("123456")
        viewModel.onAceptarTerminosChange(true)

        assertFalse(viewModel.validarRegistro())
        assertEquals("El nombre es obligatorio", viewModel.errorMensaje.value)
    }

    @Test
    fun `validarRegistro falla por correo inválido`() {
        viewModel.onRutChange("12345678-9")
        viewModel.onNombreChange("Alan")
        viewModel.onCorreoChange("correo.invalido")
        viewModel.onContrasenaChange("123456")
        viewModel.onAceptarTerminosChange(true)

        assertFalse(viewModel.validarRegistro())
        assertEquals("Correo inválido", viewModel.errorMensaje.value)
    }

    @Test
    fun `validarRegistro falla por contraseña corta`() {
        viewModel.onRutChange("12345678-9")
        viewModel.onNombreChange("Alan")
        viewModel.onCorreoChange("alan@mail.com")
        viewModel.onContrasenaChange("123")
        viewModel.onAceptarTerminosChange(true)

        assertFalse(viewModel.validarRegistro())
        assertEquals("La contraseña debe tener al menos 6 caracteres", viewModel.errorMensaje.value)
    }

    @Test
    fun `validarRegistro falla si no acepta terminos`() {
        viewModel.onRutChange("12345678-9")
        viewModel.onNombreChange("Alan")
        viewModel.onCorreoChange("alan@mail.com")
        viewModel.onContrasenaChange("123456")
        viewModel.onAceptarTerminosChange(false)

        assertFalse(viewModel.validarRegistro())
        assertEquals("Debes aceptar los términos", viewModel.errorMensaje.value)
    }

    // ------------------- FUNCIONES SUSPEND -------------------

    @Test
    fun `login retorna usuario correcto`() = runTest {
        coEvery { repository.obtenerUsuarios() } returns listOf(usuarioEjemplo)
        viewModel.onCorreoChange("alan@mail.com")
        viewModel.onContrasenaChange("123456")
        val resultado = viewModel.login()
        assertEquals(usuarioEjemplo, resultado)
    }

    @Test
    fun `login retorna null si no coincide usuario`() = runTest {
        coEvery { repository.obtenerUsuarios() } returns listOf(usuarioEjemplo)
        viewModel.onCorreoChange("otro@mail.com")
        viewModel.onContrasenaChange("654321")
        val resultado = viewModel.login()
        assertNull(resultado)
    }

    @Test
    fun `login es case-insensitive`() = runTest {
        coEvery { repository.obtenerUsuarios() } returns listOf(usuarioEjemplo)
        viewModel.onCorreoChange("ALAN@MAIL.COM")
        viewModel.onContrasenaChange("123456")
        val resultado = viewModel.login()
        assertEquals(usuarioEjemplo, resultado)
    }

    @Test
    fun `registrar llama crearUsuario y recarga usuarios`() = runTest {
        coJustRun { repository.crearUsuario(usuarioEjemplo) }
        coEvery { repository.obtenerUsuarios() } returns listOf(usuarioEjemplo)

        viewModel.onRutChange(usuarioEjemplo.rut)
        viewModel.onNombreChange(usuarioEjemplo.nombre)
        viewModel.onCorreoChange(usuarioEjemplo.correo)
        viewModel.onContrasenaChange(usuarioEjemplo.contrasena)
        viewModel.onAceptarTerminosChange(true)

        val resultado = viewModel.registrar()
        assertTrue(resultado)
        assertEquals(listOf(usuarioEjemplo), viewModel.usuarios.value)
    }

    @Test
    fun `registrar retorna false si falla crearUsuario`() = runTest {
        coEvery { repository.crearUsuario(any()) } throws Exception("Error")
        val resultado = viewModel.registrar()
        assertFalse(resultado)
    }

    @Test
    fun `cargarUsuarios captura excepción`() = runTest {
        coEvery { repository.obtenerUsuarios() } throws Exception("API fallida")
        viewModel.cargarUsuarios()
        assertTrue(viewModel.usuarios.value.isEmpty())
    }

    @Test
    fun `setError actualiza errorMensaje`() {
        viewModel.setError("Error custom")
        assertEquals("Error custom", viewModel.errorMensaje.value)
    }
}
