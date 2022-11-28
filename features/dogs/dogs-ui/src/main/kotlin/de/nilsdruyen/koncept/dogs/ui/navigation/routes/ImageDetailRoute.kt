package de.nilsdruyen.koncept.dogs.ui.navigation.routes

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import de.nilsdruyen.koncept.base.navigation.KonceptNavDestination
import de.nilsdruyen.koncept.base.navigation.KonceptNavRoute

object ImageDetailRoute : KonceptNavRoute.NestedNavRoute {

    private const val imageIdArg = "imageId"

    override val route: String = "image"

    override fun pathParameters(): List<NamedNavArgument> = listOf(
        navArgument(imageIdArg) {
            type = NavType.StringType
        }
    )

    fun createRoute(graph: KonceptNavRoute.GraphNavRoute, id: String): KonceptNavDestination.NestedNavDestination {
        return buildRoute(graph, id)
    }

    fun fromBackStackEntry(backStackEntry: NavBackStackEntry): ImageDetailArgs {
        return ImageDetailArgs(backStackEntry.arguments?.getString(imageIdArg) ?: "")
    }
}

data class ImageDetailArgs(val imageId: String)