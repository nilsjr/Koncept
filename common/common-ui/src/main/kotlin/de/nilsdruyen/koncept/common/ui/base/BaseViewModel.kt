package de.nilsdruyen.koncept.common.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.nilsdruyen.koncept.domain.sendIn
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch

abstract class BaseViewModel<State, Intent>(initialState: State) : ViewModel() {

    private var stateInitialized = false

    private val mutableState = MutableStateFlow(initialState)
    val state: StateFlow<State> by lazy {
        check(!stateInitialized) {
            "Accessing state in initialize method is not allowed. Make sure to use currentState."
        }
        stateInitialized = true
        mutableState.asStateFlow()
    }
    protected val currentState
        get() = mutableState.value

    val intent = Channel<Intent>(Channel.BUFFERED)

    init {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                launch { onIntent(it) }
            }
        }
    }

    fun sendIntent(i: Intent) {
        intent.sendIn(viewModelScope, i)
    }

    protected fun updateState(producer: State.() -> State) {
        mutableState.getAndUpdate(producer)
    }

    abstract suspend fun onIntent(intent: Intent)
}
