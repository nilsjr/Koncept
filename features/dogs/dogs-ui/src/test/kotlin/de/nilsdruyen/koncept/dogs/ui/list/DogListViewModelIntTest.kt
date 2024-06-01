@file:Suppress("CommentSpacing")

package de.nilsdruyen.koncept.dogs.ui.list

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import de.nilsdruyen.koncept.RemoteModule
import de.nilsdruyen.koncept.dogs.cache.daos.DogDao
import de.nilsdruyen.koncept.dogs.cache.entities.DogCacheEntity
import de.nilsdruyen.koncept.dogs.cache.entities.MinimalDogCacheEntity
import de.nilsdruyen.koncept.dogs.domain.usecase.GetDogListUseCase
import de.nilsdruyen.koncept.dogs.remote.DogsApi
import de.nilsdruyen.koncept.dogs.remote.DogsRemoteStaticModule
import de.nilsdruyen.koncept.dogs.testing.FakeDogsApi
import de.nilsdruyen.koncept.domain.DispatchersModule
import de.nilsdruyen.koncept.domain.annotations.IoDispatcher
import de.nilsdruyen.koncept.test.CoroutineTestRule
import de.nilsdruyen.koncept.test.testStateFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@HiltAndroidTest
@Config(sdk = [29], application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
@UninstallModules(DogsRemoteStaticModule::class, RemoteModule::class, DispatchersModule::class)
class DogListViewModelIntTest {

    @get:Rule(order = 0)
    val testRule = CoroutineTestRule()

    @get:Rule(order = 1)
    var hiltRule = HiltAndroidRule(this)

    @BindValue
    @JvmField
    val dogsApi: DogsApi = FakeDogsApi()

    @BindValue
    @IoDispatcher
    val ioDispatcher: CoroutineDispatcher = testRule.testDispatcherProvider.io

    @Inject
    lateinit var getDogsLitUseCase: GetDogListUseCase

    lateinit var viewModel: DogListViewModel

    @Before
    fun init() {
        hiltRule.inject()
        viewModel = DogListViewModel(getDogsLitUseCase)
    }

    @Ignore("not working right now")
    @Test
    fun `load dogs`() = runTest(testRule.testDispatcher) {
//        val dogEntityList = "/json/dog-list.json".parseList(DogWebEntity::class.java).take(25)
//        val dogWebEntityList = Either.Right(dogEntityList)
//        val dogCacheEntityList = listOf(DogCacheEntity(1, "Dog 1", false))

//        whenever(dogsApi.getBreeds()) doReturn dogWebEntityList
//        whenever(dogDao.getAll()) doReturn flowOf(dogCacheEntityList)
//        whenever(dogDao.addList(any())) doReturn emptyList()

        viewModel.state.testStateFlow(this) {
//            assertThat(awaitItem().list.size).isEqualTo(0)
//            assertThat(awaitItem().list.size).isEqualTo(1)
//            assertThat(awaitItem().list.size).isEqualTo(2)
//            assert(awaitItem().list.size == 1) // cache
//            assert(awaitItem().list.size == 2) // cache2
//            assert(awaitItem().list.size == 25) // web
//            cancelAndIgnoreRemainingEvents()
            val events = cancelAndConsumeRemainingEvents()
            println("${events.size}")
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object TestCacheModule {

    private val cache = listOf(
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

    private val cache2 = listOf(
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
        ),
        DogCacheEntity(
            id = 2,
            name = "Dog 2",
            isFavorite = false,
            lifeSpan = 1..2,
            weight = 1..2,
            height = 1..2,
            temperament = listOf(),
            origin = listOf(),
            bredFor = "",
            group = ""
        ),
    )

    private val dbState = MutableStateFlow(cache)

    @Provides
    fun provideoDao(): DogDao = object : DogDao {

        override fun getAll(): Flow<List<DogCacheEntity>> {
            return dbState
        }

        override fun getDogById(id: Int): Flow<DogCacheEntity> {
            return emptyFlow()
        }

        override suspend fun addList(list: List<DogCacheEntity>): List<Long> {
            return List(list.size) { 1L }
        }

        override suspend fun addMinimalList(list: List<MinimalDogCacheEntity>): List<Long> {
            return emptyList()
        }

        override suspend fun updateList(list: List<DogCacheEntity>) {
            dbState.value = cache2
        }

        override suspend fun updateMinimalList(list: List<MinimalDogCacheEntity>) {
            // do nothing
        }

        override fun getAllFavorites(): Flow<List<DogCacheEntity>> {
            return emptyFlow()
        }

        override suspend fun setFavorite(breedId: Int) {
            // do nothing
        }

        override suspend fun removeFavorite(breedId: Int) {
            // do nothing
        }
    }
}
