package com.useai.core.data.di

import com.useai.core.data.repository.ChattingRepository
import com.useai.core.data.repository.ChattingRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
internal interface RepositoryModule {

    @Binds
    @ActivityScoped
    fun providesChattingRepository(
        impl: ChattingRepositoryImpl
    ) : ChattingRepository
}
