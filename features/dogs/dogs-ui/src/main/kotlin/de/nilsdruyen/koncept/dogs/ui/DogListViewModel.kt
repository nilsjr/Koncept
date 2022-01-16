package de.nilsdruyen.koncept.dogs.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.domain.Dog
import de.nilsdruyen.koncept.domain.GetDogListUseCase
import de.nilsdruyen.koncept.domain.Logger
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DogListViewModel @Inject constructor(
    val getDogListUseCase: GetDogListUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DogListState(isLoading = true))
    internal val state: StateFlow<DogListState>
        get() = _state

    val intent = Channel<DogListIntent>(Channel.CONFLATED)

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
                    is DogListIntent.ShowDogDetail -> {
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
                    _state.value = DogListState(it)
                }
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
    data class ShowDogDetail(val id: String) : DogListIntent()
}