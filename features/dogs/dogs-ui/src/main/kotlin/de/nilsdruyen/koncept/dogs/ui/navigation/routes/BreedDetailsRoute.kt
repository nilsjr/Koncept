package de.nilsdruyen.koncept.dogs.ui.navigation.routes

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import de.nilsdruyen.koncept.base.navigation.KonceptNavDestination
import de.nilsdruyen.koncept.base.navigation.KonceptNavRoute

object BreedDetailsRoute : KonceptNavRoute.NestedNavRoute {

    private const val breedIdArg = "breedId"

    override val route: String = "breed_detail"

    override fun pathParameters(): List<NamedNavArgument> = listOf(
        navArgument(breedIdArg) {
            type = NavType.IntType
        }
    )

    fun createRoute(graph: KonceptNavRoute.GraphNavRoute, id: Int): KonceptNavDestination.NestedNavDestination {
        return KonceptNavDestination.NestedNavDestination("${graph.route}/$route/$id")
    }

    fun fromSavedState(savedStateHandle: SavedStateHandle): Int = savedStateHandle[breedIdArg] ?: -1
}