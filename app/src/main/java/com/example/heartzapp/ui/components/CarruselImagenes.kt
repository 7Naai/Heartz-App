package com.example.heartzapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.heartzapp.R
import kotlinx.coroutines.delay

@Composable
fun CarruselImagenes() {
    val imagenes = listOf(
        R.drawable.carrusel_1,
        R.drawable.carrusel_2,
        R.drawable.carrusel_3
    )

    val pagerState = rememberPagerState(pageCount = { imagenes.size })

    // Este es el autoslide
    LaunchedEffect(pagerState) {
        while (true) {
            delay(5000) // espera 5 segundos antes de cambiar
            val nextPage = (pagerState.currentPage + 1) % imagenes.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(horizontal = 16.dp)
        ) { page ->
            Image(
                painter = painterResource(id = imagenes[page]),
                contentDescription = "Imagen del carrusel ${page + 1}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop // ajusta proporciones sin deformar
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Indicadores de página
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(imagenes.size) { index ->
                val color = if (pagerState.currentPage == index)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)

                Surface(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(if (pagerState.currentPage == index) 10.dp else 8.dp)
                        .clip(CircleShape),
                    color = color
                ) {}
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CarruselPreview() {
    CarruselImagenes()
}
