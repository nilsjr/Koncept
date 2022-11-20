package de.nilsdruyen.koncept.dogs.ui.navigation

import de.nilsdruyen.koncept.dogs.ui.navigation.routes.BreedDetailsRoute
import de.nilsdruyen.koncept.dogs.ui.navigation.routes.FavoritesRoute
import de.nilsdruyen.koncept.dogs.ui.navigation.routes.navigate

object FavoritesNavigation {

    private val baseRoute: String = FavoritesRoute.route

    object Route {

        fun list() = FavoritesRoute.navigate()
    }

    object Destination {

        fun breedDetail(id: Int) = BreedDetailsRoute(baseRoute).navigate(id)
    }
}