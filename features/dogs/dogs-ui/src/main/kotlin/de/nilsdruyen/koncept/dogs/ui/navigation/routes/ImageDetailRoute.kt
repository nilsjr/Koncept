package de.nilsdruyen.koncept.dogs.ui.navigation.routes

import androidx.navigation.NavBackStackEntry
import de.nilsdruyen.koncept.base.navigation.KonceptNavDestination
import de.nilsdruyen.koncept.base.navigation.KonceptNavRoute

class ImageDetailRoute(baseRoute: String) : KonceptNavRoute.NestedNavRoute(baseRoute) {

    companion object {

        const val breedIdArg = "breedId"

        fun fromBackStackEntry(backStackEntry: NavBackStackEntry): ImageDetailArgs {
            return ImageDetailArgs(backStackEntry.arguments?.getString(breedIdArg) ?: "")
        }
    }

    override val route: String = "image"

    override fun pathParameters(): List<String> = listOf(breedIdArg)
}

data class ImageDetailArgs(val breedId: String)

fun ImageDetailRoute.navigate(id: String): KonceptNavDestination {
    return KonceptNavDestination.NestedNavDestination("$routePrefix/$id")
}