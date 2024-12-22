package de.nilsdruyen.koncept.dogs.ui.list.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Visibility
import coil3.annotation.ExperimentalCoilApi
import coil3.asImage
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import coil3.request.ImageRequest
import coil3.request.crossfade
import de.nilsdruyen.koncept.design.system.KonceptTheme
import de.nilsdruyen.koncept.dogs.entity.Breed
import de.nilsdruyen.koncept.dogs.entity.BreedId

@Composable
fun BreedItem(
    breed: Breed,
    modifier: Modifier = Modifier,
    showDog: (Breed) -> Unit = {},
) {
    val cornerShape = MaterialTheme.shapes.medium
    Card(
        onClick = { showDog(breed) },
        modifier = modifier
            .fillMaxWidth()
            .testTag("dogItem_${breed.id}"),
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (image, name, isFavorite, lifeSpan) = createRefs()
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(breed.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .constrainAs(image) {
                        width = Dimension.value(84.dp)
                        height = Dimension.fillToConstraints
                        start.linkTo(parent.start)
                        linkTo(parent.top, parent.bottom)
                    }
                    .clip(cornerShape)
            )
            Text(
                text = breed.name,
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .constrainAs(name) {
                        width = Dimension.fillToConstraints
                        linkTo(
                            image.end,
                            isFavorite.start,
                            startMargin = 16.dp,
                            bias = 0f,
                            endMargin = 8.dp,
                            endGoneMargin = 16.dp,
                        )
                        top.linkTo(parent.top, 8.dp)
                    }
            )
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                modifier = Modifier.constrainAs(isFavorite) {
                    top.linkTo(parent.top, 16.dp)
                    end.linkTo(parent.end, 16.dp)
                    visibility = if (breed.isFavorite) Visibility.Visible else Visibility.Gone
                }
            )
            Text(
                text = "age: ${breed.lifeSpan} years - ${breed.weight.last}/${breed.height.last}",
                modifier = Modifier.constrainAs(lifeSpan) {
                    start.linkTo(name.start)
                    top.linkTo(name.bottom, 8.dp)
                    bottom.linkTo(parent.bottom, 16.dp)
                },
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@ExperimentalMaterial3Api
@Preview
@Composable
private fun PreviewDogItem(@PreviewParameter(DogItemPreviewProvider::class) breed: Breed) {
    val imageBitmap = ImageBitmap.imageResource(id = de.nilsdruyen.koncept.design.system.R.drawable.malamute)
    val previewHandler = AsyncImagePreviewHandler {
        imageBitmap.asAndroidBitmap().asImage()
    }
    KonceptTheme {
        CompositionLocalProvider(
            LocalAsyncImagePreviewHandler provides previewHandler
        ) {
            BreedItem(breed)
        }
    }
}

private class DogItemPreviewProvider : PreviewParameterProvider<Breed> {
    override val values: Sequence<Breed> = sequenceOf(
        Breed(
            id = BreedId(1),
            name = "Lassie",
            isFavorite = false,
            temperament = listOf("aggressive"),
            lifeSpan = 12..14,
            weight = 20..21,
            height = 21..25,
            bredFor = "Toy",
            origin = listOf("sport"),
            group = "smallies"
        ),
        Breed(BreedId(2), "Raya", isFavorite = true),
        Breed(BreedId(2), "Thea dakad lm lakd alkw lak mldaw", isFavorite = true),
    )
}
