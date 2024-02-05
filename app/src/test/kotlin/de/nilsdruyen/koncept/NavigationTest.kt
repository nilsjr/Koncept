package de.nilsdruyen.koncept

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import de.nilsdruyen.koncept.ui.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(instrumentedPackages = ["androidx.loader.content"])
@RunWith(RobolectricTestRunner::class)
class NavigationTest {

    @get:Rule
    val rule = createAndroidComposeRule(MainActivity::class.java)

    @Test
    fun `GIVEN a list of breeds WHEN user starts app THEN a list of breeds is showed`() {
        rule.onNodeWithTag("appbar").assertIsDisplayed()

        rule.waitUntil {
            rule.onAllNodesWithTag("loading").fetchSemanticsNodes().isEmpty()
        }
        rule.mainClock.advanceTimeBy(200L)

        rule.onAllNodesWithTag("dogItem").assertCountEquals(0)
    }
}
