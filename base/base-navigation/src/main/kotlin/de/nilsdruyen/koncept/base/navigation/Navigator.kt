package de.nilsdruyen.koncept.base.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack

class Navigator(val backStack: NavBackStack<NavKey>, private val startDestination: NavKey) {

    fun goTo(destination: NavKey) {
        backStack.add(destination)
    }

    /**
     * Switches to a top level destination: pops everything above the start destination and puts the
     * selected destination on top. Reselecting the current destination is a no-op, so tab taps never
     * stack duplicates and back always returns to the start destination.
     */
    fun goToTopLevel(destination: NavKey) {
        if (backStack.last() == destination) return
        backStack.removeAll { it != startDestination }
        if (destination != startDestination) {
            backStack.add(destination)
        }
    }

    fun goBack() {
        if (backStack.size > 1) {
            backStack.removeLastOrNull()
        }
    }
}

@Composable
fun rememberNavigator(startDestination: NavKey, extraDestinations: List<NavKey> = emptyList()): Navigator {
    val backStack = rememberNavBackStack(startDestination, *extraDestinations.toTypedArray())

    return remember {
        Navigator(backStack = backStack, startDestination = startDestination)
    }
}
