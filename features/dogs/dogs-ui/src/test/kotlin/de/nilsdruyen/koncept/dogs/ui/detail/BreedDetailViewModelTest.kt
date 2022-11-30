package de.nilsdruyen.koncept.dogs.ui.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import arrow.core.Either
import de.nilsdruyen.koncept.dogs.domain.usecase.GetBreedImageListUseCase
import de.nilsdruyen.koncept.dogs.domain.usecase.IsFavoriteFlowUseCase
import de.nilsdruyen.koncept.dogs.domain.usecase.UpdateFavoriteBreedUseCase
import de.nilsdruyen.koncept.test.CoroutineTest
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
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@ExtendWith(MockitoExtension::class)
internal class BreedDetailViewModelTest : CoroutineTest {

    override lateinit var testScope: TestScope
    override lateinit var dispatcher: TestDispatcher

    @Mock
    lateinit var getBreedImageListUseCase: GetBreedImageListUseCase

    @Mock
    lateinit var updateFavoriteBreedUseCase: UpdateFavoriteBreedUseCase

    @Mock
    lateinit var isFavoriteFlowUseCase: IsFavoriteFlowUseCase

    lateinit var viewModel: BreedDetailViewModel

    @BeforeEach
    fun setup() {
        viewModel = BreedDetailViewModel(
            savedStateHandle = SavedStateHandle(mapOf("breedId" to 1)),
            getBreedImageListUseCase = getBreedImageListUseCase,
            updateFavoriteBreedUseCase = updateFavoriteBreedUseCase,
            isFavoriteFlowUseCase = isFavoriteFlowUseCase
        )
    }

    @Test
    fun `Load empty breed image without error`() = testScope.runTest {
        whenever(getBreedImageListUseCase.execute(any())) doReturn Either.Right(emptyList())
        whenever(isFavoriteFlowUseCase.execute(any())) doReturn flowOf(true)

        viewModel.state.test {
            viewModel.intent.send(BreedDetailIntent.LoadImages)

            assert(awaitItem().isLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }
}