package de.nilsdruyen.koncept.dogs.remote

import arrow.core.Either
import arrow.core.computations.ResultEffect.bind
import de.nilsdruyen.koncept.dogs.remote.entities.DogWebEntity
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.test.CoroutinesTestExtension
import de.nilsdruyen.koncept.test.parseList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class, CoroutinesTestExtension::class)
internal class DogsRemoteDataSourceImplTest {

    @Mock
    lateinit var api: DogsApi

    @InjectMocks
    lateinit var tested: DogsRemoteDataSourceImpl

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