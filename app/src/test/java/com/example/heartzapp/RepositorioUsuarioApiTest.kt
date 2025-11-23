package com.example.heartzapp.data.api

import com.example.heartzapp.data.model.Usuario
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest

class RepositorioUsuarioApiTest : StringSpec({

    // Mock de la API
    val apiMock = mockk<UsuarioApi>()

    // Repositorio fake usando el mock
    val repo = RepositorioUsuarioApiFake(apiMock)

    "obtenerUsuarios debe retornar la lista entregada por la API" {
        val fakeUsuarios = listOf(
            Usuario("12345678-5", "Juan Pérez", "juan.perez@mail.com", "123456", "Cliente"),
            Usuario("87654321-K", "María González", "maria.gonzalez@mail.com", "123456", "Empleado")
        )

        coEvery { apiMock.getUsuarios() } returns fakeUsuarios

        runTest {
            val result = repo.obtenerUsuarios()
            result shouldBe fakeUsuarios
        }

        coVerify(exactly = 1) { apiMock.getUsuarios() }
    }

    "obtenerUsuarioPorRut debe retornar el usuario esperado" {
        val usuario = Usuario("11223344-3", "Pedro Ramírez", "pedro.ramirez@mail.com", "123456", "Cliente")

        coEvery { apiMock.getUsuarioByRut("11223344-3") } returns usuario

        runTest {
            val result = repo.obtenerUsuarioPorRut("11223344-3")
            result shouldBe usuario
        }

        coVerify { apiMock.getUsuarioByRut("11223344-3") }
    }

    "crearUsuario debe retornar el usuario creado" {
        val usuario = Usuario("44332211-9", "Ana Torres", "ana@mail.com", "123456", "Empleado")

        coEvery { apiMock.crearUsuario(usuario) } returns usuario

        runTest {
            val result = repo.crearUsuario(usuario)
            result shouldBe usuario
        }

        coVerify { apiMock.crearUsuario(usuario) }
    }

    "actualizarUsuario debe retornar el usuario actualizado" {
        val usuarioActualizado = Usuario("55667788-0", "Luis Fernández", "cliente@heartz.cl", "123456", "Cliente")

        coEvery {
            apiMock.actualizarUsuario("55667788-0", usuarioActualizado)
        } returns usuarioActualizado

        runTest {
            val result = repo.actualizarUsuario("55667788-0", usuarioActualizado)
            result shouldBe usuarioActualizado
        }

        coVerify { apiMock.actualizarUsuario("55667788-0", usuarioActualizado) }
    }

    "eliminarUsuario debe llamar a la API" {
        coEvery { apiMock.eliminarUsuario("55667788-0") } returns Unit

        runTest {
            repo.eliminarUsuario("55667788-0")
        }

        coVerify(exactly = 1) { apiMock.eliminarUsuario("55667788-0") }
    }
})
