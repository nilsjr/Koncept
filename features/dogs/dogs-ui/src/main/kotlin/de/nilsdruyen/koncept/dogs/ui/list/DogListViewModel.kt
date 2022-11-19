package de.nilsdruyen.koncept.dogs.ui.list

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.nilsdruyen.koncept.common.ui.ImmutableList
import de.nilsdruyen.koncept.common.ui.base.MviViewModel
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DogListViewModel @Inject constructor(
    private val getDogListUseCase: GetDogListUseCase,
) : MviViewModel<DogListState, DogListIntent>(DogListState(isLoading = true)) {

    private val sortTypeState = MutableStateFlow(BreedSortType.Name)

    override fun initialize() {
        loadList()
    }

    override suspend fun onIntent(intent: DogListIntent) {
        when (intent) {
            is DogListIntent.ShowDetailAndSaveListPosition -> navigateToDetail(intent.id)
            is DogListIntent.SortTypeChanged -> {
                sortTypeState.value = intent.type
                updateState {
                    copy(selectedType = intent.type)
                }
            }

            DogListIntent.Reload -> {
                viewModelScope.launch {
                    // implement reload data
                    updateState { copy(isLoading = true) }
                    delay(2000L)
                    updateState { copy(isLoading = false) }
                }
            }

            DogListIntent.NavigationConsumed -> updateState {
                copy(navigateTo = null)
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
            }.map {
                it
//                it.map { list ->
//                    list.groupBy { dog -> dog.name.first() }
//                }.map { dogMap ->
//                    dogMap.map { entry -> DogGroup(entry.key.toString(), entry.value) }
//                }
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

//    private fun startTask() {
//        launchDistinct(JobKey.LONG_TASK) {
//            flow {
//                repeat(40) {
//                    delay(500)
//                    emit(it)
//                }
//            }.collect {
//                log("collect $it")
//            }
//        }
//    }

    private fun navigateToDetail(id: Int) {
        updateState {
            copy(navigateTo = id)
        }
    }

    private fun handleError(error: DataSourceError) {
        log(error.toString())
    }
}

data class DogListState(
    val list: ImmutableList<Dog> = emptyImmutableList(),
//    val list: ImmutableList<DogGroup> = emptyImmutableList(),
    val isLoading: Boolean = false,
    val selectedType: BreedSortType = BreedSortType.LifeSpan,
    val navigateTo: Int? = null,
)

sealed interface DogListIntent {
    data class ShowDetailAndSaveListPosition(val id: Int) : DogListIntent

    data class SortTypeChanged(val type: BreedSortType) : DogListIntent

    object Reload : DogListIntent

    object NavigationConsumed : DogListIntent
}