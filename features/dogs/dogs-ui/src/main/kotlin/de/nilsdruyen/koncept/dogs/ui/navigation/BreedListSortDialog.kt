package de.nilsdruyen.koncept.dogs.ui.navigation

import androidx.navigation.NavBackStackEntry
import de.nilsdruyen.koncept.base.navigation.KonceptNavDestination
import de.nilsdruyen.koncept.base.navigation.NavigationDestination
import de.nilsdruyen.koncept.dogs.entity.BreedSortType

object BreedListSortDialog : KonceptNavDestination {

    const val selectedTypeArg = "ordinal"

    override val route = "breed/list/sort/{$selectedTypeArg}"
    override val destination = "breed_list_sort_dest"

    fun createRoute(type: BreedSortType): NavigationDestination {
        return Pair(BreedListSortDialog, "breed/list/sort/${type.ordinal}")
    }

    fun fromNavBackStackEntry(entry: NavBackStackEntry): BreedSortType {
        return BreedSortType.values()[entry.arguments?.getInt(selectedTypeArg) ?: 0]
    }
}