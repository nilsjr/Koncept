package de.nilsdruyen.koncept.dogs.ui.list

import de.nilsdruyen.koncept.dogs.entity.BreedId
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.dogs.ui.favorites.PreviewDogFavoriteItem
import de.nilsdruyen.koncept.dogs.ui.utils.PaparazziTest
import org.junit.Test

internal class DogListKtTest : PaparazziTest() {

    @Test
    fun launchComposable() {
        paparazziRule.snapshot {
            DogItem(
                Dog(
                    id = BreedId(value = 0),
                    name = "Nils",
                    isFavorite = false,
                    temperament = listOf(),
                    lifeSpan = 1..2,
                    weight = 1..2,
                    height = 1..2,
                    bredFor = "",
                    origin = listOf(),
                    group = ""
                )
            )
        }
    }

    @Test
    fun launchComposable2() {
        paparazziRule.snapshot {
            PreviewDogFavoriteItem()
        }
    }
}