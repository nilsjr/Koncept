package de.nilsdruyen.koncept.base.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import dagger.hilt.android.scopes.ActivityRetainedScoped

typealias EntryProviderInstaller = EntryProviderScope<Any>.() -> Unit

@ActivityRetainedScoped
class Navigator(startDestination: NavKey) {

//    val backStack: SnapshotStateList<Any> = mutableStateListOf(startDestination)

//    val backStack = rememberNavBackStack(startDestination)

    fun goTo(destination: NavKey) {
//        backStack.add(destination)
    }

    fun goBack() {
//        backStack.removeLastOrNull()
    }
}