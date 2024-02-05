package de.nilsdruyen.koncept.dogs.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import de.nilsdruyen.koncept.common.ui.toImmutable
import de.nilsdruyen.koncept.dogs.test.DogFactory
import de.nilsdruyen.koncept.dogs.ui.list.DogListState
import de.nilsdruyen.koncept.dogs.ui.list.PreviewDogItem
import de.nilsdruyen.koncept.dogs.ui.list.PreviewDogList
import org.junit.Rule
import org.junit.Test

class DogListUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @ExperimentalMaterial3Api
    @Test
    fun MyTest() {
        val dogListState = DogListState(DogFactory.buildList(10).toImmutable())

        composeTestRule.setContent {
            PreviewDogList(dogListState)
        }

        val list = composeTestRule.onNodeWithTag("dogList", useUnmergedTree = true)
        list.assertIsDisplayed()
    }

    @ExperimentalMaterial3Api
    @Test
    fun DogListItem() {
        val dog = DogFactory.build()

        composeTestRule.setContent {
            PreviewDogItem(dog)
        }

        val list = composeTestRule.onNodeWithText(dog.name, useUnmergedTree = true)
        list.assertIsDisplayed()
    }
}
