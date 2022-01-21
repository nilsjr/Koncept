package de.nilsdruyen.koncept.dogs.domain.usecase

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.computations.ResultEffect.bind
import de.nilsdruyen.koncept.dogs.domain.repository.DogsRepository
import de.nilsdruyen.koncept.test.CoroutinesTestExtension
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class, CoroutinesTestExtension::class)
internal class GetDogListUseCaseImplTest {

    @Mock
    lateinit var dogsRepository: DogsRepository

    @InjectMocks
    lateinit var getDogListUseCaseImpl: GetDogListUseCaseImpl

    @Test
    fun `Get list from repository`() = runTest {
        whenever(dogsRepository.getList()) doReturn flowOf(Either.Right(emptyList()))

        getDogListUseCaseImpl.execute().test {
            assert(awaitItem().bind().isEmpty())
            awaitComplete()
        }
    }
}