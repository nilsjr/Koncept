package de.nilsdruyen.koncept.dogs.ui.list

import dagger.hilt.android.lifecycle.HiltViewModel
import de.nilsdruyen.koncept.dogs.domain.usecase.GetDogListUseCase
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.dogs.ui.base.BaseViewModel
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.domain.Logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class DogListViewModel @Inject constructor(
    private val getDogListUseCase: GetDogListUseCase,
) : BaseViewModel<DogListState, DogListIntent, Effect>() {

    override fun initalState(): DogListState = DogListState(isLoading = true)

    override fun handleIntent(intent: DogListIntent) {
        when (intent) {
            DogListIntent.LoadIntent -> loadList()
            is DogListIntent.ShowDetailAndSaveListPosition -> navigateToDetail(intent.id)
            DogListIntent.StartLongTask -> startTask()
        }
    }

    private fun loadList() {
        launchOnUi {
            getDogListUseCase.execute().collect { result ->
                result.fold(this@DogListViewModel::handleError) {
                    Logger.log("set list ${it.size}")
                    setState {
                        copy(isLoading = false, list = it)
                    }
                }
            }
        }
    }

    private fun startTask() {
        launchDistinct(JobKey.LONG_TASK) {
            flow {
                repeat(40) {
                    delay(500)
                    emit(it)
                }
            }.collect {
                Logger.log("collect $it")
            }
        }
    }

    private fun navigateToDetail(id: Int) {
        sendEffect(Effect.NavigateToDetail(id))
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