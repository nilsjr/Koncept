@file:OptIn(ExperimentalAnimationApi::class)

package de.nilsdruyen.koncept.dogs.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import de.nilsdruyen.koncept.base.navigation.OnNavigate
import de.nilsdruyen.koncept.dogs.ui.favorites.Favorites
import soup.compose.material.motion.animation.materialFadeThroughIn
import soup.compose.material.motion.animation.materialFadeThroughOut
import soup.compose.material.motion.navigation.composable

object FavoriteTopLevel {

    const val root = "favorite"
}

fun NavGraphBuilder.favoriteTopLevelGraph(
    onNavigate: OnNavigate,
    slideDistance: Int,
) {
    navigation(
        route = FavoriteTopLevel.root,
        startDestination = FavoritesDestination.createRoute(FavoriteTopLevel.root),
    ) {
        addFavorite(FavoriteTopLevel.root, onNavigate)
        dogDetailGraph(FavoriteTopLevel.root, onNavigate, slideDistance)
    }
}

fun NavGraphBuilder.addFavorite(root: String, onNavigate: OnNavigate) {
    composable(
        route = FavoritesDestination.createRoute(root),
        enterTransition = { materialFadeThroughIn() },
        exitTransition = { materialFadeThroughOut() },
    ) {
        Favorites()
    }
}