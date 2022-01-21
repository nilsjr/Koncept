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
import dagger.hilt.testing.TestInstallIn
import de.nilsdruyen.koncept.dogs.cache.DogsCacheModule
import de.nilsdruyen.koncept.dogs.data.DogsCacheDataSource
import de.nilsdruyen.koncept.dogs.data.DogsRemoteDataSource
import de.nilsdruyen.koncept.dogs.domain.usecase.GetDogListUseCase
import de.nilsdruyen.koncept.dogs.entity.BreedImage
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.dogs.remote.DogsRemoteModule
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.domain.annotations.DefaultDispatcher
import de.nilsdruyen.koncept.domain.annotations.IoDispatcher
import de.nilsdruyen.koncept.domain.annotations.MainDispatcher
import de.nilsdruyen.koncept.test.TestCoroutineRule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class DogListViewModelIntTest {

    @get:Rule(order = 1)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val testRule = TestCoroutineRule()

    @Inject
    lateinit var getDogListUseCase: GetDogListUseCase

    lateinit var viewModel: DogListViewModel

    @Before
    fun init() {
        hiltRule.inject()
        viewModel = DogListViewModel(testRule.dispatcher, getDogListUseCase)
    }

    @Test
    fun `load dogs`() = testRule.scope.runTest {
        viewModel.state.test {
            viewModel.intent.send(DogListIntent.LoadIntent)

            assert(awaitItem().list.isEmpty())
            assert(awaitItem().list.isEmpty())
            assert(awaitItem().list.size == 1)
        }
    }
}

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DogsRemoteModule::class, DogsCacheModule::class]
)
object FakeIntModule {

    @Provides
    fun bindDogsRemoteDataSource(): DogsRemoteDataSource {
        return object : DogsRemoteDataSource {
            override suspend fun getList(): Either<DataSourceError, List<Dog>> {
                return Either.Right(listOf(Dog(1, "Nils")))
            }

            override suspend fun getImagesForBreed(breedId: Int): Either<DataSourceError, List<BreedImage>> {
                return Either.Right(emptyList())
            }

            override suspend fun getImage(imageId: String): Either<DataSourceError, BreedImage> {
                return Either.Right(BreedImage("", "", 1, ""))
            }
        }
    }

    @Provides
    fun bindDogsCacheDataSource(): DogsCacheDataSource {
        return object : DogsCacheDataSource {
            override suspend fun getDogList(): Flow<Either<DataSourceError, List<Dog>>> {
                return flowOf(Either.Right(emptyList()))
            }

            override suspend fun setDogList(list: List<Dog>) {}
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {

    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @MainDispatcher
    @Provides
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @DefaultDispatcher
    @Provides
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
}