package de.nilsdruyen.koncept.dogs.ui.list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Visibility
import de.nilsdruyen.koncept.design.system.KonceptTheme
import de.nilsdruyen.koncept.dogs.entity.BreedId
import de.nilsdruyen.koncept.dogs.entity.Dog

@Composable
fun DogItem(
    dog: Dog,
    modifier: Modifier = Modifier,
    showDog: (Dog) -> Unit = {},
) {
    Card(
        onClick = { showDog(dog) },
        modifier = modifier
            .fillMaxWidth()
            .testTag("dogItem_${dog.id}"),
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            val (name, isFavorite, lifeSpan) = createRefs()
            Text(
                text = dog.name,
                style = MaterialTheme.typography.headlineLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .constrainAs(name) {
                        width = Dimension.fillToConstraints
                        linkTo(parent.start, isFavorite.start, bias = 0f)
                        top.linkTo(parent.top)
                    }
            )
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                modifier = Modifier.constrainAs(isFavorite) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    visibility = if (dog.isFavorite) Visibility.Visible else Visibility.Gone
                }
            )
            Text(
                text = "age: ${dog.lifeSpan} years - ${dog.weight.last}/${dog.height.last}",
                modifier = Modifier.constrainAs(lifeSpan) {
                    top.linkTo(name.bottom, 8.dp)
                }
            )
        }
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
private fun PreviewDogItem(@PreviewParameter(DogItemPreviewProvider::class) dog: Dog) {
    KonceptTheme {
        DogItem(dog)
    }
}

private class DogItemPreviewProvider : PreviewParameterProvider<Dog> {
    override val values: Sequence<Dog> = sequenceOf(
        Dog(
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
        Dog(BreedId(2), "Thea", isFavorite = true),
        Dog(BreedId(2), "Thea dakad lm lakd alkw lak mldaw", isFavorite = true),
    )
}
