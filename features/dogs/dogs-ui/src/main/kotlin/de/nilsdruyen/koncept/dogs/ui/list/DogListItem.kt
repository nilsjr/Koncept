package de.nilsdruyen.koncept.dogs.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import de.nilsdruyen.koncept.design.system.KonceptTheme
import de.nilsdruyen.koncept.dogs.entity.BreedId
import de.nilsdruyen.koncept.dogs.entity.Dog

@Composable
fun DogListItem(dog: Dog, onClick: () -> Unit, modifier: Modifier = Modifier) {
    ListItem(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        headlineContent = { Text(dog.name) },
        overlineContent = dog.bredFor.takeIf { it.isNotEmpty() }?.run {
            {
                Text(dog.bredFor, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        },
        supportingContent = dog.group.takeIf { it.isNotEmpty() }?.run {
            {
                Text(dog.group)
            }
        },
        trailingContent = {
            Icon(
                imageVector = if (dog.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Set Favorite"
            )
        }
    )
}

@Preview
@Composable
internal fun DogListItemPreview() {
    KonceptTheme {
        DogListItem(
            dog = Dog(
                id = BreedId(value = 8899),
                name = "Sara Rich",
                isFavorite = false,
                temperament = listOf(),
                lifeSpan = 0..2,
                weight = 20..30,
                height = 20..20,
                bredFor = "awdh",
                origin = listOf(),
                group = ""
            ),
            {}
        )
    }
}
