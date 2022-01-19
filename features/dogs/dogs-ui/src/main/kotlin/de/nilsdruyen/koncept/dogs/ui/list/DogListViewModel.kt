package de.nilsdruyen.koncept.dogs.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.computations.ResultEffect.bind
import dagger.hilt.android.lifecycle.HiltViewModel
import de.nilsdruyen.koncept.dogs.domain.usecase.GetDogListUseCase
import de.nilsdruyen.koncept.dogs.domain.usecase.GetListPositionUseCase
import de.nilsdruyen.koncept.dogs.domain.usecase.SaveListPositionUseCase
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.domain.Logger
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DogListViewModel @Inject constructor(
    private val getDogListUseCase: GetDogListUseCase,
    private val saveListPositionUseCase: SaveListPositionUseCase,
    private val getListPositionUseCase: GetListPositionUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(DogListState(isLoading = true))
    internal val state: StateFlow<DogListState>
        get() = _state

    val intent = Channel<DogListIntent>()

    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when (it) {
                    DogListIntent.LoadIntent -> {
                        loadList()
                    }
                    is DogListIntent.ShowDetailAndSaveListPosition -> {
                        val (index, offset) = it.listPosition
                        saveListPositionUseCase.execute("breedList", index, offset)
                        _effect.send(Effect.NavigateToDetail(it.id))
                    }
                }
            }
        }
    }

    private fun loadList() {
        viewModelScope.launch {
            getDogListUseCase.execute().collect { result ->
                result.fold(this@DogListViewModel::handleError) {
                    Logger.log("set list ${it.size}")
                    val (index, offset) = getListPositionUseCase.execute("").bind()
                    _state.value = DogListState(list = it, listPosition = ListPosition(index, offset))
                    saveListPositionUseCase.execute("breedList", 0, 0)
                }
            }
        }
    }

    private fun handleError(error: DataSourceError) {
        Logger.log(error.toString())
    }
}

data class DogListState(
    val listPosition: ListPosition = ListPosition(),
    val list: List<Dog> = emptyList(),
    val isLoading: Boolean = false,
)

sealed class DogListIntent {
    object LoadIntent : DogListIntent()
    data class ShowDetailAndSaveListPosition(val id: Int, val listPosition: ListPosition) : DogListIntent()
}

sealed class Effect {
    data class NavigateToDetail(val breedId: Int) : Effect()
}

data class ListPosition(
    val index: Int = 0,
    val offset: Int = 0,
)