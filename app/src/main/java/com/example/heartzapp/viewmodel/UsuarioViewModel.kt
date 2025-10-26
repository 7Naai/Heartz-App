package com.example.heartzapp.viewmodel


import androidx.lifecycle.ViewModel
import com.example.heartzapp.data.model.UsuarioErrores
import com.example.heartzapp.data.model.UsuarioUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class UsuarioViewModel : ViewModel(){
    private val _estado = MutableStateFlow(value = UsuarioUiState())
    val estado: StateFlow<UsuarioUiState> = _estado

    fun onRutChange(valor: String) {
        _estado.update { it.copy(rut = valor, errores = it.errores.copy(rut = null)) }
    }

    fun onNombreChange(valor: String) {
        _estado.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }
    }

    // Actualiza el campo correo
    fun onCorreoChange(valor: String) {
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }

    fun onContrasenaChange(valor: String) {
        _estado.update { it.copy(contrasena = valor, errores = it.errores.copy(contrasena = null)) }
    }

    // Actualiza checkbox de aceptación
    fun onAceptarTerminosChange(valor: Boolean) {
        _estado.update { it.copy(aceptaTerminos = valor) }
    }

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
            errores.contrasena,
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

    // --- FUNCIONES PRIVADAS DE VALIDACIÓN ---
    private fun validarRut(rut: String): Boolean {
        // RUT chileno: 7-8 dígitos + guion + dígito verificador (0-9 o K)
        val regex = Regex("""^\d{7,8}-[\dkK]$""")
        return regex.matches(rut)
    }

    private fun validarCorreo(correo: String): Boolean {
        // Correo válido básico: algo@dominio.com
        val regex = Regex("""^[\w\.-]+@[\w\.-]+\.\w+$""")
        return regex.matches(correo)
    }

}