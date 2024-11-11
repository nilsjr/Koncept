package de.nilsdruyen.koncept.dogs.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.nilsdruyen.koncept.common.ui.ImmutableList
import de.nilsdruyen.koncept.common.ui.emptyImmutableList
import de.nilsdruyen.koncept.common.ui.toImmutable
import de.nilsdruyen.koncept.dogs.domain.usecase.GetDogListUseCase
import de.nilsdruyen.koncept.dogs.entity.BreedId
import de.nilsdruyen.koncept.dogs.entity.BreedSortType
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.domain.Logger.Companion.log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DogListViewModel @Inject constructor(
    getDogListUseCase: GetDogListUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(DogListState())
    private val sortTypeState = MutableStateFlow(BreedSortType.Name)

    val state = combine(
        _state,
        getDogListUseCase.execute(),
        sortTypeState,
    ) { state, dogList, sortType ->
        val sortedList = dogList.map {
            when (sortType) {
                BreedSortType.Name -> it.sortedBy { dog -> dog.name }
                BreedSortType.LifeSpan -> it.sortedBy { dog -> dog.lifeSpan.last }
                BreedSortType.Weight -> it.sortedBy { dog -> dog.weight.last }
                BreedSortType.Height -> it.sortedBy { dog -> dog.height.last }
            }
        }
        state.copy(list = sortedList.getOrNull().orEmpty().toImmutable())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DogListState(isLoading = true),
    )

    fun sendIntent(intent: DogListIntent) {
        when (intent) {
            is DogListIntent.ShowDetailAndSaveListPosition -> navigateToDetail(intent.id)
            is DogListIntent.SortTypeChanged -> {
                sortTypeState.value = intent.type
                _state.update { it.copy(selectedType = intent.type) }
            }

            DogListIntent.Reload -> {
                viewModelScope.launch {
                    // implement reload data
                    _state.update { it.copy(isLoading = true) }
                    delay(2000L)
                    _state.update { it.copy(isLoading = false) }
                }
            }

            DogListIntent.NavigationConsumed -> _state.update { it.copy(navigateTo = null) }

            DogListIntent.BackFromSearch -> {
                _state.update { it.copy(activeSearch = false, input = "") }
            }

            DogListIntent.Search -> {
                val result = _state.value.list.items.filter {
                    it.name.contains(_state.value.input)
                }
                _state.update { it.copy(activeSearch = true, searchResult = result) }
            }

            is DogListIntent.InputChange -> {
                val result = _state.value.list.items.filter {
                    it.name.contains(_state.value.input)
                }
                _state.update { it.copy(input = intent.input, searchResult = result) }
            }
        }
    }

    private fun navigateToDetail(id: BreedId) {
        _state.update {
            it.copy(navigateTo = id)
        }
    }

    private fun handleError(error: DataSourceError) {
        log(error.toString())
    }
}

data class DogListState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val list: ImmutableList<Dog> = emptyImmutableList(),
//    val list: ImmutableList<DogGroup> = emptyImmutableList(),
    val selectedType: BreedSortType = BreedSortType.LifeSpan,
    val navigateTo: BreedId? = null,
    val input: String = "",
    val activeSearch: Boolean = false,
    val searchResult: List<Dog>? = null,
)

sealed interface DogListIntent {
    data class ShowDetailAndSaveListPosition(val id: BreedId) : DogListIntent
    data class SortTypeChanged(val type: BreedSortType) : DogListIntent
    data object Reload : DogListIntent
    data object NavigationConsumed : DogListIntent
    data object Search : DogListIntent
    data class InputChange(val input: String) : DogListIntent
    data object BackFromSearch : DogListIntent
}
