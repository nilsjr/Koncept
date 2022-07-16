package de.nilsdruyen.koncept.dogs.ui.detail.image

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import de.nilsdruyen.koncept.common.ui.KonceptTheme

@Composable
fun ImageDetail(id: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        if (id.isNotEmpty()) {
            val pxValue = with(LocalDensity.current) { 16.dp.toPx() }
            val url = "https://cdn2.thedogapi.com/images/$id.jpg"

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(url)
                    .crossfade(true)
                    .transformations(RoundedCornersTransformation(pxValue))
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(8.dp)
                    .align(Alignment.Center)
            )
        } else {
            Text(text = "no image available", modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Preview
@Composable
fun PreviewEmptyImageDetail() {
    KonceptTheme {
        ImageDetail(id = "")
    }
}

@Preview
@Composable
fun PreviewImageDetail() {
    KonceptTheme {
        ImageDetail(id = "123")
    }
}