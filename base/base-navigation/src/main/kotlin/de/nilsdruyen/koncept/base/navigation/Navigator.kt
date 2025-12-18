package de.nilsdruyen.koncept.base.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import dagger.hilt.android.scopes.ActivityRetainedScoped

class Navigator(
    val backStack: NavBackStack<NavKey>,
) {

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