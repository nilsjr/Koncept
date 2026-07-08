@file:OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)

package de.nilsdruyen.koncept.dogs.ui.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import de.nilsdruyen.koncept.base.navigation.Navigator
import de.nilsdruyen.koncept.dogs.entity.BreedSortType
import de.nilsdruyen.koncept.dogs.ui.detail.BreedDetailScreen
import de.nilsdruyen.koncept.dogs.ui.detail.BreedDetailViewModel
import de.nilsdruyen.koncept.dogs.ui.favorites.Favorites
import de.nilsdruyen.koncept.dogs.ui.image.ImageDetailScreen
import de.nilsdruyen.koncept.dogs.ui.list.DogListScreen
import de.nilsdruyen.koncept.dogs.ui.list.components.DogListSortDialog
import kotlinx.serialization.Serializable

@Serializable
data object DogListRoute : NavKey

@Serializable
data object FavoritesRoute : NavKey

@Serializable
data class DogDetailRoute(val id: Int) : NavKey

@Serializable
data class ImageRoute(val id: String) : NavKey

fun EntryProviderScope<NavKey>.dogRoutes(navigator: Navigator, sharedTransitionScope: SharedTransitionScope) {
    entry<DogListRoute> {
        var sortType by rememberSaveable { mutableIntStateOf(BreedSortType.Name.ordinal) }
        var sortDialogType by rememberSaveable { mutableStateOf<BreedSortType?>(null) }
        DogListScreen(
            sortTypeState = sortType,
            showDetail = { id ->
                navigator.goTo(DogDetailRoute(id.value))
            },
            showSortDialog = { type ->
                sortDialogType = type
            },
        )
        sortDialogType?.let { selectedType ->
            ModalBottomSheet(onDismissRequest = { sortDialogType = null }) {
                DogListSortDialog(
                    selectedType = selectedType,
                    setResult = { type ->
                        sortType = type.ordinal
                        sortDialogType = null
                    },
                )
            }
        }
    }
    entry<DogDetailRoute> { key ->
        val viewModel = hiltViewModel<BreedDetailViewModel, BreedDetailViewModel.Factory>(
            creationCallback = { factory ->
                factory.create(key)
            },
        )
        BreedDetailScreen(
            viewModel = viewModel,
            sharedTransitionScope = sharedTransitionScope,
            showImageDetail = {
                navigator.goTo(ImageRoute(it))
            },
        )
    }
    entry<ImageRoute> { key ->
        with(sharedTransitionScope) {
            ImageDetailScreen(
                id = key.id,
                imageModifier = Modifier.sharedElement(
                    sharedTransitionScope.rememberSharedContentState(key = "image-${key.id}"),
                    animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                ),
            )
        }
    }
    entry<FavoritesRoute> {
        Favorites(
            showBreed = {
                navigator.goTo(DogDetailRoute(it))
            },
        )
    }
}
