package de.nilsdruyen.koncept.dogs.ui.favorites

import de.nilsdruyen.koncept.common.ui.base.BaseViewModel
import de.nilsdruyen.koncept.dogs.entity.Dog
import javax.inject.Inject

class FavoritesViewModel @Inject constructor() :
    BaseViewModel<FavoritesState, FavoritesIntent, Nothing>(FavoritesState(true)) {

    override fun initalize() {
        loadFavorites()
    }

    override fun handleIntent(intent: FavoritesIntent) {
        when (intent) {
            is FavoritesIntent.Remove -> {
                launchOnUi {
                    // TODO: implement
                }
            }
        }
    }

    private fun loadFavorites() {

    }
}

data class FavoritesState(
    val isLoading: Boolean = false,
    val list: List<Dog> = emptyList(),
)

sealed interface FavoritesIntent {

    data class Remove(val dogId: String) : FavoritesIntent
}