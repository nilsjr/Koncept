package de.nilsdruyen.koncept.dogs.ui.favorites

import androidx.compose.runtime.Stable
import dagger.hilt.android.lifecycle.HiltViewModel
import de.nilsdruyen.koncept.common.ui.ImmutableList
import de.nilsdruyen.koncept.common.ui.base.BaseViewModel
import de.nilsdruyen.koncept.common.ui.emptyImmutableList
import de.nilsdruyen.koncept.dogs.domain.usecase.GetFavoritesUseCase
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.domain.Logger
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase,
) : BaseViewModel<FavoritesState, FavoritesIntent, Nothing>(FavoritesState(true)) {

    override fun initalize() {
        loadFavorites()
    }

    override fun handleIntent(intent: FavoritesIntent) {
        when (intent) {
            is FavoritesIntent.Remove -> { }
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
)

sealed interface FavoritesIntent {

    data class Remove(val dogId: String) : FavoritesIntent
}