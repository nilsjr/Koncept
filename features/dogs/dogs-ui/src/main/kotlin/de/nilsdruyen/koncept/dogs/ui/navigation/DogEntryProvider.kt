package de.nilsdruyen.koncept.dogs.ui.navigation

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import de.nilsdruyen.koncept.base.navigation.Navigator
import de.nilsdruyen.koncept.dogs.entity.BreedId
import de.nilsdruyen.koncept.dogs.ui.detail.BreedDetailScreen
import de.nilsdruyen.koncept.dogs.ui.detail.BreedDetailViewModel
import de.nilsdruyen.koncept.dogs.ui.list.DogListScreen
import kotlinx.serialization.Serializable

@Serializable
data object DogListRoute : NavKey
data object FavoritesRoute : NavKey

@Serializable
data class DogDetailRoute(val id: Int) : NavKey

fun EntryProviderScope<NavKey>.dogRoutes(
    navigator: Navigator,
    sharedTransitionScope: SharedTransitionScope,
) {
    entry<DogListRoute> {
        val sortType = remember { mutableIntStateOf(1) }
        DogListScreen(
            sortTypeState = sortType,
            showDetail = { id ->
                navigator.goTo(DogDetailRoute(id.value))
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
            sharedTransitionScope = sharedTransitionScope,
            showImageDetail = {
//                navController.navigate(ImageRoute(it))
            },
        )
    }
}