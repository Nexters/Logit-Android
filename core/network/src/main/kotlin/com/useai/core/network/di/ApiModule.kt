package com.useai.core.network.di

import com.useai.core.common.qualifiers.AuthClient
import com.useai.core.network.api.ChattingApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityScoped
import retrofit2.Retrofit
import retrofit2.create

@Module
@InstallIn(ActivityRetainedComponent::class)
internal object ApiModule {

    @Provides
    @ActivityScoped
    fun providesChattingApi(
        @AuthClient retrofit: Retrofit
    ) : ChattingApi {
        return retrofit.create<ChattingApi>()
    }
}
