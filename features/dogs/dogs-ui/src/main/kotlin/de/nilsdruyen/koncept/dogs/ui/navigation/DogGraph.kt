package de.nilsdruyen.koncept.dogs.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import soup.compose.material.motion.navigation.composable
import androidx.navigation.navArgument
import de.nilsdruyen.koncept.base.navigation.OnNavigate
import de.nilsdruyen.koncept.dogs.ui.detail.BreedDetail
import de.nilsdruyen.koncept.dogs.ui.detail.image.ImageDetail
import de.nilsdruyen.koncept.dogs.ui.favorites.Favorites
import de.nilsdruyen.koncept.dogs.ui.list.DogListScreen
import soup.compose.material.motion.materialElevationScaleIn
import soup.compose.material.motion.materialElevationScaleOut
import soup.compose.material.motion.materialFadeThroughIn
import soup.compose.material.motion.materialFadeThroughOut
import soup.compose.material.motion.materialSharedAxisXIn
import soup.compose.material.motion.materialSharedAxisXOut

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.dogGraph(
    onNavigate: OnNavigate,
) {
    composable(
        route = BreedListDestination.route,
        enterMotionSpec = { materialFadeThroughIn() },
        exitMotionSpec = { materialFadeThroughOut() },
    ) {
        DogListScreen(onNavigate)
    }
    composable(
        route = FavoritesDestination.route,
        enterMotionSpec = { materialFadeThroughIn() },
        exitMotionSpec = { materialFadeThroughOut() },
    ) {
        Favorites(onNavigate)
    }
    composable(
        route = BreedDetailsDestination.route,
        enterMotionSpec = { materialSharedAxisXIn() },
        exitMotionSpec = { materialSharedAxisXOut() },
        arguments = listOf(
            navArgument(BreedDetailsDestination.breedIdArg) {
                type = NavType.IntType
            }
        )
    ) {
        BreedDetail(onNavigate)
    }
    composable(
        route = ImageDetailDestination.route,
        enterMotionSpec = { materialElevationScaleIn() },
        exitMotionSpec = { materialElevationScaleOut() },
        arguments = listOf(
            navArgument(ImageDetailDestination.urlArg) {
                type = NavType.StringType
            }
        ),
    ) { backStackEntry ->
        ImageDetail(ImageDetailDestination.fromBackStackEntry(backStackEntry))
    }
}