package de.nilsdruyen.koncept.dogs.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation

@Composable
fun DogImage(url: String, onImageClick: () -> Unit, modifier: Modifier = Modifier) {
    val pxValue = with(LocalDensity.current) { 16.dp.toPx() }

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .transformations(RoundedCornersTransformation(pxValue))
            .build(),
        contentDescription = null,
        modifier = modifier
            .clickable { onImageClick() },
    )
}