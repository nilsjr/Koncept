@file:OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalAnimationApi::class)

package de.nilsdruyen.koncept.dogs.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import de.nilsdruyen.koncept.base.navigation.OnNavigate
import de.nilsdruyen.koncept.dogs.entity.BreedSortType
import de.nilsdruyen.koncept.dogs.ui.detail.BreedDetail
import de.nilsdruyen.koncept.dogs.ui.detail.image.ImageDetail
import de.nilsdruyen.koncept.dogs.ui.list.DogListScreen
import de.nilsdruyen.koncept.dogs.ui.list.DogListSortDialog
import soup.compose.material.motion.materialElevationScaleIn
import soup.compose.material.motion.materialElevationScaleOut
import soup.compose.material.motion.materialFadeThroughIn
import soup.compose.material.motion.materialFadeThroughOut
import soup.compose.material.motion.materialSharedAxisXIn
import soup.compose.material.motion.materialSharedAxisXOut
import soup.compose.material.motion.navigation.composable

fun NavGraphBuilder.dogTopLevelGraph(
    onNavigate: OnNavigate,
    setSortResult: (BreedSortType) -> Unit,
) {
    navigation(
        route = BreedTopLevel.root,
        startDestination = BreedListDestination.createRoute(BreedTopLevel.root),
    ) {
        val root = BreedTopLevel.root
        addBreedList(root, onNavigate)
        addBreedDetail(root, onNavigate)
        addImageDetail(root)
        addFavorite(root, onNavigate)
        addBreedSortBottomSheet(root, setSortResult)
    }
}

fun NavGraphBuilder.dogDetailGraph(root: String, onNavigate: OnNavigate) {
    addBreedDetail(root, onNavigate)
    addImageDetail(root)
}

@OptIn(ExperimentalLifecycleComposeApi::class)
fun NavGraphBuilder.addBreedList(
    root: String,
    onNavigate: OnNavigate,
) {
    composable(
        route = BreedListDestination.createRoute(root),
        enterMotionSpec = { materialFadeThroughIn() },
        exitMotionSpec = { materialFadeThroughOut() },
    ) {
        val sortTypeState =
            it.savedStateHandle.getStateFlow(BreedListDestination.sortTypeResult, 0)
                .collectAsStateWithLifecycle()
        DogListScreen(
            sortTypeState = sortTypeState,
            showDetail = { id ->
                onNavigate(BreedDetailsDestination.navigate(root, id))
            },
            showSortDialog = { onNavigate(BreedListSortDialog.navigate(root, it)) }
        )
    }
}

fun NavGraphBuilder.addBreedDetail(
    root: String,
    onNavigate: OnNavigate,
) {
    composable(
        route = BreedDetailsDestination.createRoute(root),
        enterMotionSpec = { materialSharedAxisXIn() },
        exitMotionSpec = { materialSharedAxisXOut() },
        arguments = listOf(
            navArgument(BreedDetailsDestination.breedIdArg) {
                type = NavType.IntType
            }
        )
    ) {
        BreedDetail(showImageDetail = {
            onNavigate(ImageDetailDestination.navigate(root, it))
        })
    }
}

fun NavGraphBuilder.addImageDetail(root: String) {
    composable(
        route = ImageDetailDestination.createRoute(root),
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

fun NavGraphBuilder.addBreedSortBottomSheet(root: String, setSortResult: (BreedSortType) -> Unit) {
    bottomSheet(
        route = BreedListSortDialog.createRoute(root),
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