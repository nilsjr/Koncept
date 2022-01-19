package de.nilsdruyen.koncept.dogs.ui

import app.cash.turbine.test
import arrow.core.Either
import de.nilsdruyen.koncept.dogs.test.DogFactory
import de.nilsdruyen.koncept.dogs.ui.list.DogListIntent
import de.nilsdruyen.koncept.dogs.ui.list.DogListViewModel
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.dogs.domain.usecase.GetDogListUseCase
import de.nilsdruyen.koncept.dogs.entities.Dog
import de.nilsdruyen.koncept.test.CoroutinesTestExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@ExtendWith(MockitoExtension::class, CoroutinesTestExtension::class)
internal class DogListViewModelTest {

    @Mock
    lateinit var getDogListUseCase: GetDogListUseCase

    private lateinit var tested: DogListViewModel

    @BeforeEach
    fun setup() {
        tested = DogListViewModel(getDogListUseCase)
    }

    @Test
    fun `Viewmodel should load dog list when intent is fired`() = runTest {
        val responses = flowOf<Either<DataSourceError, List<Dog>>>(
            Either.Right(emptyList()),
            Either.Right(List(2) { DogFactory.build() }),
        )

        whenever(getDogListUseCase.execute()).thenReturn(responses)

        tested.state.test {
            tested.intent.send(DogListIntent.LoadIntent)
            assert(awaitItem().list.isEmpty())
            tested.intent.send(DogListIntent.LoadIntent)
            assertEquals(2, awaitItem().list.size)
        }
    }
}