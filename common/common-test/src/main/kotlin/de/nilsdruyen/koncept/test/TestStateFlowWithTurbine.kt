package de.nilsdruyen.koncept.test

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest

/**
 * Test a [StateFlow] with turbine.
 * Wrapper to execute collection of [StateFlow] in Unconfined manner.
 *
 *  @param testScope [TestScope] e.g. from your [runTest] block.
 *  @param validate your validation logic using [ReceiveTurbine]
 */
suspend fun <T> StateFlow<T>.testStateFlow(
    testScope: TestScope,
    validate: suspend ReceiveTurbine<T>.() -> Unit
) {
    val results = Channel<T>(Channel.UNLIMITED)
    val job = testScope.launch(UnconfinedTestDispatcher(testScope.testScheduler)) {
        collect { results.send(it) }
    }
    results.consumeAsFlow().test(validate = validate)
    job.cancelAndJoin()
}
