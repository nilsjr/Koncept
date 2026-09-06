package de.nilsdruyen.koncept.base.navigation

import androidx.navigation3.runtime.NavKey
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Verifies the per-tab back stack bookkeeping in [Navigator]: the flattened [Navigator.backStack]
 * must always stay consistent with the per-tab sizes, and switching tabs must preserve each tab's
 * nested stack. [Navigator] is fed plain lists here so the logic can be exercised without a Compose
 * `NavBackStack`.
 */
internal class NavigatorTest {

    private data object Home : NavKey
    private data object Favorites : NavKey
    private data object Web : NavKey
    private data class Detail(val id: Int) : NavKey

    private val backStack = mutableListOf<NavKey>(Home)
    private val topLevelStackSizes = mutableListOf(1)
    private val navigator = Navigator(backStack, topLevelStackSizes)

    @Test
    fun `selecting a new tab appends it to the back stack`() {
        navigator.goToTopLevel(Favorites)

        assertEquals(listOf(Home, Favorites), backStack)
        assertInvariant()
    }

    @Test
    fun `reselecting the current tab does nothing`() {
        navigator.goToTopLevel(Home)

        assertEquals(listOf(Home), backStack)
        assertInvariant()
    }

    @Test
    fun `navigating within a tab pushes onto that tab`() {
        navigator.goToTopLevel(Favorites)
        navigator.goTo(Detail(1))

        assertEquals(listOf(Home, Favorites, Detail(1)), backStack)
        assertInvariant()
    }

    @Test
    fun `returning to a tab restores its nested stack`() {
        navigator.goToTopLevel(Favorites)
        navigator.goTo(Detail(1))
        navigator.goToTopLevel(Web)

        navigator.goToTopLevel(Favorites)

        // Favorites keeps its detail and moves back to the front; Web is preserved behind it.
        assertEquals(listOf(Home, Web, Favorites, Detail(1)), backStack)
        assertInvariant()
    }

    @Test
    fun `going back pops the current entry`() {
        navigator.goToTopLevel(Favorites)
        navigator.goTo(Detail(1))

        navigator.goBack()

        assertEquals(listOf(Home, Favorites), backStack)
        assertInvariant()
    }

    @Test
    fun `going back from a tab root drops the tab`() {
        navigator.goToTopLevel(Favorites)
        navigator.goToTopLevel(Web)

        navigator.goBack()

        assertEquals(listOf(Home, Favorites), backStack)
        assertInvariant()
    }

    @Test
    fun `going back never empties the back stack`() {
        navigator.goBack()

        assertEquals(listOf(Home), backStack)
        assertInvariant()
    }

    private fun assertInvariant() {
        assertEquals(backStack.size, topLevelStackSizes.sum())
    }
}
