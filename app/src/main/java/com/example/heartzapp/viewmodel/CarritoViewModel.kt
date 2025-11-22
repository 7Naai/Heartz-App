package com.example.heartzapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heartzapp.data.model.ItemCarrito
import com.example.heartzapp.data.model.Vinilo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CarritoViewModel : ViewModel() {

    private val _items = MutableStateFlow<List<ItemCarrito>>(emptyList())
    val items: StateFlow<List<ItemCarrito>> = _items

    val total: StateFlow<Int> = items.map { list ->
        list.sumOf { it.precio * it.cantidad }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    fun agregar(vinilo: Vinilo) {
        val actual = _items.value.toMutableList()

        val existente = actual.find { it.viniloId == vinilo.idVin }

        if (existente != null) {
            val actualizado = existente.copy(cantidad = existente.cantidad + 1)
            actual[actual.indexOf(existente)] = actualizado
        } else {
            actual.add(
                ItemCarrito(
                    viniloId = vinilo.idVin,
                    nombre = vinilo.nombre,
                    precio = vinilo.precio,
                    img = vinilo.img,
                    cantidad = 1
                )
            )
        }

        _items.value = actual
    }

    fun incrementar(item: ItemCarrito) {
        val lista = _items.value.toMutableList()
        val index = lista.indexOfFirst { it.viniloId == item.viniloId }
        if (index != -1) {
            lista[index] = lista[index].copy(cantidad = lista[index].cantidad + 1)
            _items.value = lista
        }
    }

    fun decrementar(item: ItemCarrito) {
        val lista = _items.value.toMutableList()
        val index = lista.indexOfFirst { it.viniloId == item.viniloId }
        if (index != -1) {
            val cantidad = lista[index].cantidad - 1
            if (cantidad <= 0) {
                lista.removeAt(index)
            } else {
                lista[index] = lista[index].copy(cantidad = cantidad)
            }
            _items.value = lista
        }
    }

    fun vaciar() {
        _items.value = emptyList()
    }
}


