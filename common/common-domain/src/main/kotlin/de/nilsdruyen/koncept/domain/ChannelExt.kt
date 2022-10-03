package de.nilsdruyen.koncept.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

fun <T> Channel<T>.sendIn(scope: CoroutineScope, t: T) = scope.launch {
    send(t)
}