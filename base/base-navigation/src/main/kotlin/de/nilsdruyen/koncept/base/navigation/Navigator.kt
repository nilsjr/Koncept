package de.nilsdruyen.koncept.base.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack

class Navigator(val backStack: NavBackStack<NavKey>) {

    fun goTo(destination: NavKey) {
        backStack.add(destination)
    }

    fun goBack() {
        backStack.removeLastOrNull()
    }
}

@Composable
fun rememberNavigator(startDestination: NavKey): Navigator {
    val backStack = rememberNavBackStack(startDestination)

    return remember {
        Navigator(backStack = backStack)
    }
}
