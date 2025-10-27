package com.example.heartzapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heartzapp.data.model.Usuario
import com.example.heartzapp.data.model.UsuarioErrores
import com.example.heartzapp.data.model.UsuarioUiState
import com.example.heartzapp.data.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UsuarioViewModel(private val repository: UsuarioRepository) : ViewModel() {

    // --- Estado del formulario ---
    private val _estado = MutableStateFlow(UsuarioUiState())
    val estado: StateFlow<UsuarioUiState> = _estado

    // --- Lista de usuarios ---
    private val _usuarios = MutableStateFlow<List<Usuario>>(emptyList())
    val usuarios: StateFlow<List<Usuario>> = _usuarios

    init {
        cargarUsuarios()
    }

    private fun cargarUsuarios() {
        viewModelScope.launch {
            val lista = repository.getAllUsuarios() // Recupera la lista real de la BDD
            _usuarios.value = lista
        }
    }

    // --- Funciones para actualizar campos del formulario ---
    fun onRutChange(valor: String) {
        _estado.update { it.copy(rut = valor, errores = it.errores.copy(rut = null)) }
    }

    fun onNombreChange(valor: String) {
        _estado.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }
    }

    fun onCorreoChange(valor: String) {
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }

    fun onContrasenaChange(valor: String) {
        _estado.update { it.copy(contrasena = valor, errores = it.errores.copy(contrasena = null)) }
    }

    fun onAceptarTerminosChange(valor: Boolean) {
        _estado.update { it.copy(aceptaTerminos = valor) }
    }

    // --- Validaciones ---
    fun validarFormulario(): Boolean {
        val estadoActual = _estado.value
        val errores = UsuarioErrores(
            rut = if (estadoActual.rut.isBlank()) "Campo obligatorio"
            else if (!validarRut(estadoActual.rut)) "RUT inválido (ej: 12345678-9)"
            else null,
            nombre = if (estadoActual.nombre.isBlank()) "Campo obligatorio" else null,
            correo = if (estadoActual.correo.isBlank()) "Campo obligatorio"
            else if (!validarCorreo(estadoActual.correo)) "Correo inválido"
            else null,
            contrasena = if (estadoActual.contrasena.length < 6) "Debe tener al menos 6 caracteres" else null
        )
        val hayErrores = listOfNotNull(
            errores.rut,
            errores.nombre,
            errores.correo,
            errores.contrasena
        ).isNotEmpty()
        _estado.update { it.copy(errores = errores) }
        return !hayErrores
    }

    fun validarLogin(): Boolean {
        val estadoActual = _estado.value
        val errores = UsuarioErrores(
            correo = if (estadoActual.correo.isBlank() || !estadoActual.correo.contains("@"))
                "Correo inválido" else null,
            contrasena = if (estadoActual.contrasena.length < 6)
                "Debe tener al menos 6 caracteres" else null
        )
        val hayErrores = listOfNotNull(
            errores.correo,
            errores.contrasena
        ).isNotEmpty()
        _estado.update { it.copy(errores = errores) }
        return !hayErrores
    }

    // --- Validaciones privadas ---
    private fun validarRut(rut: String): Boolean {
        val regex = Regex("""^\d{7,8}-[\dkK]$""")
        return regex.matches(rut)
    }

    private fun validarCorreo(correo: String): Boolean {
        val regex = Regex("""^[\w\.-]+@[\w\.-]+\.\w+$""")
        return regex.matches(correo)
    }
}
