package com.example.heartzapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.heartzapp.data.AppDatabase
import com.example.heartzapp.data.model.Vinilo
import com.example.heartzapp.data.repository.ViniloRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Estado inicial de un Vinilo que COINCIDE con el constructor de Vinilo.kt (15 propiedades).
val EMPTY_VINILO = Vinilo(
    idVin = 0, // 0 indica nuevo elemento
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

    private val repository: ViniloRepository

    // --- Estado de la Lista de Vinilos ---
    private val _vinilos = MutableStateFlow<List<Vinilo>>(emptyList())
    val vinilos: StateFlow<List<Vinilo>> = _vinilos

    // --- Estado del Vinilo Seleccionado (para editar o crear) ---
    private val _selectedVinilo = MutableStateFlow<Vinilo>(EMPTY_VINILO)
    val selectedVinilo: StateFlow<Vinilo> = _selectedVinilo

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        // Inicializar la base de datos y el repositorio
        val database = AppDatabase.getDatabase(application)
        val viniloDao = database.viniloDao()
        val itemCarritoDao = database.itemCarritoDao()

        repository = ViniloRepository(viniloDao, itemCarritoDao)

        loadVinilos()
    }

    fun loadVinilos() {
        viewModelScope.launch {
            _isLoading.value = true
            _vinilos.update { repository.getAllVinilos() }
            _isLoading.value = false
        }
    }

    /**
     * Establece el Vinilo a editar o un Vinilo vacío para crear.
     */
    fun selectVinilo(vinilo: Vinilo?) {
        _selectedVinilo.update { vinilo ?: EMPTY_VINILO }
    }

    /**
     * Guarda o actualiza un Vinilo en la base de datos.
     */
    fun saveVinilo(vinilo: Vinilo) {
        viewModelScope.launch {
            if (vinilo.idVin == 0) {
                // Crear nuevo: insertamos.
                repository.insertVinilo(vinilo.copy(idVin = 0))
            } else {
                // Actualizar existente: actualizamos.
                repository.updateVinilo(vinilo)
            }
            loadVinilos() // Recargar la lista después de la operación
            selectVinilo(null) // Limpiar el formulario
        }
    }

    /**
     * Elimina un Vinilo de la base de datos.
     */
    fun deleteVinilo(vinilo: Vinilo) {
        viewModelScope.launch {
            repository.deleteVinilo(vinilo)
            loadVinilos() // Recargar la lista después de la eliminación
            selectVinilo(null) // Limpiar el formulario
        }
    }

    // Funciones para actualizar el estado del formulario en tiempo real
    fun updateViniloState(vinilo: Vinilo) {
        _selectedVinilo.value = vinilo
    }
}
