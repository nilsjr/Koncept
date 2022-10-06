package de.nilsdruyen.koncept.dogs.ui.list

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.nilsdruyen.koncept.common.ui.ImmutableList
import de.nilsdruyen.koncept.common.ui.base.BaseViewModel
import de.nilsdruyen.koncept.common.ui.emptyImmutableList
import de.nilsdruyen.koncept.common.ui.toImmutable
import de.nilsdruyen.koncept.dogs.domain.usecase.GetDogListUseCase
import de.nilsdruyen.koncept.dogs.entity.BreedSortType
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.domain.Logger.Companion.log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DogListViewModel @Inject constructor(
    private val getDogListUseCase: GetDogListUseCase,
) : BaseViewModel<DogListState, DogListIntent, DogListEvent>(DogListState(isLoading = true)) {

    private val sortTypeState = MutableStateFlow(BreedSortType.Name)

    override fun initalize() {
        loadList()
    }

    override fun handleIntent(intent: DogListIntent) {
        when (intent) {
            is DogListIntent.ShowDetailAndSaveListPosition -> navigateToDetail(intent.id)
            DogListIntent.StartLongTask -> startTask()
            is DogListIntent.SortTypeChanged -> {
                sortTypeState.value = intent.type
                updateState {
                    copy(selectedType = intent.type)
                }
            }
        }
    }

    private fun loadList() {
        launchOnUi {
            getDogListUseCase.execute().combine(sortTypeState) { result, sortType ->
                log("sorted by $sortType")
                result.map {
                    when (sortType) {
                        BreedSortType.Name -> it.sortedBy { dog -> dog.name }
                        BreedSortType.LifeSpan -> it.sortedBy { dog -> dog.lifeSpan.last }
                        BreedSortType.Weight -> it.sortedBy { dog -> dog.weight.last }
                        BreedSortType.Height -> it.sortedBy { dog -> dog.height.last }
                    }
                }
            }.stateIn(viewModelScope).collect { result ->
                result.fold(this@DogListViewModel::handleError) {
                    log("set list ${it.size}")
                    updateState {
                        copy(isLoading = false, list = it.toImmutable())
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
                log("collect $it")
            }
        }
    }

    private fun navigateToDetail(id: Int) {
        emitEvent(DogListEvent.NavigateToDetail(id))
    }

    private fun handleError(error: DataSourceError) {
        log(error.toString())
    }
}

data class DogListState(
    val list: ImmutableList<Dog> = emptyImmutableList(),
    val isLoading: Boolean = false,
    val selectedType: BreedSortType = BreedSortType.LifeSpan,
)

sealed interface DogListIntent {
    data class ShowDetailAndSaveListPosition(val id: Int) : DogListIntent
    object StartLongTask : DogListIntent

    data class SortTypeChanged(val type: BreedSortType) : DogListIntent
}

sealed interface DogListEvent {
    data class NavigateToDetail(val breedId: Int) : DogListEvent
}