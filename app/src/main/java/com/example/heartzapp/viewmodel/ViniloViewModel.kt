package com.example.heartzapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.heartzapp.data.model.ItemCarrito
import com.example.heartzapp.data.model.Vinilo
import com.example.heartzapp.data.repository.CarritoRepository
import com.example.heartzapp.data.repository.ViniloRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ViniloViewModel(application: Application) : AndroidViewModel(application) {

    private val viniloRepository = ViniloRepository()
    private val carritoRepository = CarritoRepository()

    // VINILOS DESDE API
    private val _vinilos = MutableStateFlow<List<Vinilo>>(emptyList())
    val vinilos: StateFlow<List<Vinilo>> = _vinilos

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    // CARRITO DESDE API
    private val _carritoItems = MutableStateFlow<List<ItemCarrito>>(emptyList())
    val carritoItems: StateFlow<List<ItemCarrito>> = _carritoItems

    val carritoTotal: StateFlow<Int> = _carritoItems.map { items ->
        items.sumOf { it.precio * it.cantidad }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    init {
        cargarVinilosDesdeAPI()
        cargarCarritoDesdeAPI()
    }

    // --- VINILOS ---
    fun cargarVinilosDesdeAPI() {
        viewModelScope.launch {
            _isLoading.value = true
            _vinilos.value = viniloRepository.getAllVinilos() ?: emptyList()
            _isLoading.value = false
        }
    }

    fun getViniloById(id: Int): Vinilo? {
        return _vinilos.value.find { it.idVin == id }
    }

    // --- CARRITO API ---
    private fun cargarCarritoDesdeAPI() {
        viewModelScope.launch {
            _carritoItems.value = carritoRepository.getCarrito() ?: emptyList()
        }
    }

    fun agregarViniloACarrito(vinilo: Vinilo) {
        viewModelScope.launch {
            val item = ItemCarrito(
                id = 0,
                viniloId = vinilo.idVin,
                nombre = vinilo.nombre,
                precio = vinilo.precio,
                img = vinilo.img,
                cantidad = 1
            )
            carritoRepository.agregarItem(item)
            cargarCarritoDesdeAPI()
        }
    }

    fun incrementarItem(item: ItemCarrito) {
        viewModelScope.launch {
            val nuevo = item.copy(cantidad = item.cantidad + 1)
            carritoRepository.agregarItem(nuevo)
            cargarCarritoDesdeAPI()
        }
    }

    fun decrementarItem(item: ItemCarrito) {
        viewModelScope.launch {
            if (item.cantidad > 1) {
                val nuevo = item.copy(cantidad = item.cantidad - 1)
                carritoRepository.agregarItem(nuevo)
            } else {
                carritoRepository.eliminarItem(item.id)
            }
            cargarCarritoDesdeAPI()
        }
    }

    fun vaciarCarrito() {
        viewModelScope.launch {
            carritoRepository.vaciarCarrito()
            cargarCarritoDesdeAPI()
        }
    }
}
