package de.nilsdruyen.koncept.dogs.ui.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.nilsdruyen.koncept.design.system.KonceptTheme
import de.nilsdruyen.koncept.dogs.entity.BreedId
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.dogs.ui.list.DogListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogFavoriteItem(dog: Dog, showBreed: (BreedId) -> Unit = {}) {
    val threshold = LocalConfiguration.current.screenWidthDp * LocalDensity.current.density / 2
    val dismissState = rememberSwipeToDismissBoxState(
        positionalThreshold = { threshold }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red)
            ) {
                Text(
                    text = "Delete",
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(16.dp),
                    color = Color.White,
                )
            }
        }
    ) {
        DogListItem(
            dog = dog,
            onClick = { showBreed(dog.id) },
            modifier = Modifier.testTag("dog_favorite_name"),
        )
    }
}

@Preview
@Composable
private fun PreviewDogFavoriteItem() {
    KonceptTheme {
        DogFavoriteItem(dog = Dog(BreedId(1), "Nils", false))
    }
}
