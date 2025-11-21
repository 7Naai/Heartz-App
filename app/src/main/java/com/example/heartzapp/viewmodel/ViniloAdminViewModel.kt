package com.example.heartzapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.heartzapp.data.api.RepositorioVinilosApi
import com.example.heartzapp.data.model.Vinilo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Estado inicial que coincide con el modelo Vinilo.kt
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

    // --- Nuevo repositorio con Retrofit ---
    private val apiRepository = RepositorioVinilosApi()

    // --- Lista completa de vinilos ---
    private val _vinilos = MutableStateFlow<List<Vinilo>>(emptyList())
    val vinilos: StateFlow<List<Vinilo>> = _vinilos

    // --- Vinilo seleccionado para editar/crear ---
    private val _selectedVinilo = MutableStateFlow(EMPTY_VINILO)
    val selectedVinilo: StateFlow<Vinilo> = _selectedVinilo

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadVinilos()
    }

    // -----------------------------------------------------------
    // ---------------------   VINILOS API   ----------------------
    // -----------------------------------------------------------

    fun loadVinilos() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val lista = apiRepository.obtenerVinilos()
                _vinilos.value = lista
            } catch (e: Exception) {
                e.printStackTrace()
                _vinilos.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Selecciona un vinilo para edición o un vacío para crear uno nuevo.
     */
    fun selectVinilo(vinilo: Vinilo?) {
        _selectedVinilo.update { vinilo ?: EMPTY_VINILO }
    }

    /**
     * Guarda o actualiza un vinilo mediante la API.
     */
    fun saveVinilo(vinilo: Vinilo) {
        viewModelScope.launch {
            try {
                if (vinilo.idVin == 0) {
                    apiRepository.crearVinilo(vinilo.copy(idVin = 0))
                } else {
                    apiRepository.actualizarVinilo(vinilo.idVin, vinilo)
                }
                loadVinilos()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                selectVinilo(null)
            }
        }
    }

    /**
     * Elimina un vinilo mediante la API.
     */
    fun deleteVinilo(vinilo: Vinilo) {
        viewModelScope.launch {
            try {
                apiRepository.eliminarVinilo(vinilo.idVin)
                loadVinilos()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                selectVinilo(null)
            }
        }
    }

    /**
     * Actualiza el estado interno del formulario.
     */
    fun updateViniloState(vinilo: Vinilo) {
        _selectedVinilo.value = vinilo
    }
}
