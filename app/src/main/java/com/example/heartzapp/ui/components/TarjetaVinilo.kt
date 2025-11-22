package com.example.heartzapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.heartzapp.data.model.Vinilo
import com.example.heartzapp.viewmodel.CarritoViewModel

@Composable
fun TarjetaVinilo(
    vinilo: Vinilo,
    carritoVM: CarritoViewModel,
    onVerDetalle: (Vinilo) -> Unit = {}
) {
    val context = LocalContext.current

    val nombreSinExt = vinilo.img.substringBeforeLast(".")

    val imageResId = context.resources.getIdentifier(
        nombreSinExt,
        "drawable",
        context.packageName
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2A004E)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 📸 Imagen
            if (imageResId != 0) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = vinilo.nombre,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onVerDetalle(vinilo) },
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color(0xFF4A148C), RoundedCornerShape(8.dp))
                        .clickable { onVerDetalle(vinilo) },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Sin img", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 🔤 Nombre
            Text(
                text = vinilo.nombre,
                style = MaterialTheme.typography.titleSmall.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable { onVerDetalle(vinilo) }
            )

            // Artista
            Text(
                text = vinilo.artista,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFFB388FF)
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // 💵 Precio
            Text(
                text = "$${"%,d".format(vinilo.precio)}",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color(0xFF9C27B0),
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Botón VER
                Button(
                    onClick = { onVerDetalle(vinilo) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6A1B9A)
                    )
                ) {
                    Text("Ver", color = Color.White)
                }

                // Botón AÑADIR AL CARRITO
                Button(
                    onClick = { carritoVM.agregar(vinilo) },
                    modifier = Modifier.weight(1f),
                    enabled = vinilo.stock > 0,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFB388FF),
                        disabledContainerColor = Color.Gray
                    )
                ) {
                    Text(
                        if (vinilo.stock > 0) "Añadir" else "Agotado",
                        color = if (vinilo.stock > 0) Color.White else Color.LightGray
                    )
                }
            }
        }
    }
}
