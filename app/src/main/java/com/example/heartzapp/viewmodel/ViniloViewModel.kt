package com.example.heartzapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.heartzapp.data.AppDatabase
import com.example.heartzapp.data.model.ItemCarrito
import com.example.heartzapp.data.model.Vinilo
import com.example.heartzapp.data.repository.ViniloRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ViniloViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ViniloRepository

    // --- Vinilos ---
    private val _vinilos = MutableStateFlow<List<Vinilo>>(emptyList())
    val vinilos: StateFlow<List<Vinilo>> = _vinilos

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    // --- Carrito ---
    private val _carritoItems = MutableStateFlow<List<ItemCarrito>>(emptyList())
    val carritoItems: StateFlow<List<ItemCarrito>> = _carritoItems

    // Calculamos el total del carrito de forma reactiva (se actualiza automáticamente)
    val carritoTotal: StateFlow<Int> = _carritoItems.map { items ->
        items.sumOf { it.precio * it.cantidad }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Calculamos la cantidad total de ítems en el carrito para el badge de navegación
    val carritoCount: StateFlow<Int> = _carritoItems.map { items ->
        items.sumOf { it.cantidad }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)


    init {
        val database = AppDatabase.getDatabase(application)
        val viniloDao = database.viniloDao()

        val itemCarritoDao = database.itemCarritoDao()

        // Inicializamos el repositorio con ambos DAOs
        repository = ViniloRepository(viniloDao, itemCarritoDao)

        cargarVinilos()

        // Inicia la recolección de los ítems del carrito para mantener el estado sincronizado
        viewModelScope.launch {
            repository.getCarritoItems().collect { items ->
                _carritoItems.value = items
            }
        }
    }

    private fun cargarVinilos() {
        viewModelScope.launch {
            _isLoading.value = true
            _vinilos.value = repository.getAllVinilos()
            _isLoading.value = false
        }
    }

    fun obtenerViniloPorId(id: Int): Vinilo? {
        return _vinilos.value.find { it.idVin == id }
    }

    // --- Funciones de Carrito ---

    // Agrega un vinilo al carrito (usado en PantallaProductos/PantallaInicio)
    fun agregarViniloACarrito(vinilo: Vinilo) {
        viewModelScope.launch {
            repository.addItemToCarrito(vinilo)
        }
    }

    // Incrementa la cantidad de un ítem existente en la PantallaCarrito
    fun incrementarItem(item: ItemCarrito) {
        viewModelScope.launch {
            // Buscamos el objeto Vinilo completo antes de agregarlo al repositorio
            val vinilo = obtenerViniloPorId(item.viniloId)
            if (vinilo != null) {
                repository.addItemToCarrito(vinilo)
            }
        }
    }

    // Decrementa la cantidad o elimina el ítem si solo queda uno
    fun decrementarItem(item: ItemCarrito) {
        viewModelScope.launch {
            repository.removeItemFromCarrito(item)
        }
    }

    // Vacía completamente el carrito
    fun vaciarCarrito() {
        viewModelScope.launch {
            repository.clearCarrito()
        }
    }
}
