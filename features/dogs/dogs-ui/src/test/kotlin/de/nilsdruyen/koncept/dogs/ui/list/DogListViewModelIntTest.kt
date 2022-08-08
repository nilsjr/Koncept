package de.nilsdruyen.koncept.dogs.ui.list

import app.cash.turbine.test
import arrow.core.Either
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.components.SingletonComponent
import de.nilsdruyen.koncept.dogs.cache.daos.DogDao
import de.nilsdruyen.koncept.dogs.cache.entities.DogCacheEntity
import de.nilsdruyen.koncept.dogs.domain.usecase.GetDogListUseCase
import de.nilsdruyen.koncept.dogs.remote.DogsApi
import de.nilsdruyen.koncept.dogs.remote.entities.DogWebEntity
import de.nilsdruyen.koncept.domain.annotations.DefaultDispatcher
import de.nilsdruyen.koncept.domain.annotations.IoDispatcher
import de.nilsdruyen.koncept.domain.annotations.MainDispatcher
import de.nilsdruyen.koncept.test.utils.parseList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject
import javax.inject.Singleton

//@HiltAndroidTest
//@RunWith(RobolectricTestRunner::class)
//@Config(sdk = [28], application = HiltTestApplication::class)
//class DogListViewModelIntTest {
//
//    @get:Rule(order = 1)
//    var hiltRule = HiltAndroidRule(this)
//
//    @get:Rule(order = 2)
//    val mockRule = MockitoJUnit.rule()
//
//    @Inject
//    lateinit var dispatcher: TestDispatcher
//
//    @Mock
//    lateinit var getDogListUseCase: GetDogListUseCase
//
//    @Mock
//    lateinit var dogDao: DogDao
//
//    @Mock
//    lateinit var dogsApi: DogsApi
//
//    private lateinit var viewModel: DogListViewModel
//
//    private val scope = lazy {
//        TestScope(dispatcher)
//    }
//
//    @Before
//    fun init() {
//        hiltRule.inject()
//        Dispatchers.setMain(dispatcher)
//        viewModel = DogListViewModel(getDogListUseCase)
//    }
//
//    @After
//    fun cleanup() {
//        Dispatchers.resetMain()
//    }
//
//    @Test
//    fun `load dogs`() = scope.value.runTest {
//        val dogEntityList = "/json/dog-list.json".parseList(DogWebEntity::class.java).take(25)
//        val dogWebEntityList = Either.Right(dogEntityList)
//        val dogCacheEntityList = listOf(DogCacheEntity(1, "Dog 1", false))
//
//        whenever(dogsApi.getBreeds()) doReturn dogWebEntityList
//        whenever(dogDao.getAll()) doReturn flowOf(dogCacheEntityList)
//        whenever(dogDao.addList(any())) doReturn listOf()
//
//        viewModel.state.test {
//            assert(awaitItem().list.isEmpty())
//            assert(awaitItem().list.size == 1) // cache
//            assert(awaitItem().list.size == 25) // web
//        }
//    }
//}
//
//@Module
//@InstallIn(SingletonComponent::class)
//object DispatchersModule {
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
//object IntegrationTestModule {
//
//    @Provides
//    @Singleton
//    fun provideDogDao(): DogDao = mock()
//
//    @Provides
//    @Singleton
//    fun provideDogApi(): DogsApi = mock()
//}