package com.example.heartzapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heartzapp.data.api.RepositorioUsuarioApi
import com.example.heartzapp.data.model.Usuario
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UsuarioViewModel(
    private val repository: RepositorioUsuarioApi,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    // ------------------ CAMPOS DEL FORMULARIO ------------------
    private val _rut = MutableStateFlow("")
    val rut: StateFlow<String> = _rut

    private val _nombre = MutableStateFlow("")
    val nombre: StateFlow<String> = _nombre

    private val _correo = MutableStateFlow("")
    val correo: StateFlow<String> = _correo

    private val _contrasena = MutableStateFlow("")
    val contrasena: StateFlow<String> = _contrasena

    private val _aceptaTerminos = MutableStateFlow(false)
    val aceptaTerminos: StateFlow<Boolean> = _aceptaTerminos

    // ------------------ ERRORES SIMPLES ------------------
    private val _errorMensaje = MutableStateFlow<String?>(null)
    val errorMensaje: StateFlow<String?> = _errorMensaje

    // ------------------ LISTA DE USUARIOS ------------------
    private val _usuarios = MutableStateFlow<List<Usuario>>(emptyList())
    val usuarios: StateFlow<List<Usuario>> = _usuarios

    init {
        cargarUsuarios()
    }

    fun cargarUsuarios() {
        viewModelScope.launch(dispatcher) {
            try {
                _usuarios.value = repository.obtenerUsuarios()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // ------------------ SETTERS ------------------
    fun onRutChange(v: String) { _rut.value = v }
    fun onNombreChange(v: String) { _nombre.value = v }
    fun onCorreoChange(v: String) { _correo.value = v }
    fun onContrasenaChange(v: String) { _contrasena.value = v }
    fun onAceptarTerminosChange(v: Boolean) { _aceptaTerminos.value = v }

    // ------------------ VALIDACIONES ------------------
    fun validarLogin(): Boolean {
        if (_correo.value.isBlank() || !_correo.value.contains("@")) {
            _errorMensaje.value = "Correo inválido"
            return false
        }
        if (_contrasena.value.length < 6) {
            _errorMensaje.value = "La contraseña debe tener mínimo 6 caracteres"
            return false
        }
        _errorMensaje.value = null
        return true
    }

    fun validarRegistro(): Boolean {
        if (_rut.value.isBlank()) {
            _errorMensaje.value = "El RUT no puede estar vacío"
            return false
        }
        if (_nombre.value.isBlank()) {
            _errorMensaje.value = "El nombre es obligatorio"
            return false
        }
        if (!_correo.value.contains("@")) {
            _errorMensaje.value = "Correo inválido"
            return false
        }
        if (_contrasena.value.length < 6) {
            _errorMensaje.value = "La contraseña debe tener al menos 6 caracteres"
            return false
        }
        if (!_aceptaTerminos.value) {
            _errorMensaje.value = "Debes aceptar los términos"
            return false
        }
        _errorMensaje.value = null
        return true
    }

    // ------------------ LOGIN API ------------------
    suspend fun login(): Usuario? {
        return try {
            val lista = repository.obtenerUsuarios()

            val correoIngresado = _correo.value.trim()
            val passIngresada = _contrasena.value.trim()

            lista.find { u ->
                u.correo.trim().equals(correoIngresado, ignoreCase = true) &&
                        u.contrasena.trim() == passIngresada
            }

        } catch (e: Exception) {
            null
        }
    }







    // ------------------ REGISTRO API ------------------
    suspend fun registrar(): Boolean {
        val nuevo = Usuario(
            rut = _rut.value,
            nombre = _nombre.value,
            correo = _correo.value,
            contrasena = _contrasena.value,
            rol = "Cliente"
        )

        return try {
            repository.crearUsuario(nuevo)
            cargarUsuarios()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun setError(msg: String) {
        _errorMensaje.value = msg
    }

}
