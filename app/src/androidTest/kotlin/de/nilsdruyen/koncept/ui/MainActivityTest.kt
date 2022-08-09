package de.nilsdruyen.koncept.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import de.nilsdruyen.koncept.KonceptTestApplication
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode

@RunWith(AndroidJUnit4::class)
@Config(sdk = [24], application = KonceptTestApplication::class)
@LooperMode(LooperMode.Mode.PAUSED)
class MainActivityTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {

    }

    @Test
    fun dogListShouldLoadSuccessfully() {
        with(composeRule) {
            onNodeWithTag("appbar").assertIsDisplayed()

            waitUntil {
                onAllNodesWithTag("loading").fetchSemanticsNodes().isEmpty()
            }

            onNodeWithTag("breedList").assertIsDisplayed()
            onNodeWithTag("breedList").assertIsSelected()
        }
    }

    @Test
    fun navigateToFavorites() {
        composeRule.waitUntil {
            composeRule.onAllNodesWithTag("loading").fetchSemanticsNodes().isEmpty()
        }

        composeRule.onNodeWithTag("favorites").performClick()

        composeRule.waitUntil {
            composeRule.onAllNodesWithTag("loading").fetchSemanticsNodes().isEmpty()
        }

        composeRule.onNodeWithTag("fav_appbar").assertIsDisplayed()
        composeRule.onAllNodesWithText("Favorites").fetchSemanticsNodes().isNotEmpty()
    }
}