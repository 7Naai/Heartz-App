package com.example.heartzapp.viewmodel

import com.example.heartzapp.data.api.RepositorioVinilosApi
import com.example.heartzapp.data.model.Vinilo
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class ViniloAdminViewModelTest : StringSpec({

    val dispatcher = StandardTestDispatcher()
    val repo = mockk<RepositorioVinilosApi>(relaxed = true)

    lateinit var vm: ViniloAdminViewModelTestable

    beforeTest {
        Dispatchers.setMain(dispatcher)
        vm = ViniloAdminViewModelTestable(repo, dispatcher)
    }

    afterTest {
        Dispatchers.resetMain()
    }

    // -------------------------------------------------------------
    // TEST 1: loadVinilos carga correctamente desde API
    // -------------------------------------------------------------
    "loadVinilos carga vinilos desde API" {
        runTest {
            val listaFake = listOf(
                Vinilo(
                    idVin = 1,
                    nombre = "Album Test",
                    artista = "Artista",
                    genero = "Rock",
                    anno = 2020,
                    precio = 9990,
                    formato = "LP",
                    colorVinilo = "Negro",
                    stock = 5,
                    sello = "Sony",
                    pais = "Chile",
                    edicion = "Normal",
                    duracion = "40:00",
                    descripcion = "Test",
                    img = "test.jpg"
                )
            )

            coEvery { repo.obtenerVinilos() } returns listaFake

            vm.loadVinilos()
            dispatcher.scheduler.advanceUntilIdle()

            vm.vinilos.value shouldBe listaFake
            vm.isLoading.value shouldBe false
        }
    }

    // -------------------------------------------------------------
    // TEST 2: selectVinilo cambia el seleccionado
    // -------------------------------------------------------------
    "selectVinilo asigna vinilo correctamente" {
        val vinilo = EMPTY_VINILO.copy(idVin = 2, nombre = "Otro")

        vm.selectVinilo(vinilo)

        vm.selectedVinilo.value shouldBe vinilo
    }

    // -------------------------------------------------------------
    // TEST 3: saveVinilo crea vinilo nuevo si es id 0
    // -------------------------------------------------------------
    "saveVinilo crea nuevo si id=0" {
        runTest {
            val nuevo = EMPTY_VINILO.copy(nombre = "Nuevo")

            vm.saveVinilo(nuevo)
            dispatcher.scheduler.advanceUntilIdle()

            coVerify { repo.crearVinilo(any()) }
        }
    }

    // -------------------------------------------------------------
    // TEST 4: saveVinilo actualiza vinilo existente
    // -------------------------------------------------------------
    "saveVinilo actualiza si id > 0" {
        runTest {
            val existente = EMPTY_VINILO.copy(idVin = 10, nombre = "Editado")

            vm.saveVinilo(existente)
            dispatcher.scheduler.advanceUntilIdle()

            coVerify { repo.actualizarVinilo(10, existente) }
        }
    }

    // -------------------------------------------------------------
    // TEST 5: deleteVinilo llama API correctamente
    // -------------------------------------------------------------
    "deleteVinilo elimina vinilo en API" {
        runTest {
            val vin = EMPTY_VINILO.copy(idVin = 77)

            vm.deleteVinilo(vin)
            dispatcher.scheduler.advanceUntilIdle()

            coVerify { repo.eliminarVinilo(77) }
        }
    }

    // -------------------------------------------------------------
    // TEST 6: updateViniloState cambia el state del formulario
    // -------------------------------------------------------------
    "updateViniloState actualiza selectedVinilo" {
        val mod = EMPTY_VINILO.copy(nombre = "Modificado")

        vm.updateViniloState(mod)

        vm.selectedVinilo.value shouldBe mod
    }
})
