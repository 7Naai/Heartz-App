package com.example.heartzapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.heartzapp.viewmodel.DiscogsViewModel

@Composable
fun ProximosProductos() {
    val vm: DiscogsViewModel = viewModel()
    val productos by vm.productos.collectAsState()

    if (productos.isNotEmpty()) {
        Column {
            Text(
                text = "¡Próximos productos!",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(productos) { item ->
                    Column(
                        modifier = Modifier.width(140.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(item.imagen),
                            contentDescription = item.titulo,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                        )
                        Text(item.titulo)
                        Text(item.artista, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
