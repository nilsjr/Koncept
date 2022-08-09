package de.nilsdruyen.koncept

import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(instrumentedPackages = ["androidx.loader.content"])
@RunWith(RobolectricTestRunner::class)
class ComposeTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun textIsDisplayed() {
        rule.setContent {
            Text("Testing Compose")
        }

        rule.onNodeWithText("Testing Compose").assertIsDisplayed()
    }
}