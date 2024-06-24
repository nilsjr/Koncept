package de.nilsdruyen.koncept.dogs.ui.favorites

import dagger.hilt.android.lifecycle.HiltViewModel
import de.nilsdruyen.koncept.common.ui.ImmutableList
import de.nilsdruyen.koncept.common.ui.base.MviViewModel
import de.nilsdruyen.koncept.common.ui.emptyImmutableList
import de.nilsdruyen.koncept.dogs.domain.usecase.GetFavoritesUseCase
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.domain.Logger
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase,
) : MviViewModel<FavoritesState, FavoritesIntent>(FavoritesState(true)) {

    override fun initialize() {
        loadFavorites()
    }

    override suspend fun onIntent(intent: FavoritesIntent) {
        when (intent) {
            is FavoritesIntent.Remove -> {}
            FavoritesIntent.NavigationConsumed -> updateState {
                copy(showBreed = null)
            }
            is FavoritesIntent.ShowBreed -> updateState {
                copy(showBreed = intent.id)
            }
        }
    }

    private fun loadFavorites() {
        launchOnUi {
            getFavoritesUseCase.execute().collect {
                it.fold(::handleError) {
                    updateState {
                        copy(isLoading = false, list = ImmutableList(it))
                    }
                }
            }
        }
    }

    private fun handleError(dataSourceError: DataSourceError) {
        Logger.log(dataSourceError.toString())
    }
}

data class FavoritesState(
    val isLoading: Boolean = false,
    val list: ImmutableList<Dog> = emptyImmutableList(),
    val showBreed: Int? = null,
)

sealed interface FavoritesIntent {

    data class Remove(val dogId: String) : FavoritesIntent
    data class ShowBreed(val id: Int) : FavoritesIntent
    object NavigationConsumed : FavoritesIntent
}
