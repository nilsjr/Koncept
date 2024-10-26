package de.nilsdruyen.koncept.dogs.ui.navigation.graph

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import de.nilsdruyen.koncept.base.navigation.NavigateTo
import de.nilsdruyen.koncept.base.navigation.NestedGraph
import de.nilsdruyen.koncept.dogs.ui.list.DogGraph
import de.nilsdruyen.koncept.dogs.ui.list.DogListRoute
import de.nilsdruyen.koncept.dogs.ui.list.DogListScreen
import de.nilsdruyen.koncept.dogs.ui.navigation.routes.BreedDetailsRoute
import de.nilsdruyen.koncept.dogs.ui.navigation.routes.BreedListRoute
import de.nilsdruyen.koncept.dogs.ui.navigation.routes.BreedListSortDialogRoute

fun NavGraphBuilder.breedTopLevelGraph(
    onNavigate: NavigateTo,
    nestedGraphs: NestedGraph = {},
) {
    navigation<DogGraph>(startDestination = DogListRoute) {
        composable<DogListRoute> {
            val sortTypeState =
                it.savedStateHandle.getStateFlow(BreedListRoute.sortTypeResult, 0).collectAsStateWithLifecycle()
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
//        breedDetailGraph(BreedListRoute)
//        addBreedSortBottomSheet(BreedListRoute, setSortResult)
        nestedGraphs(BreedListRoute)
    }
}

//fun NavGraphBuilder.breedDetailGraph(base: KonceptNavRoute.GraphNavRoute) { //    addBreedDetail(base, onNavigate,
//slideDistance)
//    addImageDetail(base)
//}
//
//fun NavGraphBuilder.addImageDetail(base: KonceptNavRoute.GraphNavRoute) {
//    konceptComposable(
//        navRoute = ImageDetailRoute,
//        graphRoute = base,
//    ) { backStackEntry ->
//        ImageDetailScreen(ImageDetailRoute.fromBackStackEntry(backStackEntry).imageId)
//    }
//}
