package com.example.heartzapp.ui.screens.administrador

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.heartzapp.data.model.Vinilo
import com.example.heartzapp.ui.components.BotonVolver
import com.example.heartzapp.viewmodel.ViniloAdminViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAdminVinilo(
    navController: NavController,
    viewModel: ViniloAdminViewModel = viewModel()
) {
    // --- Lógica del Estado (sin cambios) ---
    val vinilos by viewModel.vinilos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val selectedVinilo by viewModel.selectedVinilo.collectAsState()

    val isFormVisible = remember { mutableStateOf(false) }
    val isEditing = selectedVinilo.idVin != 0

    LaunchedEffect(selectedVinilo) {
        if (selectedVinilo.idVin != 0 || selectedVinilo.nombre.isNotEmpty()) {
            isFormVisible.value = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Administración de Vinilos", color = Color.White, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF8E24AA)),
                navigationIcon = {
                    BotonVolver(navController)
                },
                actions = {
                    Button(
                        onClick = {
                            viewModel.selectVinilo(null) // Carga un vinilo vacío para crear
                            isFormVisible.value = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD5B6F2)),
                        contentPadding = PaddingValues(horizontal = 12.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Crear nuevo", tint = Color.Black)
                        Spacer(Modifier.width(4.dp))
                        Text("Nuevo", color = Color.Black)
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8F8F8))
        ) {
            // 1. Lista de Vinilos
            if (isLoading) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            } else if (vinilos.isEmpty()) {
                Text(
                    text = "No hay vinilos en la base de datos.",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Gray
                )
            } else {
                LazyColumn(contentPadding = PaddingValues(16.dp)) {
                    items(vinilos, key = { it.idVin }) { vinilo ->
                        ViniloAdminItem(
                            vinilo = vinilo,
                            onEditClick = {
                                viewModel.selectVinilo(it)
                            },
                            onDeleteClick = {
                                viewModel.deleteVinilo(it)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            // 2. Formulario de Edición/Creación (Modal Inferior)
            AnimatedVisibility(
                visible = isFormVisible.value,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                ViniloFormulario(
                    vinilo = selectedVinilo,
                    onSave = {
                        viewModel.saveVinilo(it)
                        isFormVisible.value = false
                    },
                    onCancel = {
                        viewModel.selectVinilo(null)
                        isFormVisible.value = false
                    },
                    isEditing = isEditing,
                    onValueChange = viewModel::updateViniloState
                )
            }
        }
    }
}

@Composable
fun ViniloAdminItem(
    vinilo: Vinilo,
    onEditClick: (Vinilo) -> Unit,
    onDeleteClick: (Vinilo) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEditClick(vinilo) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ID del Vinilo y Título
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "ID: ${vinilo.idVin}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = vinilo.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF6A1B9A)
                )
                Text(
                    text = "Artista: ${vinilo.artista}",
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Text(
                    text = "Precio: $${vinilo.precio}, Stock: ${vinilo.stock}",
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }

            // Botón Editar
            IconButton(onClick = { onEditClick(vinilo) }) {
                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color(0xFF9C27B0))
            }

            // Botón Eliminar
            IconButton(onClick = { onDeleteClick(vinilo) }) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color(0xFFE57373))
            }
        }
    }
}

@Composable
fun ViniloFormulario(
    vinilo: Vinilo,
    onSave: (Vinilo) -> Unit,
    onCancel: () -> Unit,
    isEditing: Boolean,
    onValueChange: (Vinilo) -> Unit
) {
    var dialogOpen by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        color = Color.White,
        shadowElevation = 16.dp
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isEditing) "Editar Vinilo (ID: ${vinilo.idVin})" else "Crear Nuevo Vinilo",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp),
                color = Color(0xFF6A1B9A)
            )

            // --- Formulario de Campos ---

            OutlinedTextField(
                value = vinilo.nombre,
                onValueChange = { onValueChange(vinilo.copy(nombre = it)) },
                label = { Text("Nombre del Álbum") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                singleLine = true
            )
            OutlinedTextField(
                value = vinilo.artista,
                onValueChange = { onValueChange(vinilo.copy(artista = it)) },
                label = { Text("Artista") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                singleLine = true
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = if (vinilo.precio == 0) "" else vinilo.precio.toString(),
                    onValueChange = { onValueChange(vinilo.copy(precio = it.toIntOrNull() ?: 0)) },
                    label = { Text("Precio ($)") },
                    // CORRECCIÓN 1: Sustituir vertical por top y bottom.
                    modifier = Modifier.padding(end = 8.dp, top = 4.dp, bottom = 4.dp).weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = if (vinilo.stock == 0) "" else vinilo.stock.toString(),
                    onValueChange = { onValueChange(vinilo.copy(stock = it.toIntOrNull() ?: 0)) },
                    label = { Text("Stock") },
                    // CORRECCIÓN 2: Sustituir vertical por top y bottom.
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp, bottom = 4.dp).weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            OutlinedTextField(
                value = vinilo.genero,
                onValueChange = { onValueChange(vinilo.copy(genero = it)) },
                label = { Text("Género") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                singleLine = true
            )
            OutlinedTextField(
                value = vinilo.img,
                onValueChange = { onValueChange(vinilo.copy(img = it)) },
                label = { Text("URL Imagen") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                singleLine = true
            )
            OutlinedTextField(
                value = vinilo.descripcion,
                onValueChange = { onValueChange(vinilo.copy(descripcion = it)) },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- Botones de Acción ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onCancel,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar", color = Color.Black)
                }
                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        if (isEditing) {
                            dialogOpen = true // Muestra confirmación al editar
                        } else {
                            onSave(vinilo) // Guarda inmediatamente al crear
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (isEditing) "Guardar Cambios" else "Crear Vinilo")
                }
            }

            // Diálogo de Confirmación (solo para Edición/Actualización)
            if (dialogOpen) {
                AlertDialog(
                    onDismissRequest = { dialogOpen = false },
                    title = { Text("Confirmar Guardado") },
                    text = { Text("¿Estás seguro de que quieres guardar los cambios en el vinilo \"${vinilo.nombre}\" (ID: ${vinilo.idVin})?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                onSave(vinilo)
                                dialogOpen = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A))
                        ) {
                            Text("Confirmar")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { dialogOpen = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}