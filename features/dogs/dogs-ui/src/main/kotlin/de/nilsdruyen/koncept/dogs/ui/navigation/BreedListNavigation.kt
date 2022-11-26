package de.nilsdruyen.koncept.dogs.ui.navigation

import de.nilsdruyen.koncept.dogs.entity.BreedSortType
import de.nilsdruyen.koncept.dogs.ui.navigation.routes.BreedDetailsRoute
import de.nilsdruyen.koncept.dogs.ui.navigation.routes.BreedListRoute
import de.nilsdruyen.koncept.dogs.ui.navigation.routes.BreedListSortDialogRoute
import de.nilsdruyen.koncept.dogs.ui.navigation.routes.ImageDetailRoute
import de.nilsdruyen.koncept.dogs.ui.navigation.routes.navigate

object BreedListNavigation {

    val baseRoute: String = BreedListRoute.route

    object Route {

        fun detail() = BreedDetailsRoute(baseRoute).getNestedRoute()

        fun sortDialog() = BreedListSortDialogRoute(baseRoute).getNestedRoute()

        fun imageDetail() = ImageDetailRoute(baseRoute).getNestedRoute()
    }

    object Destination {

        fun sortDialog(type: BreedSortType) = BreedListSortDialogRoute(baseRoute).navigate(type)

        fun detail(id: Int) = BreedDetailsRoute(baseRoute).navigate(id)

        fun imageDetail(id: String) = ImageDetailRoute(baseRoute).navigate(id)
    }
}