package com.example.heartzapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.heartzapp.data.model.ItemCarrito

@Composable
fun ItemCarritoCard(
    item: ItemCarrito,
    onIncrement: (ItemCarrito) -> Unit,
    onDecrement: (ItemCarrito) -> Unit
) {
    val context = LocalContext.current

    val imageResId = context.resources.getIdentifier(
        item.img,
        "drawable",
        context.packageName
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2A004E)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del Vinilo
            if (imageResId != 0) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = item.nombre,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color(0xFF4A148C), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "💿", style = MaterialTheme.typography.titleLarge)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Nombre y Precio
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.nombre,
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "$${"%,d".format(item.precio)} c/u",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFFB388FF)
                    )
                )
            }

            // Controles de Cantidad
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(
                    onClick = { onDecrement(item) },
                    enabled = item.cantidad > 0,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color.White,
                        containerColor = Color(0xFF6A1B9A)
                    )
                ) {
                    // USO DEL ÍCONO 'REMOVE'
                    Icon(Icons.Default.Remove, contentDescription = "Quitar uno")
                }

                Text(
                    text = item.cantidad.toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                IconButton(
                    onClick = { onIncrement(item) },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color.White,
                        containerColor = Color(0xFF9C27B0)
                    )
                ) {
                    // USO DEL ÍCONO 'ADD'
                    Icon(Icons.Default.Add, contentDescription = "Agregar uno")
                }
            }
        }
    }
}