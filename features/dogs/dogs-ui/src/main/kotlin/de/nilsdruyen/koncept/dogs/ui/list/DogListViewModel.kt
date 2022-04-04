package de.nilsdruyen.koncept.dogs.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.nilsdruyen.koncept.dogs.domain.usecase.GetDogListUseCase
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.dogs.ui.base.BaseViewModel
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.domain.Logger
import de.nilsdruyen.koncept.domain.annotations.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DogListViewModel @Inject constructor(
    private val getDogListUseCase: GetDogListUseCase,
    @DefaultDispatcher
    val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _state = MutableStateFlow(DogListState(isLoading = true))
    internal val state: StateFlow<DogListState> = _state

    val intent = Channel<DogListIntent>()

    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

//    override fun initalState(): DogListState = DogListState(isLoading = true)
//
//    override fun handleIntent(intent: DogListIntent) {
//        when (intent) {
//            DogListIntent.LoadIntent -> loadList()
//            is DogListIntent.ShowDetailAndSaveListPosition -> navigateToDetail(intent.id)
//            DogListIntent.StartLongTask -> startTask()
//        }
//    }

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch(dispatcher) {
            intent.consumeAsFlow().collect {
                when (it) {
                    DogListIntent.LoadIntent -> loadList()
                    is DogListIntent.ShowDetailAndSaveListPosition -> navigateToDetail(it.id)
                    DogListIntent.StartLongTask -> startTask()
                }
            }
        }
    }

    private fun loadList() {
//        launchOnUi {
        viewModelScope.launch(dispatcher) {
            getDogListUseCase.execute().collect { result ->
                result.fold(this@DogListViewModel::handleError) {
                    Logger.log("set list ${it.size}")
//                    setState {
//                        copy(list = it)
//                    }
                }
            }
        }
    }

    private fun startTask() {
//        launchDistinct(BaseViewModel.JobKey.LONG_TASK) {
//            flow {
//                repeat(40) {
//                    delay(500)
//                    emit(it)
//                }
//            }.collect {
//                Logger.log("collect $it")
//            }
//        }
    }

    private fun navigateToDetail(id: Int) {
//        sendEffect(Effect.NavigateToDetail(id))
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
    object StartLongTask : DogListIntent()
}

sealed class Effect {
    data class NavigateToDetail(val breedId: Int) : Effect()
}