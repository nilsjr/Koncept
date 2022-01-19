package de.nilsdruyen.koncept.dogs.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.nilsdruyen.koncept.dogs.domain.usecase.GetBreedImageListUseCase
import de.nilsdruyen.koncept.dogs.entity.BreedImage
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.domain.Logger
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreedDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val getBreedImageListUseCase: GetBreedImageListUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(BreedDetailState(isLoading = true))
    internal val state: StateFlow<BreedDetailState>
        get() = _state

    private val breedId = savedStateHandle.get<Int>("breedId") ?: -1

    val intent = Channel<BreedDetailIntent>()

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when (it) {
                    BreedDetailIntent.LoadImages -> loadImages()
                    else -> {}
                }
            }
        }
    }

    private fun loadImages() {
        viewModelScope.launch {
            if (breedId > -1) {
                getBreedImageListUseCase.execute(breedId = breedId).fold(::handleError) {
                    _state.value = BreedDetailState(it)
                }
            }
        }
    }

    private fun handleError(error: DataSourceError) {
        Logger.log(error.toString())
    }
}

data class BreedDetailState(
    val images: List<BreedImage> = emptyList(),
    val isLoading: Boolean = false,
)

sealed class BreedDetailIntent {

    object LoadImages : BreedDetailIntent()
    object ShowImage : BreedDetailIntent()
}