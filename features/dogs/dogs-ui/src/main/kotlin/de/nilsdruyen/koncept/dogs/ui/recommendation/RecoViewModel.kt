package de.nilsdruyen.koncept.dogs.ui.recommendation

import androidx.lifecycle.viewModelScope
import de.nilsdruyen.koncept.common.ui.base.MviViewModel
import de.nilsdruyen.koncept.dogs.domain.usecase.GetRecoUseCase
import de.nilsdruyen.koncept.dogs.domain.usecase.UpdateFavoriteBreedUseCase
import de.nilsdruyen.koncept.dogs.entity.BreedId
import de.nilsdruyen.koncept.dogs.entity.Dog
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface RecoIntent {

    data class ShowDog(val id: String) : RecoIntent
    data class SetFavorite(val id: BreedId) : RecoIntent
    data class RemoveFavorite(val id: BreedId) : RecoIntent
}

data class RecoState(
    val isLoading: Boolean,
    val recoList: List<Dog> = emptyList(),
)

internal class RecoViewModel @Inject constructor(
    private val getRecoUseCase: GetRecoUseCase,
    private val updateFavoriteBreedUseCase: UpdateFavoriteBreedUseCase,
) : MviViewModel<RecoState, RecoIntent>(RecoState(true)), RecoDelegate {

    override fun initialize() {
        loadReco()
    }

    // FIX: sentIntent as interface clashes with vm sendIntent
    override fun action(intent: RecoIntent) {
        sendIntent(intent)
    }

    override suspend fun onIntent(intent: RecoIntent) {
        when (intent) {
            is RecoIntent.RemoveFavorite -> {
                updateFavoriteBreedUseCase.execute(intent.id.value, false)
            }
            is RecoIntent.SetFavorite -> {
                updateFavoriteBreedUseCase.execute(intent.id.value, true)
            }
            is RecoIntent.ShowDog -> TODO()
        }
    }

    override fun recoState(): StateFlow<RecoState> = state

    private fun loadReco() {
        viewModelScope.launch {
            getRecoUseCase.execute().collect {
                updateState {
                    copy(recoList = it)
                }
            }
        }
    }
}