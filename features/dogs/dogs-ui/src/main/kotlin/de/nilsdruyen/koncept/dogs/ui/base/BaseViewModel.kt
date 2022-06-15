package de.nilsdruyen.koncept.dogs.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<S, I, E> : ViewModel() {

    private val initalState: S by lazy {
        initalState()
    }

    private val _state = MutableStateFlow(initalState)
    internal val state: StateFlow<S> = _state

    val intent = Channel<I>()

    private val _effect: Channel<E> = Channel()
    val effect = _effect.receiveAsFlow()

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

    abstract fun initalState(): S

    abstract fun handleIntent(intent: I)

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

    protected fun setState(setState: S.() -> S) {
        _state.value = setState(state.value)
    }

    protected fun sendEffect(effect: E) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}