package de.nilsdruyen.koncept.dogs.ui.di

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import de.nilsdruyen.koncept.base.navigation.EntryProviderInstaller
import de.nilsdruyen.koncept.base.navigation.Navigator
import de.nilsdruyen.koncept.dogs.ui.list.DogListRoute
import de.nilsdruyen.koncept.dogs.ui.list.DogListScreen

@Module
@InstallIn(ActivityRetainedComponent::class)
object DogsModule {

    @IntoSet
    @Provides
    fun provideEntryProviderInstaller(navigator: Navigator): EntryProviderInstaller {
        return {
            entry<DogListRoute> {
                //                val sortTypeState =
                //                    it.savedStateHandle.getStateFlow(BreedListRoute.sortTypeResult, 0).collectAsStateWithLifecycle()
                val sortType = remember { mutableIntStateOf(1) }
                DogListScreen(
                    sortTypeState = sortType,
                    showDetail = { id ->
                        //                        onNavigate(BreedDetailsRoute.createRoute(BreedListRoute, id))
                    },
                    showSortDialog = { type ->
                        //                        onNavigate(BreedListSortDialogRoute.createRoute(BreedListRoute, type))
                    }
                )
            }
            //            entry<ConversationDetail> { key ->
            //                ConversationDetailScreen(key) { navigator.goTo(Profile) }
            //            }
        }
    }
}