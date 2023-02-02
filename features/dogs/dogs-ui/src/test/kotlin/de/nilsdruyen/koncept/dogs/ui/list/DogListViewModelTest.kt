package de.nilsdruyen.koncept.dogs.ui.list

import app.cash.turbine.test
import arrow.core.Either
import de.nilsdruyen.koncept.common.ui.isEmpty
import de.nilsdruyen.koncept.dogs.domain.usecase.GetDogListUseCase
import de.nilsdruyen.koncept.dogs.test.DogFactory
import de.nilsdruyen.koncept.test.CoroutineTest
import de.nilsdruyen.koncept.test.CoroutinesTestExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@ExtendWith(MockitoExtension::class, CoroutinesTestExtension::class)
internal class DogListViewModelTest : CoroutineTest {

    override lateinit var testScope: TestScope
    override lateinit var dispatcher: TestDispatcher

    @Mock
    lateinit var getDogListUseCase: GetDogListUseCase

    private lateinit var tested: DogListViewModel

    @BeforeEach
    fun setup() {
        tested = DogListViewModel(getDogListUseCase)
    }

    @Test
    fun `Viewmodel should load dog list when intent is fired`() = testScope.runTest {
        val dogList = List(2) { DogFactory.build() }
        val dogList3 = List(3) { DogFactory.build() }
        val responses = flowOf(
            Either.Right(dogList),
            Either.Right(dogList3),
        )

        whenever(getDogListUseCase.execute()) doReturn responses

        tested.state.test {
            assert(awaitItem().list.isEmpty())
            assert(awaitItem().list.size == 2)
            assert(awaitItem().list.size == 3)
            cancelAndIgnoreRemainingEvents()
        }
    }
}