package com.useai.core.network.di

import com.useai.core.network.source.ChattingRemoteDataSource
import com.useai.core.network.source.ChattingRemoteDataSourceImpl
import com.useai.core.network.source.ExperienceRemoteDataSource
import com.useai.core.network.source.ExperienceRemoteDataSourceImpl
import com.useai.core.network.source.QuestionRemoteDataSource
import com.useai.core.network.source.QuestionRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
internal interface DataSourceModule {

    @Binds
    @ActivityRetainedScoped
    fun bindsChattingRemoteDataSource(
        chattingRemoteDataSourceImpl: ChattingRemoteDataSourceImpl
    ): ChattingRemoteDataSource

    @Binds
    @ActivityRetainedScoped
    fun bindsQuestionRemoteDataSource(
        questionRemoteDataSourceImpl: QuestionRemoteDataSourceImpl
    ): QuestionRemoteDataSource

    @Binds
    @ActivityRetainedScoped
    fun bindsExperienceRemoteDataSource(
        experienceRemoteDataSourceImpl: ExperienceRemoteDataSourceImpl
    ): ExperienceRemoteDataSource
}
