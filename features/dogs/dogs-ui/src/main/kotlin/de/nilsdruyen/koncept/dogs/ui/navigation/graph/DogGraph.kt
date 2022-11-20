@file:OptIn(
    ExperimentalMaterialNavigationApi::class, ExperimentalAnimationApi::class,
    ExperimentalLifecycleComposeApi::class
)

package de.nilsdruyen.koncept.dogs.ui.navigation.graph

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import de.nilsdruyen.koncept.base.navigation.NavigateTo
import de.nilsdruyen.koncept.base.navigation.NestedGraph
import de.nilsdruyen.koncept.dogs.entity.BreedSortType
import de.nilsdruyen.koncept.dogs.ui.detail.BreedDetail
import de.nilsdruyen.koncept.dogs.ui.detail.image.ImageDetail
import de.nilsdruyen.koncept.dogs.ui.list.DogListScreen
import de.nilsdruyen.koncept.dogs.ui.list.DogListSortDialog
import de.nilsdruyen.koncept.dogs.ui.navigation.BreedListNavigation
import de.nilsdruyen.koncept.dogs.ui.navigation.routes.BreedDetailsRoute
import de.nilsdruyen.koncept.dogs.ui.navigation.routes.BreedListRoute
import de.nilsdruyen.koncept.dogs.ui.navigation.routes.BreedListSortDialogRoute
import de.nilsdruyen.koncept.dogs.ui.navigation.routes.ImageDetailRoute
import soup.compose.material.motion.animation.materialElevationScaleIn
import soup.compose.material.motion.animation.materialElevationScaleOut
import soup.compose.material.motion.animation.materialFadeThroughIn
import soup.compose.material.motion.animation.materialFadeThroughOut
import soup.compose.material.motion.animation.materialSharedAxisXIn
import soup.compose.material.motion.animation.materialSharedAxisXOut
import soup.compose.material.motion.navigation.composable

fun NavGraphBuilder.dogTopLevelGraph(
    onNavigate: NavigateTo,
    setSortResult: (BreedSortType) -> Unit,
    slideDistance: Int,
    nestedGraphs: NestedGraph = {},
) {
    navigation(
        route = BreedListRoute.getGraphRoute(),
        startDestination = BreedListRoute.getStartDestination(),
    ) {
        val baseRoute = BreedListRoute.getGraphRoute()
        addBreedList(onNavigate)
        addBreedDetail(baseRoute, onNavigate, slideDistance)
        addImageDetail(baseRoute)
        addBreedSortBottomSheet(setSortResult)
        nestedGraphs(baseRoute)
    }
}

fun NavGraphBuilder.dogDetailGraph(baseRoute: String, onNavigate: NavigateTo, slideDistance: Int) {
    addBreedDetail(baseRoute, onNavigate, slideDistance)
    addImageDetail(baseRoute)
}

fun NavGraphBuilder.addBreedList(onNavigate: NavigateTo) {
    composable(
        route = BreedListRoute.getStartDestination(),
        enterTransition = { materialFadeThroughIn() },
        exitTransition = { materialFadeThroughOut() },
    ) {
        val sortTypeState =
            it.savedStateHandle.getStateFlow(BreedListRoute.sortTypeResult, 0)
                .collectAsStateWithLifecycle()
        DogListScreen(
            sortTypeState = sortTypeState,
            showDetail = { id ->
                onNavigate(BreedListNavigation.Destination.detail(id))
            },
            showSortDialog = { type ->
                onNavigate(BreedListNavigation.Destination.sortDialog(type))
            }
        )
    }
}

fun NavGraphBuilder.addBreedDetail(
    baseRoute: String,
    onNavigate: NavigateTo,
    slideDistance: Int,
) {
    composable(
        route = BreedListNavigation.Route.detail(),
        enterTransition = { materialSharedAxisXIn(true, slideDistance) },
        exitTransition = { materialSharedAxisXOut(false, slideDistance) },
        arguments = listOf(
            navArgument(BreedDetailsRoute.breedIdArg) {
                type = NavType.IntType
            }
        )
    ) {
        BreedDetail(showImageDetail = {
            onNavigate(BreedListNavigation.Destination.imageDetail(it))
        })
    }
}

fun NavGraphBuilder.addImageDetail(baseRoute: String) {
    composable(
        route = BreedListNavigation.Route.imageDetail(),
        enterTransition = { materialElevationScaleIn() },
        exitTransition = { materialElevationScaleOut() },
        arguments = listOf(
            navArgument(ImageDetailRoute.breedIdArg) {
                type = NavType.StringType
            }
        ),
    ) { backStackEntry ->
        ImageDetail(ImageDetailRoute.fromBackStackEntry(backStackEntry).breedId)
    }
}

fun NavGraphBuilder.addBreedSortBottomSheet(setSortResult: (BreedSortType) -> Unit) {
    bottomSheet(
        route = BreedListNavigation.Route.sortDialog(),
        arguments = listOf(
            navArgument(BreedListSortDialogRoute.selectedTypeArg) {
                type = NavType.IntType
                defaultValue = 0
            }
        )
    ) {
        DogListSortDialog(BreedListSortDialogRoute.fromNavBackStackEntry(it), setSortResult)
    }
}