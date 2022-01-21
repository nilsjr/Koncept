package de.nilsdruyen.koncept.dogs.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.nilsdruyen.koncept.dogs.domain.usecase.GetDogListUseCase
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.domain.Logger
import de.nilsdruyen.koncept.domain.annotations.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DogListViewModel @Inject constructor(
    @DefaultDispatcher val dispatcher: CoroutineDispatcher,
    private val getDogListUseCase: GetDogListUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(DogListState(isLoading = true))
    internal val state: StateFlow<DogListState> = _state

    val intent = Channel<DogListIntent>()

    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch(dispatcher) {
            intent.consumeAsFlow().collect {
                when (it) {
                    DogListIntent.LoadIntent -> loadList()
                    is DogListIntent.ShowDetailAndSaveListPosition -> {
                        _effect.send(Effect.NavigateToDetail(it.id))
                    }
                }
            }
        }
    }

    private suspend fun loadList() {
        getDogListUseCase.execute().collect { result ->
            result.fold(this@DogListViewModel::handleError) {
                Logger.log("set list ${it.size}")
                _state.value = DogListState(list = it)
            }
        }
    }

    private fun handleError(error: DataSourceError) {
        Logger.log(error.toString())
    }
}

data class DogListState(
    val list: List<Dog> = emptyList(),
    val isLoading: Boolean = false,
)

sealed class DogListIntent {
    object LoadIntent : DogListIntent()
    data class ShowDetailAndSaveListPosition(val id: Int) : DogListIntent()
}

sealed class Effect {
    data class NavigateToDetail(val breedId: Int) : Effect()
}