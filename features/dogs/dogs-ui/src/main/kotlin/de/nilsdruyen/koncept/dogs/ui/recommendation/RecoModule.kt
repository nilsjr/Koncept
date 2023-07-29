package de.nilsdruyen.koncept.dogs.ui.recommendation

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
internal interface RecoModule {

    @ViewModelScoped
    @Binds
    fun RecoViewModel.bind(): RecoDelegate
}