package de.nilsdruyen.koncept.dogs.ui.navigation.routes

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import de.nilsdruyen.koncept.base.navigation.KonceptNavDestination
import de.nilsdruyen.koncept.base.navigation.KonceptNavRoute
import de.nilsdruyen.koncept.dogs.entity.BreedId

object BreedDetailsRoute : KonceptNavRoute.NestedNavRoute {

    private const val BREED_ID_ARG = "breedId"

    override val route: String = "breed_detail"

    override fun pathParameters(): List<NamedNavArgument> = listOf(
        navArgument(BREED_ID_ARG) {
            type = NavType.IntType
        },
    )

    fun createRoute(id: BreedId): KonceptNavDestination.NestedNavDestination {
//        return KonceptNavDestination.NestedNavDestination("${graph.route}/$route/${id.value}")
        return KonceptNavDestination.NestedNavDestination("$route/${id.value}")
    }

    fun fromSavedState(savedStateHandle: SavedStateHandle): BreedId = BreedId(savedStateHandle[BREED_ID_ARG] ?: -1)
}
