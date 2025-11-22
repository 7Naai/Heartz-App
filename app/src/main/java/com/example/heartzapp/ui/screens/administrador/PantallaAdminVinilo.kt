package com.example.heartzapp.ui.screens.administrador

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.heartzapp.data.model.Vinilo
import com.example.heartzapp.ui.components.BotonVolver
import com.example.heartzapp.viewmodel.ViniloAdminViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAdminVinilo(
    navController: NavController,
    viewModel: ViniloAdminViewModel = viewModel()
) {
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
                navigationIcon = { BotonVolver(navController) },
                actions = {
                    Button(
                        onClick = {
                            viewModel.selectVinilo(null)
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
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8F8F8))
        ) {

            if (isLoading) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            } else if (vinilos.isEmpty()) {
                Text(
                    "No hay vinilos en la base de datos.",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Gray
                )
            } else {
                LazyColumn(contentPadding = PaddingValues(16.dp)) {
                    items(vinilos, key = { it.idVin }) { vinilo ->
                        ViniloAdminItem(
                            vinilo = vinilo,
                            onEditClick = { viewModel.selectVinilo(it) },
                            onDeleteClick = { viewModel.deleteVinilo(it) }
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }

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
        Modifier.fillMaxWidth().clickable { onEditClick(vinilo) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(Modifier.weight(1f)) {
                Text("ID: ${vinilo.idVin}", fontSize = 12.sp, color = Color.Gray)
                Text(vinilo.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF6A1B9A))
                Text("Artista: ${vinilo.artista}")
                Text("Precio: $${vinilo.precio}  Stock: ${vinilo.stock}")
            }

            IconButton(onClick = { onEditClick(vinilo) }) {
                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color(0xFF9C27B0))
            }

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

    var previewImage by remember { mutableStateOf(vinilo.img) }

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            previewImage = it.toString()
            onValueChange(vinilo.copy(img = it.toString()))
        }
    }

    val drawableId = remember(previewImage) {
        if (!previewImage.contains("/") && previewImage.isNotBlank()) {
            context.resources.getIdentifier(
                previewImage.substringBeforeLast("."),
                "drawable",
                context.packageName
            )
        } else 0
    }

    Surface(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        color = Color.White,
        shadowElevation = 16.dp
    ) {

        Column(
            Modifier.padding(24.dp).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                if (isEditing) "Editar Vinilo (ID: ${vinilo.idVin})" else "Crear Nuevo Vinilo",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF6A1B9A)
            )

            Spacer(Modifier.height(16.dp))


            Box(
                Modifier
                    .size(160.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF3E5F5)),
                contentAlignment = Alignment.Center
            ) {
                when {
                    drawableId != 0 ->
                        Image(
                            painter = painterResource(drawableId),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                    previewImage.startsWith("content://") ||
                            previewImage.startsWith("http") ->
                        AsyncImage(
                            model = previewImage,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                    else ->
                        Icon(
                            Icons.Default.Image,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(48.dp)
                        )
                }
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = { launcher.launch("image/*") },
                colors = ButtonDefaults.buttonColors(Color(0xFF9C27B0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Image, contentDescription = null, tint = Color.White)
                Spacer(Modifier.width(6.dp))
                Text("Seleccionar Imagen", color = Color.White)
            }

            Spacer(Modifier.height(16.dp))


            OutlinedTextField(
                value = vinilo.nombre,
                onValueChange = { onValueChange(vinilo.copy(nombre = it)) },
                label = { Text("Nombre del Álbum") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = vinilo.artista,
                onValueChange = { onValueChange(vinilo.copy(artista = it)) },
                label = { Text("Artista") },
                modifier = Modifier.fillMaxWidth()
            )


            Row(Modifier.fillMaxWidth()) {

                OutlinedTextField(
                    value = if (vinilo.precio == 0) "" else vinilo.precio.toString(),
                    onValueChange = { onValueChange(vinilo.copy(precio = it.toIntOrNull() ?: 0)) },
                    label = { Text("Precio") },
                    modifier = Modifier.weight(1f).padding(end = 8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )

                OutlinedTextField(
                    value = if (vinilo.stock == 0) "" else vinilo.stock.toString(),
                    onValueChange = { onValueChange(vinilo.copy(stock = it.toIntOrNull() ?: 0)) },
                    label = { Text("Stock") },
                    modifier = Modifier.weight(1f).padding(start = 8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }

            OutlinedTextField(
                value = vinilo.genero,
                onValueChange = { onValueChange(vinilo.copy(genero = it)) },
                label = { Text("Género") },
                modifier = Modifier.fillMaxWidth()
            )


            OutlinedTextField(
                value = vinilo.descripcion,
                onValueChange = { onValueChange(vinilo.copy(descripcion = it)) },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            Spacer(Modifier.height(20.dp))


            Row(Modifier.fillMaxWidth()) {

                Button(
                    onClick = onCancel,
                    colors = ButtonDefaults.buttonColors(Color.LightGray),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar", color = Color.Black)
                }

                Spacer(Modifier.width(14.dp))

                Button(
                    onClick = {
                        if (isEditing) dialogOpen = true else onSave(vinilo)
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFF6A1B9A)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (isEditing) "Guardar" else "Crear")
                }
            }


            if (dialogOpen) {
                AlertDialog(
                    onDismissRequest = { dialogOpen = false },
                    title = { Text("Confirmar actualización") },
                    text = { Text("¿Guardar cambios en \"${vinilo.nombre}\"?") },
                    confirmButton = {
                        Button(onClick = {
                            dialogOpen = false
                            onSave(vinilo)
                        }) {
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
