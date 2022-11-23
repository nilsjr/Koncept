package de.nilsdruyen.koncept.dogs.ui.navigation.routes

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import de.nilsdruyen.koncept.base.navigation.KonceptNavDestination
import de.nilsdruyen.koncept.base.navigation.KonceptNavRoute
import de.nilsdruyen.koncept.dogs.ui.navigation.BreedListNavigation

object BreedDetailsRoute : KonceptNavRoute.NestedNavRoute {

//    companion object {

//        fun detail() = de.nilsdruyen.koncept.dogs.ui.navigation.routes.BreedDetailsRoute(BreedListNavigation.baseRoute).getNestedRoute()
//
//        fun detail(id: Int) = de.nilsdruyen.koncept.dogs.ui.navigation.routes.BreedDetailsRoute(BreedListNavigation.baseRoute)
//            .navigate(id)
//    }
    const val breedIdArg = "breedId"

    fun fromSavedState(savedStateHandle: SavedStateHandle): Int {
        return savedStateHandle[breedIdArg] ?: -1
    }

    override val route: String = "breed_detail"

    override fun pathParameters(): List<NamedNavArgument> = listOf(
        navArgument(breedIdArg) {
            type = NavType.IntType
        }
    )
}

//fun BreedDetailsRoute.navigate(id: Int): KonceptNavDestination {
//    return KonceptNavDestination.NestedNavDestination("$routePrefix/$id")
//}