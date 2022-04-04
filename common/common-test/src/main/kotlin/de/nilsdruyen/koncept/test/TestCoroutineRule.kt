package de.nilsdruyen.koncept.test

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class TestCoroutineRule(val dispatcher: TestDispatcher = StandardTestDispatcher(TestCoroutineScheduler())) : TestRule {

    val scope = TestScope(dispatcher)

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                Dispatchers.setMain(dispatcher)
                // everything above this happens before the test
                base.evaluate()
                // everything below this happens after the test
                Dispatchers.resetMain()
            }
        }
    }
}