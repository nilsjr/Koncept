package de.nilsdruyen.koncept.test

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.javaField

class CoroutinesTestExtension(
    val testScope: TestScope = TestScope(),
    val testDispatcher: TestDispatcher = StandardTestDispatcher(testScope.testScheduler)
) : BeforeAllCallback, AfterAllCallback, BeforeEachCallback {

    override fun beforeAll(context: ExtensionContext?) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun afterAll(context: ExtensionContext?) {
        Dispatchers.resetMain()
        testDispatcher
    }

    override fun beforeEach(context: ExtensionContext?) {
        val testDispatcherProvider = TestDispatcherProvider(testDispatcher)
        val testInstances = context?.requiredTestInstances?.allInstances ?: return
        testInstances.forEach { testInstance ->
            testInstance::class.declaredMemberProperties.filterIsInstance<KMutableProperty<*>>().filter {
                it.javaField?.isAnnotationPresent(InjectTestDispatcherProvider::class.java) == true
            }.forEach {
                it.setter.call(testInstance, testDispatcherProvider)
            }
            testInstance::class.declaredMemberProperties.filterIsInstance<KMutableProperty<*>>().filter {
                it.javaField?.isAnnotationPresent(InjectTestScope::class.java) == true
            }.forEach {
                it.setter.call(testInstance, testScope)
            }
        }
    }
}