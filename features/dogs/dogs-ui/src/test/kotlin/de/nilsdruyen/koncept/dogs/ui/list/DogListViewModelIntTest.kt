package de.nilsdruyen.koncept.dogs.ui.list

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.right
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import de.nilsdruyen.koncept.dogs.cache.daos.DogDao
import de.nilsdruyen.koncept.dogs.cache.entities.DogCacheEntity
import de.nilsdruyen.koncept.dogs.cache.entities.MinimalDogCacheEntity
import de.nilsdruyen.koncept.dogs.domain.usecase.GetDogListUseCase
import de.nilsdruyen.koncept.dogs.remote.DogsApi
import de.nilsdruyen.koncept.dogs.remote.DogsRemoteStaticModule
import de.nilsdruyen.koncept.dogs.remote.entities.BreedImageWebEntity
import de.nilsdruyen.koncept.dogs.remote.entities.DogWebEntity
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.domain.DispatchersModule
import de.nilsdruyen.koncept.domain.annotations.DefaultDispatcher
import de.nilsdruyen.koncept.domain.annotations.IoDispatcher
import de.nilsdruyen.koncept.domain.annotations.MainDispatcher
import de.nilsdruyen.koncept.test.utils.parseList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnit
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject
import javax.inject.Singleton

//@UninstallModules(DogsRemoteStaticModule::class)
//@HiltAndroidTest
//@RunWith(RobolectricTestRunner::class)
//@Config(sdk = [28], application = HiltTestApplication::class)
//class DogListViewModelIntTest {
//
//    @get:Rule(order = 1)
//    var hiltRule = HiltAndroidRule(this)
//
////    @get:Rule(order = 2)
////    val mockRule = MockitoJUnit.rule()
//
//    @Inject
//    lateinit var dispatcher: TestDispatcher
//
//    @Inject
//    lateinit var getDogListUseCase: GetDogListUseCase
//
//    private lateinit var viewModel: DogListViewModel
//
//    private val scope = lazy { TestScope(dispatcher) }
//
//    @Before
//    fun init() {
//        hiltRule.inject()
//        Dispatchers.setMain(dispatcher)
//        viewModel = DogListViewModel(dispatcher, getDogListUseCase)
//    }
//
//    @After
//    fun cleanup() {
//        Dispatchers.resetMain()
//    }
//
//    @Test
//    fun `load dogs`() = scope.value.runTest {
////        val dogEntityList = "/json/dog-list.json".parseList(DogWebEntity::class.java).take(25)
////        val dogWebEntityList = Either.Right(dogEntityList)
////        val dogCacheEntityList = listOf(DogCacheEntity(1, "Dog 1", false))
//
////        whenever(dogsApi.getBreeds()) doReturn dogWebEntityList
////        whenever(dogDao.getAll()) doReturn flowOf(dogCacheEntityList)
////        whenever(dogDao.addList(any())) doReturn emptyList()
//
//        viewModel.state.test {
//            assert(awaitItem().list.isEmpty())
//            assert(awaitItem().list.size == 1) // cache
//            assert(awaitItem().list.size == 25) // web
//            cancelAndIgnoreRemainingEvents()
//        }
//    }
//}
//
//@Module
//@TestInstallIn(
//    components = [SingletonComponent::class],
//    replaces = [DispatchersModule::class]
//)
//object TestDispatchersModule {
//
//    @Provides
//    @Singleton
//    fun provideTestDispatcher(): TestDispatcher = StandardTestDispatcher(TestCoroutineScheduler())
//
//    @Provides
//    @IoDispatcher
//    fun providesIoDispatcher(dispatcher: TestDispatcher): CoroutineDispatcher = dispatcher
//
//    @Provides
//    @MainDispatcher
//    fun providesMainDispatcher(dispatcher: TestDispatcher): CoroutineDispatcher = dispatcher
//
//    @Provides
//    @DefaultDispatcher
//    fun providesDefaultDispatcher(dispatcher: TestDispatcher): CoroutineDispatcher = dispatcher
//}
//
//@Module
//@InstallIn(SingletonComponent::class)
//object TestCacheModule {
//
//    @Provides
//    fun provideoDao(): DogDao = object : DogDao {
//
//        override fun getAll(): Flow<List<DogCacheEntity>> {
//            val dogCacheEntityList = listOf(DogCacheEntity(1, "Dog 1", false))
//            return flowOf(dogCacheEntityList)
//        }
//
//        override fun getDogById(id: Int): Flow<DogCacheEntity> {
//            TODO("Not yet implemented")
//        }
//
//        override suspend fun addList(list: List<DogCacheEntity>): List<Long> {
//            TODO("Not yet implemented")
//        }
//
//        override suspend fun addMinimalList(list: List<MinimalDogCacheEntity>): List<Long> {
//            return emptyList()
//        }
//
//        override suspend fun updateList(list: List<MinimalDogCacheEntity>) {
//
//        }
//
//        override fun getAllFavorites(): Flow<List<DogCacheEntity>> {
//            TODO("Not yet implemented")
//        }
//
//        override suspend fun setFavorite(breedId: Int) {
//
//        }
//
//        override suspend fun removeFavorite(breedId: Int) {
//
//        }
//
//    }
//}
//
//@Module
//@TestInstallIn(
//    components = [SingletonComponent::class],
//    replaces = [DogsRemoteStaticModule::class]
//)
//object TestModule {
//
//    @Provides
//    fun provideApi(): DogsApi = object : DogsApi {
//
//        override suspend fun getBreeds(): Either<DataSourceError, List<DogWebEntity>> {
//            val dogEntityList = "/json/dog-list.json".parseList(DogWebEntity::class.java).take(25)
//            return dogEntityList.right()
//        }
//
//        override suspend fun searchBreed(input: String): Either<DataSourceError, List<DogWebEntity>> {
//            TODO("Not yet implemented")
//        }
//
//        override suspend fun searchImages(
//            limit: Int,
//            page: Int,
//            order: String
//        ): Either<DataSourceError, List<BreedImageWebEntity>> {
//            TODO("Not yet implemented")
//        }
//
//        override suspend fun searchImagesForBreed(
//            limit: Int,
//            breedId: Int,
//            size: String
//        ): Either<DataSourceError, List<BreedImageWebEntity>> {
//            TODO("Not yet implemented")
//        }
//
//        override suspend fun getImage(id: String): Either<DataSourceError, BreedImageWebEntity> {
//            TODO("Not yet implemented")
//        }
//
//    }
//}