package com.example.heartzapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.heartzapp.data.AppDatabase
import com.example.heartzapp.data.model.Vinilo
import com.example.heartzapp.data.repository.ViniloRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ViniloViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ViniloRepository

    private val _vinilos = MutableStateFlow<List<Vinilo>>(emptyList())
    val vinilos: StateFlow<List<Vinilo>> = _vinilos

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        val viniloDao = AppDatabase.getDatabase(application).viniloDao()
        repository = ViniloRepository(viniloDao)
        cargarVinilos()
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
}