package com.useai.feature.report.ui

import androidx.annotation.StringRes
import com.useai.core.designsystem.R
import com.useai.core.model.experience.ExperienceCategory

@StringRes
internal fun ExperienceCategory.reportTopInsightDescriptionRes(): Int = when (this) {
    ExperienceCategory.PROACTIVE_EXECUTION -> R.string.report_top_insight_desc_proactive_execution
    ExperienceCategory.TECHNICAL_EXPERTISE -> R.string.report_top_insight_desc_technical_expertise
    ExperienceCategory.LOGICAL_ANALYSIS -> R.string.report_top_insight_desc_logical_analysis
    ExperienceCategory.CREATIVE_PROBLEM_SOLVING -> R.string.report_top_insight_desc_creative_problem_solving
    ExperienceCategory.COLLABORATIVE_COMMUNICATION -> R.string.report_top_insight_desc_collaborative_communication
    ExperienceCategory.TENACIOUS_RESPONSIBILITY -> R.string.report_top_insight_desc_tenacious_responsibility
    ExperienceCategory.FLEXIBLE_ADAPTABILITY -> R.string.report_top_insight_desc_flexible_adaptability
    ExperienceCategory.CUSTOMER_VALUE_ORIENTATION -> R.string.report_top_insight_desc_customer_value_orientation
}

@StringRes
internal fun ExperienceCategory?.reportTopInsightDescriptionResOrDefault(): Int =
    this?.reportTopInsightDescriptionRes() ?: R.string.report_top_insight_desc_default
