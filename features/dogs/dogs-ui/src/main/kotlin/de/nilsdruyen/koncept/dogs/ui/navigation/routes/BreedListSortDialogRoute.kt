package de.nilsdruyen.koncept.dogs.ui.navigation.routes

import androidx.navigation.NavBackStackEntry
import de.nilsdruyen.koncept.base.navigation.KonceptNavDestination
import de.nilsdruyen.koncept.base.navigation.KonceptNavRoute
import de.nilsdruyen.koncept.dogs.entity.BreedSortType

class BreedListSortDialogRoute(baseRoute: String) : KonceptNavRoute.NestedNavRoute(baseRoute) {

    companion object {

        const val selectedTypeArg = "ordinal"

        fun fromNavBackStackEntry(entry: NavBackStackEntry): BreedSortType {
            return BreedSortType.values()[entry.arguments?.getInt(selectedTypeArg) ?: 0]
        }
    }

    override val route = "breed_list_sort"

    override fun pathParameters(): List<String> = listOf(selectedTypeArg)
}

fun BreedListSortDialogRoute.navigate(type: BreedSortType): KonceptNavDestination {
    return KonceptNavDestination.NestedNavDestination("$routePrefix/${type.ordinal}")
}