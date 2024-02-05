package de.nilsdruyen.koncept.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import arrow.core.Either
import arrow.core.right
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import de.nilsdruyen.koncept.dogs.data.DogsDataModule
import de.nilsdruyen.koncept.dogs.domain.BreedImages
import de.nilsdruyen.koncept.dogs.domain.DogListFlow
import de.nilsdruyen.koncept.dogs.domain.repository.DogsRepository
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.dogs.test.DogFactory
import de.nilsdruyen.koncept.domain.DataSourceError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UninstallModules(DogsDataModule::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainActivity2Test {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun checkBreeds() {
        hiltRule.inject()
        with(composeRule) {
            onNodeWithTag("appbar").assertIsDisplayed()

            waitUntil {
                onAllNodesWithTag("loading").fetchSemanticsNodes().isEmpty()
            }

            onAllNodesWithTag("dogItem").fetchSemanticsNodes().size == 2
        }
    }

    @Module
    @InstallIn(SingletonComponent::class)
    object FakeDataModule {

        @Provides
        fun bindDogsRepository(): DogsRepository = object : DogsRepository {
            override fun getList(): DogListFlow {
                val dogList = DogFactory.buildList(2)
                return flow {
                    emit(dogList.right())
                }
            }

            override suspend fun getImagesForBreed(breedId: Int): BreedImages {
                TODO("Not yet implemented")
            }

            override fun getFavoriteList(): Flow<Either<DataSourceError, List<Dog>>> {
                TODO("Not yet implemented")
            }

            override suspend fun setFavorite(breedId: Int) {
                TODO("Not yet implemented")
            }

            override suspend fun removeFavorite(breedId: Int) {
                TODO("Not yet implemented")
            }

            override fun isFavoriteFlow(breedId: Int): Flow<Boolean> {
                TODO("Not yet implemented")
            }
        }
    }
}
