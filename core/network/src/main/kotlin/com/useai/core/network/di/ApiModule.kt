package com.useai.core.network.di

import com.useai.core.common.qualifiers.AuthClient
import com.useai.core.network.api.ChattingApi
import com.useai.core.network.api.ExperienceApi
import com.useai.core.network.api.ProjectsApi
import com.useai.core.network.api.QuestionApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit
import retrofit2.create

@Module
@InstallIn(ActivityRetainedComponent::class)
internal object ApiModule {
    @Provides
    @ActivityRetainedScoped
    fun providesNewProjectApi(
        @AuthClient retrofit: Retrofit
    ) = retrofit.create<ProjectsApi>()

    @Provides
    @ActivityRetainedScoped
    fun providesChattingApi(
        @AuthClient retrofit: Retrofit
    ) : ChattingApi {
        return retrofit.create<ChattingApi>()
    }

    @Provides
    @ActivityRetainedScoped
    fun providesQuestionApi(
        @AuthClient retrofit: Retrofit
    ) : QuestionApi {
        return retrofit.create<QuestionApi>()
    }

    @Provides
    @ActivityRetainedScoped
    fun providesExperienceApi(
        @AuthClient retrofit: Retrofit
    ) : ExperienceApi {
        return retrofit.create<ExperienceApi>()
    }
}
