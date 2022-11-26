package de.nilsdruyen.koncept.dogs.ui.navigation.routes

import androidx.lifecycle.SavedStateHandle
import de.nilsdruyen.koncept.base.navigation.KonceptNavDestination
import de.nilsdruyen.koncept.base.navigation.KonceptNavRoute

class BreedDetailsRoute(baseRoute: String) : KonceptNavRoute.NestedNavRoute(baseRoute) {

    companion object {

        const val breedIdArg = "breedId"

        fun fromSavedState(savedStateHandle: SavedStateHandle): Int {
            return savedStateHandle[breedIdArg] ?: -1
        }
    }

    override val route: String = "breed_detail"

    override fun pathParameters(): List<String> = listOf(breedIdArg)
}

fun BreedDetailsRoute.navigate(id: Int): KonceptNavDestination {
    return KonceptNavDestination.NestedNavDestination("$routePrefix/$id")
}