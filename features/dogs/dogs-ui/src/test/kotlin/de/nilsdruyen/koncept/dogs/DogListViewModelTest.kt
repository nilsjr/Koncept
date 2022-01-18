package de.nilsdruyen.koncept.dogs

import app.cash.turbine.test
import arrow.core.Either
import de.nilsdruyen.koncept.dogs.list.DogListIntent
import de.nilsdruyen.koncept.dogs.list.DogListViewModel
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.test.CoroutinesTestExtension
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class, CoroutinesTestExtension::class)
internal class DogListViewModelTest {

    @MockK
    lateinit var getDogListUseCase: GetDogListUseCase

    private lateinit var tested: DogListViewModel

    @BeforeEach
    fun setup() {
        tested = DogListViewModel(getDogListUseCase)
    }

    @Disabled
    @Test
    fun `Viewmodel should load dog list when intent is fired`() = runTest {
        val responseFlow = flowOf<Either<DataSourceError, List<Dog>>>(
            Either.Right(List(2) { DogFactory.build() }),
        )

        coEvery { getDogListUseCase() } returns responseFlow

        tested.state.test {
            tested.intent.send(DogListIntent.LoadIntent)
            val first = awaitItem()
            assert(first.isLoading)
            val second = awaitItem()
            assert(!second.isLoading)
            assert(second.list.isNotEmpty())
            awaitComplete()
        }
    }
}