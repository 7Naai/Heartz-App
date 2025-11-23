package com.example.heartzapp.viewmodel

import com.example.heartzapp.data.api.RepositorioVinilosApi
import com.example.heartzapp.data.model.ItemCarrito
import com.example.heartzapp.data.model.Vinilo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class ViniloViewModelTest {

    private val repo = mockk<RepositorioVinilosApi>()
    private lateinit var viewModel: ViniloViewModelTestable
    private val dispatcher = UnconfinedTestDispatcher()

    private val ejemploVinilo = Vinilo(
        idVin = 1,
        nombre = "The Wall",
        artista = "Pink Floyd",
        genero = "Rock",
        anno = 1979,
        precio = 15000,
        formato = "LP",
        colorVinilo = "Negro",
        stock = 5,
        sello = "EMI",
        pais = "UK",
        edicion = "Original",
        duracion = "83:00",
        descripcion = "Álbum clásico",
        img = "thewall.jpg"
    )

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = ViniloViewModelTestable(repo)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // -----------------------------------------------------------
    //                TEST cargarVinilos()
    // -----------------------------------------------------------
    @Test
    fun `cargarVinilos actualiza lista correctamente`() = runTest {
        coEvery { repo.obtenerVinilos() } returns listOf(ejemploVinilo)

        viewModel.cargarVinilos()

        assertEquals(1, viewModel.vinilos.value.size)
        assertEquals("The Wall", viewModel.vinilos.value.first().nombre)
        assertEquals(false, viewModel.isLoading.value)
    }

    @Test
    fun `cargarVinilos vacia lista si falla`() = runTest {
        coEvery { repo.obtenerVinilos() } throws Exception("API caída")

        viewModel.cargarVinilos()

        assertTrue(viewModel.vinilos.value.isEmpty())
    }

    // -----------------------------------------------------------
    //                TEST getViniloById()
    // -----------------------------------------------------------
    @Test
    fun `getViniloById devuelve vinilo correcto`() = runTest {
        coEvery { repo.obtenerVinilos() } returns listOf(ejemploVinilo)
        viewModel.cargarVinilos()

        val resultado = viewModel.getViniloById(1)

        assertEquals(ejemploVinilo, resultado)
    }

    @Test
    fun `getViniloById devuelve null si no existe`() = runTest {
        coEvery { repo.obtenerVinilos() } returns emptyList()
        viewModel.cargarVinilos()

        val resultado = viewModel.getViniloById(99)

        assertNull(resultado)
    }

    // -----------------------------------------------------------
    //                TEST agregarViniloACarrito()
    // -----------------------------------------------------------
    @Test
    fun `agregarViniloACarrito agrega un nuevo item`() = runTest {
        viewModel.agregarViniloACarrito(ejemploVinilo)

        val items = viewModel.carritoItems.value
        assertEquals(1, items.size)
        assertEquals(1, items.first().cantidad)
        assertEquals(ejemploVinilo.idVin, items.first().viniloId)
    }

    @Test
    fun `agregarViniloACarrito aumenta cantidad si ya existe`() = runTest {
        viewModel.agregarViniloACarrito(ejemploVinilo)
        viewModel.agregarViniloACarrito(ejemploVinilo)

        val items = viewModel.carritoItems.value
        assertEquals(1, items.size)
        assertEquals(2, items.first().cantidad)
    }

    // -----------------------------------------------------------
    //                 TEST incrementarItem()
    // -----------------------------------------------------------
    @Test
    fun `incrementarItem aumenta cantidad`() = runTest {
        viewModel.agregarViniloACarrito(ejemploVinilo)
        val item = viewModel.carritoItems.value.first()

        viewModel.incrementarItem(item)

        assertEquals(2, viewModel.carritoItems.value.first().cantidad)
    }

    // -----------------------------------------------------------
    //                 TEST decrementarItem()
    // -----------------------------------------------------------
    @Test
    fun `decrementarItem baja cantidad`() = runTest {
        viewModel.agregarViniloACarrito(ejemploVinilo)
        val item = viewModel.carritoItems.value.first()

        viewModel.incrementarItem(item) // cantidad = 2

        viewModel.decrementarItem(viewModel.carritoItems.value.first())

        assertEquals(1, viewModel.carritoItems.value.first().cantidad)
    }

    @Test
    fun `decrementarItem elimina item si queda en 0`() = runTest {
        viewModel.agregarViniloACarrito(ejemploVinilo)
        val item = viewModel.carritoItems.value.first()

        viewModel.decrementarItem(item)

        assertTrue(viewModel.carritoItems.value.isEmpty())
    }

    // -----------------------------------------------------------
    //                 TEST vaciarCarrito()
    // -----------------------------------------------------------
    @Test
    fun `vaciarCarrito limpia el carrito`() = runTest {
        viewModel.agregarViniloACarrito(ejemploVinilo)
        viewModel.agregarViniloACarrito(ejemploVinilo)

        viewModel.vaciarCarrito()

        assertTrue(viewModel.carritoItems.value.isEmpty())
    }


}
