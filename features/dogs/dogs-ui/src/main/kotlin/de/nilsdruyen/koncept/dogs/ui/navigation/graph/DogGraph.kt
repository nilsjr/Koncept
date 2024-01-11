@file:OptIn(
    ExperimentalMaterialNavigationApi::class,
)

package de.nilsdruyen.koncept.dogs.ui.navigation.graph

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import de.nilsdruyen.koncept.base.navigation.KonceptNavRoute
import de.nilsdruyen.koncept.base.navigation.NavigateTo
import de.nilsdruyen.koncept.base.navigation.NestedGraph
import de.nilsdruyen.koncept.base.navigation.bottomSheet
import de.nilsdruyen.koncept.base.navigation.konceptComposable
import de.nilsdruyen.koncept.base.navigation.navigation
import de.nilsdruyen.koncept.dogs.entity.BreedSortType
import de.nilsdruyen.koncept.dogs.ui.detail.BreedDetail
import de.nilsdruyen.koncept.dogs.ui.detail.image.ImageDetail
import de.nilsdruyen.koncept.dogs.ui.list.DogListScreen
import de.nilsdruyen.koncept.dogs.ui.list.DogListSortDialog
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

fun NavGraphBuilder.breedTopLevelGraph(
    onNavigate: NavigateTo,
    setSortResult: (BreedSortType) -> Unit,
    slideDistance: Int,
    nestedGraphs: NestedGraph = {},
) {
    navigation(navRoute = BreedListRoute) {
        konceptComposable(
            navRoute = BreedListRoute,
            enterTransition = { materialFadeThroughIn() },
            exitTransition = { materialFadeThroughOut() },
        ) {
            val sortTypeState =
                it.savedStateHandle.getStateFlow(BreedListRoute.sortTypeResult, 0)
                    .collectAsStateWithLifecycle()
            DogListScreen(
                sortTypeState = sortTypeState,
                showDetail = { id ->
                    onNavigate(BreedDetailsRoute.createRoute(BreedListRoute, id))
                },
                showSortDialog = { type ->
                    onNavigate(BreedListSortDialogRoute.createRoute(BreedListRoute, type))
                }
            )
        }
        breedDetailGraph(BreedListRoute, onNavigate, slideDistance)
        addBreedSortBottomSheet(BreedListRoute, setSortResult)
        nestedGraphs(BreedListRoute)
    }
}

fun NavGraphBuilder.breedDetailGraph(base: KonceptNavRoute.GraphNavRoute, onNavigate: NavigateTo, slideDistance: Int) {
//    addBreedDetail(base, onNavigate, slideDistance)
    addImageDetail(base)
}

fun NavGraphBuilder.addBreedDetail(base: KonceptNavRoute.GraphNavRoute, onNavigate: NavigateTo, slideDistance: Int) {
    konceptComposable(
        navRoute = BreedDetailsRoute,
        graphRoute = base,
        enterTransition = { materialSharedAxisXIn(true, slideDistance) },
        exitTransition = { materialSharedAxisXOut(false, slideDistance) },
    ) {
        BreedDetail(showImageDetail = {
            onNavigate(ImageDetailRoute.createRoute(base, it))
        })
    }
}

fun NavGraphBuilder.addImageDetail(base: KonceptNavRoute.GraphNavRoute) {
    konceptComposable(
        navRoute = ImageDetailRoute,
        graphRoute = base,
        enterTransition = { materialElevationScaleIn() },
        exitTransition = { materialElevationScaleOut() },
    ) { backStackEntry ->
        ImageDetail(ImageDetailRoute.fromBackStackEntry(backStackEntry).imageId)
    }
}

@OptIn(ExperimentalMaterialNavigationApi::class)
fun NavGraphBuilder.addBreedSortBottomSheet(
    base: KonceptNavRoute.GraphNavRoute,
    setSortResult: (BreedSortType) -> Unit
) = bottomSheet(navRoute = BreedListSortDialogRoute, graphRoute = base) {
    DogListSortDialog(BreedListSortDialogRoute.fromNavBackStackEntry(it), setSortResult)
}