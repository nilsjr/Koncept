package de.nilsdruyen.koncept.dogs.ui.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import de.nilsdruyen.koncept.base.navigation.KonceptNavDestination
import de.nilsdruyen.koncept.base.navigation.NavigationDestination

object ImageDetailDestination : KonceptNavDestination {

    const val urlArg = "url"

    override val route: String = "image/{$urlArg}"
    override val destination: String = "image_dest"

    fun buildRoute(url: String): NavigationDestination {
        return Pair(ImageDetailDestination, "image/$url")
    }

    fun fromBackStackEntry(backStackEntry: NavBackStackEntry): String {
        return backStackEntry.arguments?.getString(urlArg) ?: ""
    }

    fun fromSavedState(savedStateHandle: SavedStateHandle): String {
        return savedStateHandle[urlArg] ?: ""
    }
}