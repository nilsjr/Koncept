package de.nilsdruyen.koncept.dogs.ui.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import de.nilsdruyen.koncept.common.ui.toImmutable
import de.nilsdruyen.koncept.design.system.KonceptTheme
import de.nilsdruyen.koncept.dogs.entity.BreedId
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.dogs.ui.favorites.DogFavoriteItem
import de.nilsdruyen.koncept.dogs.ui.utils.PaparazziTest
import org.junit.Test

internal class DogListKtTest : PaparazziTest() {

    @Test
    fun dogItemSnapshot() {
        paparazziRule.snapshot {
            KonceptTheme {
                Column {
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
        }
    }

    @Test
    fun dogFavoriteItemSnapshot() {
        paparazziRule.snapshot {
            KonceptTheme {
                Column {
                    DogFavoriteItem(dog = Dog(BreedId(1), "Nils", false))
                }
            }
        }
    }

    @Test
    fun dogListSnapshot() {
        val state = DogListState(
            list = List(6) {
                Dog(BreedId(it), "Breed $it")
            }.toImmutable()
        )

        paparazziRule.snapshot {
            KonceptTheme {
                DogList(
                    state = state,
                    showDog = {},
                    reloadList = {},
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    @Test
    fun dogListEmptySnapshot() {
        val state = DogListState()

        paparazziRule.snapshot {
            KonceptTheme {
                DogList(
                    state = state,
                    showDog = {},
                    reloadList = {},
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}