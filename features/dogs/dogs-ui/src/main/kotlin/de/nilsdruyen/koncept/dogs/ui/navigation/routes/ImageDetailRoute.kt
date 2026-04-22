package de.nilsdruyen.koncept.dogs.ui.navigation.routes

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import de.nilsdruyen.koncept.base.navigation.KonceptNavDestination
import de.nilsdruyen.koncept.base.navigation.KonceptNavRoute

object ImageDetailRoute : KonceptNavRoute.NestedNavRoute {

    const val IMAGE_ID_ARG = "imageId"

    override val route: String = "image"

    override fun pathParameters(): List<NamedNavArgument> = listOf(
        navArgument(IMAGE_ID_ARG) {
            type = NavType.StringType
        },
    )

    fun createRoute(graph: KonceptNavRoute.GraphNavRoute, id: String): KonceptNavDestination.NestedNavDestination =
        buildRoute(graph, id)

    fun fromBackStackEntry(backStackEntry: NavBackStackEntry): ImageDetailArgs =
        ImageDetailArgs(backStackEntry.arguments?.getString(IMAGE_ID_ARG) ?: "")
}

data class ImageDetailArgs(val imageId: String)
