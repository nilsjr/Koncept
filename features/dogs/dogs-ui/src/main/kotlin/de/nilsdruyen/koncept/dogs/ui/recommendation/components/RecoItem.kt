package de.nilsdruyen.koncept.dogs.ui.recommendation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.dogs.ui.components.DogImage

@Composable
fun RecoItem(dog: Dog, toggleFavorite: () -> Unit) {
    ConstraintLayout(
        modifier = Modifier
            .widthIn(160.dp)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, shape = RoundedCornerShape(16.dp), color = Color.LightGray)
    ) {
        val (image, favButton, name) = createRefs()

        DogImage(
            url = "https://cdn2.thedogapi.com/images/${dog.imageId}_150x150.jpg",
            onImageClick = {},
            modifier = Modifier.constrainAs(image) {
                linkTo(parent.top, parent.bottom)
                linkTo(parent.start, parent.end)
            }
        )

        IconButton(
            onClick = toggleFavorite,
            modifier = Modifier.constrainAs(favButton) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
            }
        ) {
            Icon(
                imageVector = if (dog.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Set Favorite",
                tint = Color.White,
            )
        }
    }
}