package de.nilsdruyen.koncept.dogs.ui.list

import arrow.core.Either
import dagger.Module
import dagger.Provides
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class DogListViewModelTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var getDogListUseCase: GetDogListUseCase

    lateinit var viewModel: DogListViewModel

    @Before
    fun init() {
        hiltRule.inject()

        Dispatchers.setMain(StandardTestDispatcher())

        viewModel = DogListViewModel(getDogListUseCase)
    }

    @After
    fun reset() {
        Dispatchers.resetMain()
    }

    @Ignore
    @Test
    fun loaDogList() = runTest {
        viewModel.intent.send(DogListIntent.LoadIntent)

        val states = mutableListOf<DogListState>()
//        val job = launch {
//            viewModel.state.onEach {
//                states.add(it)
//            }.collect()
//        }

        viewModel.state.toList(states)
//        val currentState = viewModel.state.value

        println("list ${states.size}")

        assert(states[0].isLoading)
        assert(states.size > 1)
//        assert(currentState.list.isNotEmpty())
//        assert(currentState.list.size == 1)

//        job.cancel()
    }
}

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DogsRemoteModule::class, DogsCacheModule::class]
)
object FakeModule {

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

            override suspend fun setDogList(list: List<Dog>) {

            }
        }
    }
}

