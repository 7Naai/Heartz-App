package com.example.heartzapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heartzapp.data.model.ProximoProducto
import com.example.heartzapp.data.repository.DiscogsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DiscogsViewModel : ViewModel() {

    private val repository = DiscogsRepository()

    private val _productos = MutableStateFlow<List<ProximoProducto>>(emptyList())
    val productos: StateFlow<List<ProximoProducto>> = _productos

    init {
        cargarProductos()
    }

    private fun cargarProductos() {
        viewModelScope.launch {
            _productos.value = repository.getProximosProductos()
        }
    }
}
