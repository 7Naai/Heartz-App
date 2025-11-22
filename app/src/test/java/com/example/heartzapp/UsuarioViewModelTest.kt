package com.example.heartzapp.viewmodel

import com.example.heartzapp.data.api.RepositorioUsuarioApi
import com.example.heartzapp.data.model.Usuario
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class UsuarioViewModelTest : StringSpec({

    "registrar llama al repositorio y actualiza lista" {
        val dispatcher = StandardTestDispatcher()
        val mockRepo = mockk<RepositorioUsuarioApi>()
        val usuario = Usuario("11-1", "Alan", "alan@mail.com", "123456", "Cliente")

        coJustRun { mockRepo.crearUsuario(usuario) }
        coEvery { mockRepo.obtenerUsuarios() } returns listOf(usuario)

        val vm = UsuarioViewModel(mockRepo, dispatcher)

        vm.onRutChange(usuario.rut)
        vm.onNombreChange(usuario.nombre)
        vm.onCorreoChange(usuario.correo)
        vm.onContrasenaChange(usuario.contrasena)
        vm.onAceptarTerminosChange(true)

        runTest(dispatcher) {
            val resultado = vm.registrar()
            resultado shouldBe true
            vm.usuarios.value shouldContain usuario
        }
    }

    "validarRegistro retorna false si datos incompletos" {
        val vm = UsuarioViewModel(mockk(), StandardTestDispatcher())
        vm.onRutChange("")
        vm.onNombreChange("")
        vm.onCorreoChange("correo.invalido")
        vm.onContrasenaChange("123")
        vm.onAceptarTerminosChange(false)

        vm.validarRegistro() shouldBe false
        vm.errorMensaje.value shouldBe "El RUT no puede estar vacío"
    }

    "validarLogin retorna true si correo y contraseña correctos" {
        val vm = UsuarioViewModel(mockk(), StandardTestDispatcher())
        vm.onCorreoChange("alan@mail.com")
        vm.onContrasenaChange("123456")

        vm.validarLogin() shouldBe true
        vm.errorMensaje.value shouldBe null
    }

    // ---------- NUEVAS VALIDACIONES ----------

    "validarLogin retorna false si correo vacío" {
        val vm = UsuarioViewModel(mockk(), StandardTestDispatcher())
        vm.onCorreoChange("")
        vm.onContrasenaChange("123456")

        vm.validarLogin() shouldBe false
        vm.errorMensaje.value shouldBe "Correo inválido"
    }

    "validarLogin retorna false si contraseña corta" {
        val vm = UsuarioViewModel(mockk(), StandardTestDispatcher())
        vm.onCorreoChange("test@mail.com")
        vm.onContrasenaChange("123")

        vm.validarLogin() shouldBe false
        vm.errorMensaje.value shouldBe "La contraseña debe tener mínimo 6 caracteres"
    }

    "validarRegistro retorna false si no acepta términos" {
        val vm = UsuarioViewModel(mockk(), StandardTestDispatcher())
        vm.onRutChange("11-1")
        vm.onNombreChange("Alan")
        vm.onCorreoChange("alan@mail.com")
        vm.onContrasenaChange("123456")
        vm.onAceptarTerminosChange(false)

        vm.validarRegistro() shouldBe false
        vm.errorMensaje.value shouldBe "Debes aceptar los términos"
    }

    "login retorna usuario si credenciales correctas" {
        val usuario = Usuario("11-1","Alan","alan@mail.com","123456","Cliente")
        val mockRepo = mockk<RepositorioUsuarioApi>()
        coEvery { mockRepo.obtenerUsuarios() } returns listOf(usuario)

        val vm = UsuarioViewModel(mockRepo, StandardTestDispatcher())
        vm.onCorreoChange("alan@mail.com")
        vm.onContrasenaChange("123456")

        runTest {
            val result = vm.login()
            result shouldBe usuario
        }
    }

    "login retorna null si credenciales incorrectas" {
        val usuario = Usuario("11-1","Alan","alan@mail.com","123456","Cliente")
        val mockRepo = mockk<RepositorioUsuarioApi>()
        coEvery { mockRepo.obtenerUsuarios() } returns listOf(usuario)

        val vm = UsuarioViewModel(mockRepo, StandardTestDispatcher())
        vm.onCorreoChange("alan@mail.com")
        vm.onContrasenaChange("wrongpass")

        runTest {
            val result = vm.login()
            result shouldBe null
        }
    }

    "setError actualiza errorMensaje" {
        val vm = UsuarioViewModel(mockk(), StandardTestDispatcher())
        vm.setError("Error custom")
        vm.errorMensaje.value shouldBe "Error custom"
    }

    "cargarUsuarios captura excepción" {
        val mockRepo = mockk<RepositorioUsuarioApi>()
        coEvery { mockRepo.obtenerUsuarios() } throws Exception("API fallida")

        val vm = UsuarioViewModel(mockRepo, StandardTestDispatcher())
        runTest {
            vm.cargarUsuarios()
            vm.usuarios.value shouldBe emptyList()
        }
    }

    "registrar retorna false si falla repositorio" {
        val mockRepo = mockk<RepositorioUsuarioApi>()
        coEvery { mockRepo.crearUsuario(any()) } throws Exception("API error")
        coEvery { mockRepo.obtenerUsuarios() } returns emptyList()

        val vm = UsuarioViewModel(mockRepo, StandardTestDispatcher())
        vm.onRutChange("11-1")
        vm.onNombreChange("Alan")
        vm.onCorreoChange("alan@mail.com")
        vm.onContrasenaChange("123456")
        vm.onAceptarTerminosChange(true)

        runTest {
            val resultado = vm.registrar()
            resultado shouldBe false
        }
    }

    "validarRegistro retorna false si nombre vacío" {
        val vm = UsuarioViewModel(mockk(), StandardTestDispatcher())
        vm.onRutChange("11-1")
        vm.onNombreChange("")
        vm.onCorreoChange("alan@mail.com")
        vm.onContrasenaChange("123456")
        vm.onAceptarTerminosChange(true)

        vm.validarRegistro() shouldBe false
        vm.errorMensaje.value shouldBe "El nombre es obligatorio"
    }

    "validarRegistro retorna false si correo inválido" {
        val vm = UsuarioViewModel(mockk(), StandardTestDispatcher())
        vm.onRutChange("11-1")
        vm.onNombreChange("Alan")
        vm.onCorreoChange("correo.invalido")
        vm.onContrasenaChange("123456")
        vm.onAceptarTerminosChange(true)

        vm.validarRegistro() shouldBe false
        vm.errorMensaje.value shouldBe "Correo inválido"
    }

    "validarRegistro retorna false si contraseña muy corta" {
        val vm = UsuarioViewModel(mockk(), StandardTestDispatcher())
        vm.onRutChange("11-1")
        vm.onNombreChange("Alan")
        vm.onCorreoChange("alan@mail.com")
        vm.onContrasenaChange("123")
        vm.onAceptarTerminosChange(true)

        vm.validarRegistro() shouldBe false
        vm.errorMensaje.value shouldBe "La contraseña debe tener al menos 6 caracteres"
    }

    "login es case insensitive en correo" {
        val usuario = Usuario("11-1","Alan","alan@mail.com","123456","Cliente")
        val mockRepo = mockk<RepositorioUsuarioApi>()
        coEvery { mockRepo.obtenerUsuarios() } returns listOf(usuario)

        val vm = UsuarioViewModel(mockRepo, StandardTestDispatcher())
        vm.onCorreoChange("ALAN@MAIL.COM")
        vm.onContrasenaChange("123456")

        runTest {
            val result = vm.login()
            result shouldBe usuario
        }
    }

})
