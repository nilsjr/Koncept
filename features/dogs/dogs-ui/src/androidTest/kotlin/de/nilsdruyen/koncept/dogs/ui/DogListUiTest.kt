package de.nilsdruyen.koncept.dogs.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.karumi.shot.ScreenshotTest
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.dogs.test.DogFactory
import de.nilsdruyen.koncept.dogs.ui.list.DogListState
import de.nilsdruyen.koncept.dogs.ui.list.PreviewDogItem
import de.nilsdruyen.koncept.dogs.ui.list.PreviewDogList
import org.junit.Rule
import org.junit.Test

//@RunWith(ShotTestRunner::class)
class DogListUiTest : ScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @ExperimentalMaterial3Api
    @Test
    fun MyTest() {
        val dogListState = DogListState(DogFactory.buildList(20))

        composeTestRule.setContent {
            PreviewDogList(dogListState)
        }

        val list = composeTestRule.onNodeWithTag("dogList", useUnmergedTree = true)
        list.assertIsDisplayed()

        compareScreenshot(composeTestRule)
    }

    @ExperimentalMaterial3Api
    @Test
    fun DogListItem() {
        val dog = Dog(1, "Nils Hund")

        composeTestRule.setContent {
            PreviewDogItem(dog)
        }

        val list = composeTestRule.onNodeWithText("Nils Hund", useUnmergedTree = true)
        list.assertIsDisplayed()

        compareScreenshot(composeTestRule)
    }
}