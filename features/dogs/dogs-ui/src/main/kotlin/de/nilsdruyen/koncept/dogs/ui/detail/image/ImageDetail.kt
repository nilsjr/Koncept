package de.nilsdruyen.koncept.dogs.ui.detail.image

import android.graphics.drawable.BitmapDrawable
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import androidx.palette.graphics.Target
import androidx.palette.graphics.get
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import de.nilsdruyen.koncept.design.system.KonceptTheme
import de.nilsdruyen.koncept.design.system.annotations.KonceptPreview
import de.nilsdruyen.koncept.domain.Logger.Companion.log
import kotlinx.coroutines.delay

private val targets = listOf(
    Target.LIGHT_VIBRANT,
    Target.VIBRANT,
    Target.DARK_VIBRANT,
    Target.LIGHT_MUTED,
    Target.MUTED,
    Target.DARK_MUTED,
)

fun Int.hex() = String.format("#%06X", (0xFFFFFF.and(this)))

const val duration = 1200L

@Composable
fun ImageDetail(id: String) {
    var palette: Palette? by remember { mutableStateOf(null) }
    var index by remember { mutableStateOf(0) }
    var bgColor by remember { mutableStateOf(Color.White) }
    val colors: List<Color> by remember(palette) {
        derivedStateOf {
            val current = palette
            if (current != null) {
                targets
                    .mapNotNull { current[it] }
                    .map { Color(it.rgb) }
            } else {
                emptyList()
            }
        }
    }
    val animatedColor = animateColorAsState(
        targetValue = bgColor,
        animationSpec = tween(duration.toInt())
    )

    LaunchedEffect(colors, bgColor) {
        if (colors.isNotEmpty()) {
            delay(duration)
            if (index == colors.lastIndex) {
                index = 0
            } else {
                index += 1
            }
            bgColor = colors[index]
        }
    }

    LaunchedEffect(colors) {
        log("image success ${colors.map { it.toArgb().hex() }}")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(animatedColor.value)
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
                    .align(Alignment.Center),
                onState = {
                    when (it) {
                        is AsyncImagePainter.State.Success -> {
                            val bitmap = (it.result.drawable as BitmapDrawable).bitmap
                            palette = Palette.from(bitmap).generate()
                        }
                        else -> {
                            // do nothing
                        }
                    }
                }
            )
        } else {
            Text(text = "no image available", modifier = Modifier.align(Alignment.Center))
        }
        Row(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.BottomCenter)
        ) {
            colors.forEach {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(it)
                )
            }
        }
    }
}

@KonceptPreview
@Composable
fun PreviewEmptyImageDetail() {
    KonceptTheme {
        ImageDetail(id = "")
    }
}

@KonceptPreview
@Composable
fun PreviewImageDetail() {
    KonceptTheme {
        ImageDetail(id = "123")
    }
}