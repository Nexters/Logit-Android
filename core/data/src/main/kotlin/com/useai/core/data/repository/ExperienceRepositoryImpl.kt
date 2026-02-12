package com.useai.core.data.repository

import com.useai.core.common.extensions.toFormattedString
import com.useai.core.model.experience.Experience
import com.useai.core.model.experience.ExperienceParam
import com.useai.core.model.experience.MatchingExperience
import com.useai.core.network.error.runCatchingWith
import com.useai.core.network.request.CreateExperienceRequest
import com.useai.core.network.request.UpdateExperienceRequest
import com.useai.core.network.response.toExperience
import com.useai.core.network.response.toMatchingExperience
import com.useai.core.network.source.ExperienceRemoteDataSource
import com.useai.core.network.response.toMatchingExperience as toQuestionMatchingExperience // For mapper
import javax.inject.Inject

internal class ExperienceRepositoryImpl @Inject constructor(
    private val experienceRemoteDataSource: ExperienceRemoteDataSource
) : ExperienceRepository {

    override suspend fun createExperience(experience: ExperienceParam): Result<Experience> {
        return runCatchingWith {
            experienceRemoteDataSource.createExperience(
                CreateExperienceRequest(
                    category = experience.category,
                    date = experience.date.toFormattedString().orEmpty(),
                    experienceType = experience.experienceType,
                    situation = experience.situation,
                    task = experience.task,
                    action = experience.action,
                    result = experience.result,
                    title = experience.title
                )
            ).toExperience()
        }
    }

    override suspend fun getExperiences(): Result<List<Experience>> {
        return runCatchingWith {
            experienceRemoteDataSource.getExperiences().experiences.map { it.toExperience() }
        }
    }

    override suspend fun getExperience(experienceId: String): Result<Experience> {
        return runCatchingWith {
            experienceRemoteDataSource.getExperience(experienceId).toExperience()
        }
    }

    override suspend fun searchExperience(query: String): Result<List<MatchingExperience>> {
        return runCatchingWith {
            experienceRemoteDataSource.searchExperience(query).results.map { it.toMatchingExperience() }
        }
    }

    override suspend fun getMatchingExperiences(questionId: String): Result<List<MatchingExperience>> {
        return runCatchingWith {
            experienceRemoteDataSource.getMatchingExperiences(questionId).experiences.map { it.toQuestionMatchingExperience() }
        }
    }

    override suspend fun updateExperience(
        experienceId: String,
        request: UpdateExperienceRequest
    ): Result<Experience> {
        return runCatchingWith {
            experienceRemoteDataSource.updateExperience(experienceId, request).toExperience()
        }
    }

    override suspend fun deleteExperience(experienceId: String): Result<Unit> {
        return runCatchingWith {
            experienceRemoteDataSource.deleteExperience(experienceId)
        }
    }
}
