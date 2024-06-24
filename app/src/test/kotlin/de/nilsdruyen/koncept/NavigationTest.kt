package de.nilsdruyen.koncept

import android.os.Build
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import de.nilsdruyen.koncept.ui.MainActivity
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(instrumentedPackages = ["androidx.loader.content"], sdk = [Build.VERSION_CODES.TIRAMISU])
@RunWith(RobolectricTestRunner::class)
@Ignore("Replace with proper compose tests")
class NavigationTest {

    @get:Rule
    val rule = createAndroidComposeRule(MainActivity::class.java)

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `GIVEN a list of breeds WHEN user starts app THEN a list of breeds is showed`() {
        with(rule) {
            onNodeWithTag("appbar").assertIsDisplayed()
            waitUntilDoesNotExist(hasTestTag("loading"), 2_000)
            onAllNodesWithTag("dogItem").assertCountEquals(0)
        }
    }
}
