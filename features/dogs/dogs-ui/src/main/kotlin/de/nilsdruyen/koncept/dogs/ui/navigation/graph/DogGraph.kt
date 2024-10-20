package de.nilsdruyen.koncept.dogs.ui.navigation.graph

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import de.nilsdruyen.koncept.base.navigation.KonceptNavRoute
import de.nilsdruyen.koncept.base.navigation.NavigateTo
import de.nilsdruyen.koncept.base.navigation.NestedGraph
import de.nilsdruyen.koncept.base.navigation.konceptComposable
import de.nilsdruyen.koncept.base.navigation.navigation
import de.nilsdruyen.koncept.dogs.ui.detail.BreedDetail
import de.nilsdruyen.koncept.dogs.ui.detail.image.ImageDetail
import de.nilsdruyen.koncept.dogs.ui.list.DogListScreen
import de.nilsdruyen.koncept.dogs.ui.navigation.routes.BreedDetailsRoute
import de.nilsdruyen.koncept.dogs.ui.navigation.routes.BreedListRoute
import de.nilsdruyen.koncept.dogs.ui.navigation.routes.BreedListSortDialogRoute
import de.nilsdruyen.koncept.dogs.ui.navigation.routes.ImageDetailRoute

fun NavGraphBuilder.breedTopLevelGraph(
    onNavigate: NavigateTo,
    nestedGraphs: NestedGraph = {},
) {
    navigation(navRoute = BreedListRoute) {
        konceptComposable(
            navRoute = BreedListRoute,
        ) {
            val sortTypeState =
                it.savedStateHandle.getStateFlow(BreedListRoute.sortTypeResult, 0)
                    .collectAsStateWithLifecycle(
                        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
                    )
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
        breedDetailGraph(BreedListRoute)
//        addBreedSortBottomSheet(BreedListRoute, setSortResult)
        nestedGraphs(BreedListRoute)
    }
}

fun NavGraphBuilder.breedDetailGraph(base: KonceptNavRoute.GraphNavRoute) {
//    addBreedDetail(base, onNavigate, slideDistance)
    addImageDetail(base)
}

fun NavGraphBuilder.addBreedDetail(base: KonceptNavRoute.GraphNavRoute, onNavigate: NavigateTo) {
    konceptComposable(
        navRoute = BreedDetailsRoute,
        graphRoute = base,
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
    ) { backStackEntry ->
        ImageDetail(ImageDetailRoute.fromBackStackEntry(backStackEntry).imageId)
    }
}
