package de.nilsdruyen.koncept.dogs.ui.navigation.graph

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import de.nilsdruyen.koncept.base.navigation.NavigateTo
import de.nilsdruyen.koncept.dogs.ui.favorites.Favorites
import de.nilsdruyen.koncept.dogs.ui.navigation.FavoritesNavigation
import de.nilsdruyen.koncept.dogs.ui.navigation.routes.FavoritesRoute
import soup.compose.material.motion.animation.materialFadeThroughIn
import soup.compose.material.motion.animation.materialFadeThroughOut
import soup.compose.material.motion.navigation.composable

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.favoriteTopLevelGraph(
    onNavigate: NavigateTo,
    nestedGraph: (baseRoute: String) -> Unit
) {
    navigation(
        route = FavoritesRoute.getGraphRoute(),
        startDestination = FavoritesRoute.getStartDestination(),
    ) {
        composable(
            route = FavoritesRoute.getStartDestination(),
            enterTransition = { materialFadeThroughIn() },
            exitTransition = { materialFadeThroughOut() },
        ) {
            Favorites(showBreed = { onNavigate(FavoritesNavigation.Destination.breedDetail(id)) })
        }
        nestedGraph(FavoritesRoute.getGraphRoute())
    }
}