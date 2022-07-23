package de.nilsdruyen.koncept.dogs.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Visibility
import de.nilsdruyen.koncept.common.ui.KonceptTheme
import de.nilsdruyen.koncept.common.ui.MaterialCard
import de.nilsdruyen.koncept.dogs.entity.Dog

@Composable
fun DogItem(dog: Dog, showDog: (Dog) -> Unit = {}) {
    MaterialCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { showDog(dog) },
        backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(8.dp),
        elevation = 2.dp
    ) {
        ConstraintLayout(
            modifier = Modifier.padding(8.dp)
        ) {
            val (name, isFavorite) = createRefs()
            Text(
                text = dog.name,
                style = MaterialTheme.typography.headlineLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .constrainAs(name) {
                        width = Dimension.fillToConstraints
                        linkTo(parent.start, isFavorite.start, bias = 0f)
                        linkTo(parent.top, parent.bottom)
                    }
            )
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                modifier = Modifier.constrainAs(isFavorite) {
                    linkTo(parent.top, parent.bottom)
                    end.linkTo(parent.end)
                    visibility = if (dog.isFavorite) Visibility.Visible else Visibility.Gone
                }
            )
        }
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun PreviewDogItem(@PreviewParameter(DogItemPreviewProvider::class) dog: Dog) {
    KonceptTheme {
        DogItem(dog)
    }
}

class DogItemPreviewProvider : PreviewParameterProvider<Dog> {
    override val values: Sequence<Dog> = sequenceOf(
        Dog(1, "Lassie", isFavorite = false),
        Dog(2, "Thea", isFavorite = true),
        Dog(2, "Thea dakad lm lakd alkw lak mldaw", isFavorite = true),
    )
}