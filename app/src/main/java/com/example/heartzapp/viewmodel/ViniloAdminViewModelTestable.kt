package com.example.heartzapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heartzapp.data.api.RepositorioVinilosApi
import com.example.heartzapp.data.model.Vinilo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ViniloAdminViewModelTestable(
    private val apiRepository: RepositorioVinilosApi,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _vinilos = MutableStateFlow<List<Vinilo>>(emptyList())
    val vinilos: StateFlow<List<Vinilo>> = _vinilos

    private val _selectedVinilo = MutableStateFlow(EMPTY_VINILO)
    val selectedVinilo: StateFlow<Vinilo> = _selectedVinilo

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadVinilos() {
        viewModelScope.launch(dispatcher) {
            _isLoading.value = true
            try {
                val lista = apiRepository.obtenerVinilos()
                _vinilos.value = lista
            } catch (e: Exception) {
                _vinilos.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectVinilo(vinilo: Vinilo?) {
        _selectedVinilo.update { vinilo ?: EMPTY_VINILO }
    }

    fun saveVinilo(vinilo: Vinilo) {
        viewModelScope.launch(dispatcher) {
            try {
                if (vinilo.idVin == 0) {
                    apiRepository.crearVinilo(vinilo.copy(idVin = 0))
                } else {
                    apiRepository.actualizarVinilo(vinilo.idVin, vinilo)
                }
                loadVinilos()
            } finally {
                selectVinilo(null)
            }
        }
    }

    fun deleteVinilo(vinilo: Vinilo) {
        viewModelScope.launch(dispatcher) {
            try {
                apiRepository.eliminarVinilo(vinilo.idVin)
                loadVinilos()
            } finally {
                selectVinilo(null)
            }
        }
    }

    fun updateViniloState(vinilo: Vinilo) {
        _selectedVinilo.value = vinilo
    }
}
