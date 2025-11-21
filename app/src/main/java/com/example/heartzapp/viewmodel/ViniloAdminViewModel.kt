package com.example.heartzapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.heartzapp.data.model.Vinilo
import com.example.heartzapp.data.repository.ViniloRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Vinilo vacío para formularios
val EMPTY_VINILO = Vinilo(
    idVin = 0,
    nombre = "",
    artista = "",
    genero = "",
    anno = 0,
    precio = 0,
    formato = "",
    colorVinilo = "",
    stock = 0,
    sello = "",
    pais = "",
    edicion = "",
    duracion = "",
    descripcion = "",
    img = ""
)

class ViniloAdminViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ViniloRepository()

    // Lista de vinilos
    private val _vinilos = MutableStateFlow<List<Vinilo>>(emptyList())
    val vinilos: StateFlow<List<Vinilo>> = _vinilos

    // Vinilo seleccionado para editar/crear
    private val _selectedVinilo = MutableStateFlow(EMPTY_VINILO)
    val selectedVinilo: StateFlow<Vinilo> = _selectedVinilo

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadVinilos()
    }

    // --- Cargar vinilos desde la API ---
    fun loadVinilos() {
        viewModelScope.launch {
            _isLoading.value = true
            val data = repository.getAllVinilos()
            _vinilos.value = data ?: emptyList()
            _isLoading.value = false
        }
    }

    // Seleccionar un vinilo o limpiar para crear uno nuevo
    fun selectVinilo(vinilo: Vinilo?) {
        _selectedVinilo.value = vinilo ?: EMPTY_VINILO
    }

    // Guardar o actualizar vinilo
    fun saveVinilo(vinilo: Vinilo) {
        viewModelScope.launch {
            if (vinilo.idVin == 0) {
                // Crear nuevo → POST
                repository.insertVinilo(vinilo)
            } else {
                // Actualizar → PUT
                repository.updateVinilo(vinilo)
            }

            loadVinilos()
            selectVinilo(null)
        }
    }

    // Eliminar vinilo por ID
    fun deleteVinilo(vinilo: Vinilo) {
        viewModelScope.launch {
            repository.deleteVinilo(vinilo)
            loadVinilos()
            selectVinilo(null)
        }
    }

    // Actualizar campos del formulario
    fun updateViniloState(vinilo: Vinilo) {
        _selectedVinilo.value = vinilo
    }
}
