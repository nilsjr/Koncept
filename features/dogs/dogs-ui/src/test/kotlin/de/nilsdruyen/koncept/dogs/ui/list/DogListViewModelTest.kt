package de.nilsdruyen.koncept.dogs.ui.list

import app.cash.turbine.test
import arrow.core.right
import de.nilsdruyen.koncept.dogs.domain.usecase.GetDogListUseCase
import de.nilsdruyen.koncept.dogs.test.DogFactory
import de.nilsdruyen.koncept.test.CoroutinesTestExtension
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever

@ExtendWith(CoroutinesTestExtension::class, MockitoExtension::class)
internal class DogListViewModelTest {

    @Mock
    lateinit var getDogListUseCase: GetDogListUseCase

    @InjectMocks
    private lateinit var tested: DogListViewModel

    @Disabled("This test is not working")
    @Test
    fun `Viewmodel should load dog list when intent is fired`() = runTest {
        val dogList = List(2) { DogFactory.build() }

        whenever(getDogListUseCase.execute()) doReturn flowOf(dogList.right())

        tested.state.test {
            runCurrent()
            assert(awaitItem().isLoading)
            assert(awaitItem().list.size == 2)
            ensureAllEventsConsumed()
        }
    }
}
