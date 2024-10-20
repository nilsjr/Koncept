package de.nilsdruyen.koncept.dogs.ui.navigation.graph

import androidx.navigation.NavGraphBuilder
import de.nilsdruyen.koncept.base.navigation.KonceptNavRoute
import de.nilsdruyen.koncept.base.navigation.NavigateTo
import de.nilsdruyen.koncept.base.navigation.konceptComposable
import de.nilsdruyen.koncept.base.navigation.navigation
import de.nilsdruyen.koncept.dogs.entity.BreedId
import de.nilsdruyen.koncept.dogs.ui.favorites.Favorites
import de.nilsdruyen.koncept.dogs.ui.navigation.routes.BreedDetailsRoute
import de.nilsdruyen.koncept.dogs.ui.navigation.routes.FavoritesRoute

fun NavGraphBuilder.favoriteTopLevelGraph(
    onNavigate: NavigateTo,
    nestedGraph: (base: KonceptNavRoute.GraphNavRoute) -> Unit
) = navigation(navRoute = FavoritesRoute) {
    konceptComposable(
        navRoute = FavoritesRoute,
    ) {
        Favorites(showBreed = { onNavigate(BreedDetailsRoute.createRoute(FavoritesRoute, BreedId(it))) })
    }
    nestedGraph(FavoritesRoute)
}
