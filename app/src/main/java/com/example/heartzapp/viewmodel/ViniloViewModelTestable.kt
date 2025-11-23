package com.example.heartzapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.heartzapp.data.api.RepositorioVinilosApi
import com.example.heartzapp.data.model.ItemCarrito
import com.example.heartzapp.data.model.Vinilo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ViniloViewModelTestable(
    private val apiRepository: RepositorioVinilosApi
) : ViewModel() {

    private val _vinilos = MutableStateFlow<List<Vinilo>>(emptyList())
    val vinilos: StateFlow<List<Vinilo>> = _vinilos

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _carritoItems = MutableStateFlow<List<ItemCarrito>>(emptyList())
    val carritoItems: StateFlow<List<ItemCarrito>> = _carritoItems

    val carritoTotal: StateFlow<Int> = _carritoItems.map { items ->
        items.sumOf { it.precio * it.cantidad }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    suspend fun cargarVinilos() {
        try {
            _isLoading.value = true
            val lista = apiRepository.obtenerVinilos()
            _vinilos.value = lista
        } catch (e: Exception) {
            _vinilos.value = emptyList()
        } finally {
            _isLoading.value = false
        }
    }

    fun getViniloById(id: Int): Vinilo? {
        return _vinilos.value.find { it.idVin == id }
    }

    fun agregarViniloACarrito(vinilo: Vinilo) {
        val items = _carritoItems.value.toMutableList()
        val existente = items.find { it.viniloId == vinilo.idVin }

        if (existente != null) {
            items[items.indexOf(existente)] = existente.copy(cantidad = existente.cantidad + 1)
        } else {
            items.add(
                ItemCarrito(
                    id = 0,
                    viniloId = vinilo.idVin,
                    nombre = vinilo.nombre,
                    precio = vinilo.precio,
                    img = vinilo.img,
                    cantidad = 1
                )
            )
        }
        _carritoItems.value = items
    }

    fun incrementarItem(item: ItemCarrito) {
        val items = _carritoItems.value.toMutableList()
        val index = items.indexOf(item)
        if (index != -1) {
            items[index] = item.copy(cantidad = item.cantidad + 1)
        }
        _carritoItems.value = items
    }

    fun decrementarItem(item: ItemCarrito) {
        val items = _carritoItems.value.toMutableList()
        val index = items.indexOf(item)
        if (index != -1) {
            if (item.cantidad > 1) {
                items[index] = item.copy(cantidad = item.cantidad - 1)
            } else {
                items.removeAt(index)
            }
        }
        _carritoItems.value = items
    }

    fun vaciarCarrito() {
        _carritoItems.value = emptyList()
    }
}
