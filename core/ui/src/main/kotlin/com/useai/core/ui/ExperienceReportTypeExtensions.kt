package com.useai.core.ui

import com.useai.core.model.report.ExperienceReportType

val ExperienceReportType.displayName: String
    get() = when (this) {
        ExperienceReportType.PART_TIME -> "아르바이트"
        ExperienceReportType.INTERN -> "인턴"
        ExperienceReportType.FULL_TIME -> "정규직"
        ExperienceReportType.CONTRACT -> "계약직"
        ExperienceReportType.VOLUNTEER -> "봉사 활동"
        ExperienceReportType.AWARD -> "수상경력"
        ExperienceReportType.CLUB -> "동아리 활동"
        ExperienceReportType.RESEARCH -> "연구 활동"
        ExperienceReportType.MILITARY -> "군복무"
        ExperienceReportType.PERSONAL -> "개인 활동"
        ExperienceReportType.UNKNOWN -> "기타"
    }
