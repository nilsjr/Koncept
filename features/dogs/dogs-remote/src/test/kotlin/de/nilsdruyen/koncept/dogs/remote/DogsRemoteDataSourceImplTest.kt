package de.nilsdruyen.koncept.dogs.remote

import arrow.core.Either
import de.nilsdruyen.koncept.dogs.remote.entities.DogWebEntity
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.domain.DispatcherProvider
import de.nilsdruyen.koncept.test.CoroutinesTestExtension
import de.nilsdruyen.koncept.test.InjectTestDispatcherProvider
import de.nilsdruyen.koncept.test.utils.parseList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class, CoroutinesTestExtension::class)
internal class DogsRemoteDataSourceImplTest {

    @InjectTestDispatcherProvider
    lateinit var dispatcherProvider: DispatcherProvider

    @Mock
    lateinit var api: DogsApi

    lateinit var tested: DogsRemoteDataSourceImpl

    @BeforeEach
    fun setup() {
        tested = DogsRemoteDataSourceImpl(api, dispatcherProvider.io)
    }

    @Test
    fun `Get dog list from remote`() = runTest {
        val dogEntityList = "/dog-list.json".parseList(DogWebEntity::class.java)
        val response: Either<DataSourceError, List<DogWebEntity>> = Either.Right(dogEntityList)

        whenever(api.getBreeds()) doReturn response

        tested.getList()
            .onRight {
                assert(it.size == dogEntityList.size)
                dogEntityList.forEachIndexed { index, dog ->
                    assert(dog.id == it[index].id.value)
                }
            }
            .onLeft {
                error("error $it not expected")
            }
    }
}
