package de.nilsdruyen.koncept.common.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<State, Intent, Event>(initalState: State) : ViewModel() {

    private val mutableState = MutableStateFlow(initalState)
    val state: StateFlow<State> by lazy {
        initalize()
        mutableState.asStateFlow()
    }
    val intent = Channel<Intent>(Channel.BUFFERED)

    private val eventChannel: Channel<Event> = Channel()
    val events = eventChannel.receiveAsFlow()

    private val jobs = mutableMapOf<JobKey, Job>()

    enum class JobKey {
        LONG_TASK
    }

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                handleIntent(it)
            }
        }
    }

    abstract fun initalize()

    abstract fun handleIntent(intent: Intent)

    protected fun launchOnUi(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch {
            block()
        }
    }

    protected fun launchDistinct(key: JobKey, block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch {
            jobs[key]?.cancelAndJoin()
            jobs[key] = this.launch {
                block()
            }
        }
    }

    protected fun updateState(setState: State.() -> State) {
        mutableState.value = setState(state.value)
    }

    protected fun emitEvent(event: Event) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }
}