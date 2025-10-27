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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.SharingStarted
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

    val carritoTotal: StateFlow<Int> = _carritoItems.map { items ->
        items.sumOf { it.precio * it.cantidad }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    init {
        val database = AppDatabase.getDatabase(application)
        val viniloDao = database.viniloDao()
        val itemCarritoDao = database.itemCarritoDao()

        repository = ViniloRepository(viniloDao, itemCarritoDao)

        cargarVinilos()

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

    fun getViniloById(id: Int): Vinilo? {
        return _vinilos.value.find { it.idVin == id }
    }

    // --- Funciones de Carrito ---
    fun agregarViniloACarrito(vinilo: Vinilo) {
        viewModelScope.launch {
            repository.addItemToCarrito(vinilo)
        }
    }

    fun incrementarItem(item: ItemCarrito) {
        viewModelScope.launch {
            val updatedItem = item.copy(cantidad = item.cantidad + 1)
            repository.updateItemCarrito(updatedItem)
        }
    }

    fun decrementarItem(item: ItemCarrito) {
        viewModelScope.launch {
            if (item.cantidad > 1) {
                val updatedItem = item.copy(cantidad = item.cantidad - 1)
                repository.updateItemCarrito(updatedItem)
            } else {
                repository.deleteItemCarrito(item)
            }
        }
    }

    fun vaciarCarrito() {
        viewModelScope.launch {
            repository.clearCarrito()
        }
    }
}
