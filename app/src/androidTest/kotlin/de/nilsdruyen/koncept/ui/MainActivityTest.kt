package de.nilsdruyen.koncept.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.espresso.web.assertion.WebViewAssertions.webMatches
import androidx.test.espresso.web.sugar.Web.onWebView
import androidx.test.espresso.web.webdriver.DriverAtoms
import androidx.test.espresso.web.webdriver.DriverAtoms.clearElement
import androidx.test.espresso.web.webdriver.DriverAtoms.findElement
import androidx.test.espresso.web.webdriver.DriverAtoms.getText
import androidx.test.espresso.web.webdriver.DriverAtoms.webClick
import androidx.test.espresso.web.webdriver.Locator
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import de.nilsdruyen.koncept.KonceptTestApplication
import org.hamcrest.CoreMatchers.containsString
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@Config(sdk = [24], application = KonceptTestApplication::class)
@LooperMode(LooperMode.Mode.PAUSED)
class MainActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
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

    @Test
    fun testWeb() {
        with(composeRule) {
            val MACCHIATO = "Macchiato"

            onNodeWithTag("web").performClick()

            onWebView()
                // Find the input element by ID
                .withElement(findElement(Locator.ID, "text_input"))
                // Clear previous input
                .perform(clearElement())
                // Enter text into the input element
                .perform(DriverAtoms.webKeys(MACCHIATO))
                // Find the submit button
                .withElement(findElement(Locator.ID, "submitBtn"))
                // Simulate a click via javascript
                .perform(webClick())
                // Find the response element by ID
                .withElement(findElement(Locator.ID, "response"))
                // Verify that the response page contains the entered text
                .check(webMatches(getText(), containsString(MACCHIATO)))
        }
    }
}