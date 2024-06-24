package de.nilsdruyen.koncept.dogs.ui.list

import de.nilsdruyen.koncept.dogs.cache.daos.DogDao
import de.nilsdruyen.koncept.dogs.cache.entities.DogCacheEntity
import de.nilsdruyen.koncept.dogs.cache.entities.MinimalDogCacheEntity
import de.nilsdruyen.koncept.test.CoroutinesTestExtension
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class, CoroutinesTestExtension::class)
class DogIntTest {

//    @Mock
//    @IoDispatcher
//    lateinit var ioDispatcher: CoroutineDispatcher
//
//    @Mock
//    private val dogDao: DogDao
//
//    @InjectMocks
//    lateinit var viewModel: DogListViewModel
//
//    @Test
//    fun `GIVEN no cache WHEN init THEN list is shown`() = runTest {
//
//    }
}

class FakeDao : DogDao {
    override fun getAll(): Flow<List<DogCacheEntity>> {
        val dogCacheEntityList = listOf(
            DogCacheEntity(
                id = 1,
                name = "Dog 1",
                isFavorite = false,
                lifeSpan = 1..2,
                weight = 1..2,
                height = 1..2,
                temperament = listOf(),
                origin = listOf(),
                bredFor = "",
                group = ""
            )
        )
        return flowOf(dogCacheEntityList)
    }

    override fun getDogById(id: Int): Flow<DogCacheEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun addList(list: List<DogCacheEntity>): List<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun addMinimalList(list: List<MinimalDogCacheEntity>): List<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun updateList(list: List<DogCacheEntity>) {
        TODO("Not yet implemented")
    }

    override suspend fun updateMinimalList(list: List<MinimalDogCacheEntity>) {
        TODO("Not yet implemented")
    }

    override fun getAllFavorites(): Flow<List<DogCacheEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun setFavorite(breedId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun removeFavorite(breedId: Int) {
        TODO("Not yet implemented")
    }
}
