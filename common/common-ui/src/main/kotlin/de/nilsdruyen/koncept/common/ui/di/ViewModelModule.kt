package de.nilsdruyen.koncept.common.ui.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import de.nilsdruyen.koncept.common.ui.providers.PropertyProvider
import de.nilsdruyen.koncept.common.ui.providers.PropertyProviderImpl

@Module
@InstallIn(ViewModelComponent::class)
interface ViewModelModule {

    @Binds
    @ViewModelScoped
    fun bindPropertyProvider(propertyProviderImpl: PropertyProviderImpl): PropertyProvider
}
