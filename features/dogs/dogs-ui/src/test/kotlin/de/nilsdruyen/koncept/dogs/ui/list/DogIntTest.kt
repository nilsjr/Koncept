package de.nilsdruyen.koncept.dogs.ui.list

import de.nilsdruyen.koncept.dogs.cache.daos.DogDao
import de.nilsdruyen.koncept.dogs.cache.entities.BreedCacheEntity
import de.nilsdruyen.koncept.dogs.cache.entities.MinimalDogCacheEntity
import de.nilsdruyen.koncept.test.CoroutinesTestExtension
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(CoroutinesTestExtension::class, MockitoExtension::class)
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
    override fun getAll(): Flow<List<BreedCacheEntity>> {
        val dogCacheEntityList = listOf(
            BreedCacheEntity(
                id = 1,
                name = "Dog 1",
                isFavorite = false,
                lifeSpan = 1..2,
                weight = 1..2,
                height = 1..2,
                temperament = listOf(),
                origin = listOf(),
                bredFor = "",
                group = "",
                imageUrl = null,
            ),
        )
        return flowOf(dogCacheEntityList)
    }

    override fun getDogById(id: Int): Flow<BreedCacheEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun addList(list: List<BreedCacheEntity>): List<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun addMinimalList(list: List<MinimalDogCacheEntity>): List<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun updateList(list: List<BreedCacheEntity>) {
        TODO("Not yet implemented")
    }

    override suspend fun updateMinimalList(list: List<MinimalDogCacheEntity>) {
        TODO("Not yet implemented")
    }

    override fun getAllFavorites(): Flow<List<BreedCacheEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun setFavorite(breedId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun removeFavorite(breedId: Int) {
        TODO("Not yet implemented")
    }
}
