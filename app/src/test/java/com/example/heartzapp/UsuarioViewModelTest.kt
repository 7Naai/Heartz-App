package com.example.heartzapp.viewmodel

import com.example.heartzapp.data.api.RepositorioUsuarioApi
import com.example.heartzapp.data.model.Usuario
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class UsuarioViewModelTest : StringSpec({

    val testDispatcher = UnconfinedTestDispatcher()

    beforeTest {
        Dispatchers.setMain(testDispatcher)
    }

    afterTest {
        Dispatchers.resetMain()
    }

    "cargarUsuarios debe cargar datos del repositorio" {
        runTest {
            // Mock repositorio
            val repo = mockk<RepositorioUsuarioApi>()
            val usuariosFake = listOf(
                Usuario("11-1", "Alan", "alan@mail.com", "123456", "Cliente")
            )

            coEvery { repo.obtenerUsuarios() } returns usuariosFake

            // ViewModel con dispatcher de test
            val vm = UsuarioViewModel(repo, testDispatcher)

            advanceUntilIdle()

            vm.usuarios.value.size shouldBe 1
            vm.usuarios.value.first().nombre shouldBe "Alan"
        }
    }
})
