package de.nilsdruyen.koncept.dogs.ui.navigation

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import de.nilsdruyen.koncept.base.navigation.Navigator
import de.nilsdruyen.koncept.base.navigation.Screen
import de.nilsdruyen.koncept.dogs.ui.detail.BreedDetailScreen
import de.nilsdruyen.koncept.dogs.ui.detail.BreedDetailViewModel
import de.nilsdruyen.koncept.dogs.ui.list.DogListScreen
import kotlinx.serialization.Serializable

@Serializable
data object DogListRoute : Screen
data object FavoritesRoute : Screen

@Serializable
data class DogDetailRoute(val id: Int) : Screen

fun EntryProviderScope<NavKey>.dogRoutes(
//    navigator: Navigator,
) {
    entry<DogListRoute> {
        val sortType = remember { mutableIntStateOf(1) }
        DogListScreen(
            sortTypeState = sortType,
            showDetail = { id ->
                //                        onNavigate(BreedDetailsRoute.createRoute(BreedListRoute, id))
            },
            showSortDialog = { type ->
                //                        onNavigate(BreedListSortDialogRoute.createRoute(BreedListRoute, type))
            }
        )
    }
    entry<DogDetailRoute> { key ->
        val viewModel = hiltViewModel<BreedDetailViewModel, BreedDetailViewModel.Factory>(
            creationCallback = { factory ->
                factory.create(key)
            }
        )
        BreedDetailScreen(
            viewModel = viewModel,
            showImageDetail = {
//                navController.navigate(ImageRoute(it))
            },
//            sharedTransitionScope = sharedTransitionScope,
//            animatedContentScope = this,
        )
    }
}