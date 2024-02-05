package de.nilsdruyen.koncept.dogs.ui.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.nilsdruyen.koncept.design.system.KonceptTheme
import de.nilsdruyen.koncept.dogs.entity.BreedId
import de.nilsdruyen.koncept.dogs.entity.Dog

@Composable
fun DogFavoriteItem(dog: Dog, showBreed: (BreedId) -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(4.dp)
            )
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable {
                showBreed(dog.id)
            }
            .padding(8.dp)
    ) {
        Text(text = dog.name, modifier = Modifier.testTag("dog_favorite_name"))
    }
}

@Preview
@Composable
private fun PreviewDogFavoriteItem() {
    KonceptTheme {
        DogFavoriteItem(dog = Dog(BreedId(1), "Nils", false))
    }
}
