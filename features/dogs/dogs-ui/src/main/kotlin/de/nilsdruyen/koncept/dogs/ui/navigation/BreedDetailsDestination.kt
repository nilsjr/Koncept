package de.nilsdruyen.koncept.dogs.ui.navigation

import androidx.lifecycle.SavedStateHandle
import de.nilsdruyen.koncept.base.navigation.KonceptNavDestination
import de.nilsdruyen.koncept.base.navigation.NavigationDestination

object BreedDetailsDestination : KonceptNavDestination {

    const val breedIdArg = "breedId"

    override val route: String = "details/{$breedIdArg}"
    override val destination: String = "breed_details_dest"

    fun navigate(root: String, id: Int): NavigationDestination {
        return Pair(BreedDetailsDestination, "$root/details/$id")
    }

    fun buildRoute(id: Int): NavigationDestination {
        return Pair(BreedDetailsDestination, "details/$id")
    }

    fun fromSavedState(savedStateHandle: SavedStateHandle): Int {
        return savedStateHandle[breedIdArg] ?: -1
    }
}