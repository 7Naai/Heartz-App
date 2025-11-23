package com.example.heartzapp.viewmodel

import com.example.heartzapp.data.model.ItemCarrito
import com.example.heartzapp.data.model.Vinilo
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class CarritoViewModelTesteableTest : StringSpec({

    fun viniloTest(
        id: Int = 1,
        precio: Int = 1000
    ) = Vinilo(
        idVin = id,
        nombre = "Test",
        artista = "Artista",
        genero = "Rock",
        anno = 1990,
        precio = precio,
        formato = "LP",
        colorVinilo = "Negro",
        stock = 10,
        sello = "Sony",
        pais = "Chile",
        edicion = "1ra",
        duracion = "40:00",
        descripcion = "Test",
        img = "img.jpg"
    )

    lateinit var vm: CarritoViewModelTesteable

    beforeTest {
        vm = CarritoViewModelTesteable()
    }

    "agregar agrega un item" {
        val vinilo = viniloTest()

        vm.agregar(vinilo)

        vm.items.value.size shouldBe 1
        vm.total.value shouldBe 1000
    }

    "incrementar aumenta cantidad" {
        val vinilo = viniloTest()

        vm.agregar(vinilo)
        vm.incrementar(vm.items.value.first())

        vm.items.value.first().cantidad shouldBe 2
        vm.total.value shouldBe 2000
    }

    "decrementar elimina cuando queda en 0" {
        val vinilo = viniloTest()

        vm.agregar(vinilo)
        vm.decrementar(vm.items.value.first())

        vm.items.value.size shouldBe 0
    }

    "vaciar elimina todo" {
        val vinilo = viniloTest()

        vm.agregar(vinilo)
        vm.vaciar()

        vm.items.value.size shouldBe 0
    }
})
