package de.nilsdruyen.koncept.dogs.ui.navigation.routes

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import de.nilsdruyen.koncept.base.navigation.KonceptNavDestination
import de.nilsdruyen.koncept.base.navigation.KonceptNavRoute
import de.nilsdruyen.koncept.dogs.entity.BreedSortType

object BreedListSortDialogRoute : KonceptNavRoute.NestedNavRoute {

    private const val selectedTypeArg = "ordinal"

    override val route = "breed_list_sort"

    override fun pathParameters(): List<NamedNavArgument> = listOf(
        navArgument(selectedTypeArg) {
            type = NavType.IntType
        }
    )

    fun createRoute(
        graph: KonceptNavRoute.GraphNavRoute,
        type: BreedSortType
    ): KonceptNavDestination.NestedNavDestination = buildRoute(graph, type.ordinal.toString())

    fun fromNavBackStackEntry(entry: NavBackStackEntry): BreedSortType {
        return BreedSortType.values()[entry.arguments?.getInt(selectedTypeArg) ?: 0]
    }
}
