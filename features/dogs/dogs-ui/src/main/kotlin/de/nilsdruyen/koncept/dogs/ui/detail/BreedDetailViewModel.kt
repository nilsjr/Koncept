package de.nilsdruyen.koncept.dogs.ui.detail

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import de.nilsdruyen.koncept.common.ui.ImmutableList
import de.nilsdruyen.koncept.common.ui.base.MviViewModel
import de.nilsdruyen.koncept.common.ui.emptyImmutableList
import de.nilsdruyen.koncept.common.ui.toImmutable
import de.nilsdruyen.koncept.dogs.domain.usecase.GetBreedImageListUseCase
import de.nilsdruyen.koncept.dogs.domain.usecase.IsFavoriteFlowUseCase
import de.nilsdruyen.koncept.dogs.domain.usecase.UpdateFavoriteBreedUseCase
import de.nilsdruyen.koncept.dogs.entity.BreedImage
import de.nilsdruyen.koncept.dogs.ui.navigation.DogDetailRoute
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.domain.Logger

@HiltViewModel(assistedFactory = BreedDetailViewModel.Factory::class)
class BreedDetailViewModel @AssistedInject constructor(
    @Assisted val route: DogDetailRoute,
    private val getBreedImageListUseCase: GetBreedImageListUseCase,
    private val updateFavoriteBreedUseCase: UpdateFavoriteBreedUseCase,
    private val isFavoriteFlowUseCase: IsFavoriteFlowUseCase,
) : MviViewModel<BreedDetailState, BreedDetailIntent>(BreedDetailState(isLoading = true)) {

    private val breedId: Int = route.id

    @AssistedFactory
    interface Factory {
        fun create(key: DogDetailRoute): BreedDetailViewModel
    }

    override fun initialize() {
        listenFavorite()
    }

    override suspend fun onIntent(intent: BreedDetailIntent) {
        when (intent) {
            BreedDetailIntent.LoadImages -> loadImages()
            BreedDetailIntent.ToggleFavorite -> toggleFavorite()
        }
    }

    private fun toggleFavorite() {
        launchOnUi {
            updateFavoriteBreedUseCase.execute(breedId, !state.value.isFavorite)
        }
    }

    private fun listenFavorite() {
        launchOnUi {
            isFavoriteFlowUseCase.execute(breedId).collect {
                updateState {
                    copy(isFavorite = it)
                }
            }
        }
    }

    private suspend fun loadImages() {
        if (breedId > -1) {
            getBreedImageListUseCase.execute(breedId = breedId).fold(::handleError) {
                updateState {
                    copy(images = it.toImmutable(), isLoading = false)
                }
            }
        } else {
            updateState {
                BreedDetailState()
            }
        }
    }

    private fun handleError(error: DataSourceError) {
        Logger.log(error.toString())
    }
}

data class BreedDetailState(
    val isLoading: Boolean = false,
    val images: ImmutableList<BreedImage> = emptyImmutableList(),
    val isFavorite: Boolean = false,
)

sealed interface BreedDetailIntent {

    object LoadImages : BreedDetailIntent
    object ToggleFavorite : BreedDetailIntent
}
