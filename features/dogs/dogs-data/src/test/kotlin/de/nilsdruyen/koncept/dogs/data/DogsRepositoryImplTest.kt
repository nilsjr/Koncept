package de.nilsdruyen.koncept.dogs.data

import app.cash.turbine.test
import arrow.core.Either
import de.nilsdruyen.koncept.dogs.domain.repository.DogsRepository
import de.nilsdruyen.koncept.dogs.test.DogFactory
import de.nilsdruyen.koncept.test.CoroutineTest
import de.nilsdruyen.koncept.test.CoroutinesTestExtension
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class, CoroutinesTestExtension::class)
internal class DogsRepositoryImplTest : CoroutineTest {

    override lateinit var testScope: TestScope
    override lateinit var dispatcher: TestDispatcher

    @Mock
    lateinit var dogsRemoteDataSource: DogsRemoteDataSource

    @Mock
    lateinit var dogsCacheDataSource: DogsCacheDataSource

    lateinit var tested: DogsRepository

    @BeforeEach
    fun setup() {
        tested = DogsRepositoryImpl(dogsRemoteDataSource, dogsCacheDataSource, dispatcher)
    }

    @Nested
    inner class GetList {

        @Test
        fun `Given cache is empty & remote not Then it should return an empty list & a filled list`() = runTest {
            // given
            val remote = Either.Right(List(2) { DogFactory.build() })
            whenever(dogsCacheDataSource.getDogList()).thenReturn(flowOf(Either.Right(emptyList()), remote))
            whenever(dogsRemoteDataSource.getList()).thenReturn(remote)

            // when
            tested.getList().test {
                // then
                with(awaitItem()) {
                    println("item $this")

                    assert(this.isRight())
                    fold({}) {
                        assert(it.isEmpty())
                    }
                }
                with(awaitItem()) {
                    println("item $this")

                    assert(this.isRight())
                    fold({}) {
                        assert(it.isNotEmpty())
                        assert(it.size == 2)
                    }
                }
                awaitComplete()
            }
        }

        @Test
        fun `Given cache & remote returns same values Then it should emit only once`() = runTest {
            val dogList = DogFactory.buildList(4)

            whenever(dogsCacheDataSource.getDogList()).thenReturn(flowOf(Either.Right(dogList)))
            whenever(dogsRemoteDataSource.getList()).thenReturn(Either.Right(dogList))

            tested.getList().test {
                with(awaitItem()) {
                    assert(this.isRight())
                    fold({}) {
                        assert(it.size == 4)
                        assert(it.first().name == dogList.first().name)
                    }
                }
                awaitComplete()
            }
        }
    }
}