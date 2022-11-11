package de.nilsdruyen.koncept.dogs.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import de.nilsdruyen.koncept.dogs.entity.Dog

@Composable
fun DogGridItem(dog: Dog) {
    val url = "https://cdn2.thedogapi.com/images/${dog.id}.jpg"

    Box(
        modifier = Modifier
            .width(150.dp)
            .aspectRatio(1f)
            .padding(4.dp)
            .background(Color.Red)
    ) {
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(8.dp)
                .clickable { },
        )
    }
}