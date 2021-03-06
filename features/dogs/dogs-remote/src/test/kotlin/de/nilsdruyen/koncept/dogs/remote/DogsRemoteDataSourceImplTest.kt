package de.nilsdruyen.koncept.dogs.remote

import arrow.core.Either
import arrow.core.computations.ResultEffect.bind
import de.nilsdruyen.koncept.dogs.remote.entities.DogWebEntity
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.test.CoroutineTest
import de.nilsdruyen.koncept.test.utils.parseList
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
internal class DogsRemoteDataSourceImplTest : CoroutineTest {

    override lateinit var testScope: TestScope
    override lateinit var dispatcher: TestDispatcher

    @Mock
    lateinit var api: DogsApi

    lateinit var tested: DogsRemoteDataSourceImpl

    @BeforeEach
    fun setup() {
        tested = DogsRemoteDataSourceImpl(api, dispatcher)
    }

    @Test
    fun `Get dog list from remote`() = runTest {
        val dogEntityList = "/dog-list.json".parseList(DogWebEntity::class.java)
        val response: Either<DataSourceError, List<DogWebEntity>> = Either.Right(dogEntityList)

        whenever(api.getBreeds()).thenReturn(response)

        val result = tested.getList().bind()

        assert(result.size == dogEntityList.size)
        dogEntityList.forEachIndexed { index, dog ->
            assert(dog.id == result[index].id)
        }
    }
}