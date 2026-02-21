package com.useai.core.data.repository

import com.useai.core.model.report.ExperienceCategoryCountResult
import com.useai.core.model.report.ExperienceSummary
import com.useai.core.model.report.ExperienceTagCountResult
import com.useai.core.model.report.ExperienceTypeCountResult
import com.useai.core.network.error.runCatchingWith
import com.useai.core.network.response.toExperienceCategoryCountResult
import com.useai.core.network.response.toExperienceSummary
import com.useai.core.network.response.toExperienceTagCountResult
import com.useai.core.network.response.toExperienceTypeCountResult
import com.useai.core.network.source.ReportRemoteDataSource
import javax.inject.Inject

internal class ReportRepositoryImpl @Inject constructor(
    private val reportRemoteDataSource: ReportRemoteDataSource
) : ReportRepository {

    override suspend fun getExperienceSummary(): Result<ExperienceSummary> {
        return runCatchingWith {
            reportRemoteDataSource.getExperienceSummary().toExperienceSummary()
        }
    }

    override suspend fun getExperienceTypeCount(): Result<ExperienceTypeCountResult> {
        return runCatchingWith {
            reportRemoteDataSource.getExperienceTypeCount().toExperienceTypeCountResult()
        }
    }

    override suspend fun getExperienceCategoryCount(): Result<ExperienceCategoryCountResult> {
        return runCatchingWith {
            reportRemoteDataSource.getExperienceCategoryCount().toExperienceCategoryCountResult()
        }
    }

    override suspend fun getExperienceTagCount(): Result<ExperienceTagCountResult> {
        return runCatchingWith {
            reportRemoteDataSource.getExperienceTagCount().toExperienceTagCountResult()
        }
    }
}
