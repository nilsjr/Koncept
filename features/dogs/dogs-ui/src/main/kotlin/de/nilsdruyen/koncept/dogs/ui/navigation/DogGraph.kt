package de.nilsdruyen.koncept.dogs.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import de.nilsdruyen.koncept.base.navigation.OnNavigate
import de.nilsdruyen.koncept.dogs.entity.BreedSortType
import de.nilsdruyen.koncept.dogs.ui.detail.BreedDetail
import de.nilsdruyen.koncept.dogs.ui.detail.image.ImageDetail
import de.nilsdruyen.koncept.dogs.ui.favorites.Favorites
import de.nilsdruyen.koncept.dogs.ui.list.DogListScreen
import de.nilsdruyen.koncept.dogs.ui.list.DogListSortDialog
import soup.compose.material.motion.materialElevationScaleIn
import soup.compose.material.motion.materialElevationScaleOut
import soup.compose.material.motion.materialFadeThroughIn
import soup.compose.material.motion.materialFadeThroughOut
import soup.compose.material.motion.materialSharedAxisXIn
import soup.compose.material.motion.materialSharedAxisXOut
import soup.compose.material.motion.navigation.composable

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
fun NavGraphBuilder.dogGraph(
    onNavigate: OnNavigate,
    setSortResult: (BreedSortType) -> Unit,
) {
    composable(
        route = BreedListDestination.route,
        enterMotionSpec = { materialFadeThroughIn() },
        exitMotionSpec = { materialFadeThroughOut() },
    ) {
        val sortTypeState =
            it.savedStateHandle.getStateFlow(BreedListDestination.sortTypeResult, 0)
                .collectAsState()
        DogListScreen(onNavigate = onNavigate, sortTypeState = sortTypeState)
    }
    composable(
        route = FavoritesDestination.route,
        enterMotionSpec = { materialFadeThroughIn() },
        exitMotionSpec = { materialFadeThroughOut() },
    ) {
        Favorites()
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
    bottomSheet(
        route = BreedListSortDialog.route,
        arguments = listOf(
            navArgument(BreedListSortDialog.selectedTypeArg) {
                type = NavType.IntType
                defaultValue = 0
            }
        )
    ) {
        DogListSortDialog(BreedListSortDialog.fromNavBackStackEntry(it), setSortResult)
    }
}