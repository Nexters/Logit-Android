package com.useai.core.data.repository

import com.useai.core.model.report.ExperienceCategoryCountResult
import com.useai.core.model.report.ExperienceSummary
import com.useai.core.model.report.ExperienceTagCountResult
import com.useai.core.model.report.ExperienceTypeCountResult

interface ReportRepository {

    suspend fun getExperienceSummary(): Result<ExperienceSummary>
    suspend fun getExperienceTypeCount(): Result<ExperienceTypeCountResult>
    suspend fun getExperienceCategoryCount(): Result<ExperienceCategoryCountResult>
    suspend fun getExperienceTagCount(): Result<ExperienceTagCountResult>
}
