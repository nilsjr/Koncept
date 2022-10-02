package de.nilsdruyen.koncept.dogs.ui.navigation

import androidx.lifecycle.SavedStateHandle
import de.nilsdruyen.koncept.base.navigation.KonceptNavDestination
import de.nilsdruyen.koncept.base.navigation.NavigationDestination

object BreedDetailsDestination : KonceptNavDestination {

    const val breedIdArg = "breedId"

    override val route: String = "breed/details/{$breedIdArg}"
    override val destination: String = "breed_details_dest"

    fun buildRoute(id: Int): NavigationDestination {
        return Pair(BreedDetailsDestination, "breed/details/$id")
    }

    fun fromSavedState(savedStateHandle: SavedStateHandle): Int {
        return savedStateHandle[breedIdArg] ?: -1
    }
}