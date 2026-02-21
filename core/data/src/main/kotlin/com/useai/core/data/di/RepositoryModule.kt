package com.useai.core.data.di

import com.useai.core.data.repository.AccountRepository
import com.useai.core.data.repository.AccountRepositoryImpl
import com.useai.core.data.repository.ChattingRepository
import com.useai.core.data.repository.ChattingRepositoryImpl
import com.useai.core.data.repository.ExperienceRepository
import com.useai.core.data.repository.ExperienceRepositoryImpl
import com.useai.core.data.repository.ProjectRepository
import com.useai.core.data.repository.ProjectRepositoryImpl
import com.useai.core.data.repository.QuestionRepository
import com.useai.core.data.repository.QuestionRepositoryImpl
import com.useai.core.data.repository.ReportRepository
import com.useai.core.data.repository.ReportRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
internal interface RepositoryModule {
    @Binds
    @ActivityRetainedScoped
    fun providesAccountRepository(
        impl: AccountRepositoryImpl
    ) : AccountRepository

    @Binds
    @ActivityRetainedScoped
    fun providesChattingRepository(
        impl: ChattingRepositoryImpl
    ) : ChattingRepository

    @Binds
    @ActivityRetainedScoped
    fun providesQuestionRepository(
        impl: QuestionRepositoryImpl
    ) : QuestionRepository

    @Binds
    @ActivityRetainedScoped
    fun providesExperienceRepository(
        impl: ExperienceRepositoryImpl
    ) : ExperienceRepository

    @Binds
    @ActivityRetainedScoped
    fun providesProjectRepository(
        impl: ProjectRepositoryImpl
    ) : ProjectRepository

    @Binds
    @ActivityRetainedScoped
    fun providesReportRepository(
        impl: ReportRepositoryImpl
    ) : ReportRepository
}
