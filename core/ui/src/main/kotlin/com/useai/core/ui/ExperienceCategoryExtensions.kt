package com.useai.core.ui

import androidx.compose.ui.graphics.Color
import com.useai.core.model.experience.ExperienceCategory
import com.useai.core.designsystem.R
val ExperienceCategory.fullName: String
    get() = when (this) {
        ExperienceCategory.PROACTIVE_EXECUTION -> "주도적 실행력"
        ExperienceCategory.TECHNICAL_EXPERTISE -> "기술적 전문성"
        ExperienceCategory.LOGICAL_ANALYSIS -> "논리적 분석력"
        ExperienceCategory.CREATIVE_PROBLEM_SOLVING -> "창의적 문제해결"
        ExperienceCategory.COLLABORATIVE_COMMUNICATION -> "협업적 소통"
        ExperienceCategory.TENACIOUS_RESPONSIBILITY -> "끈기 있는 책임감"
        ExperienceCategory.FLEXIBLE_ADAPTABILITY -> "유연한 적응력"
        ExperienceCategory.CUSTOMER_VALUE_ORIENTATION -> "고객 가치 지향"
    }

val ExperienceCategory.simpleName: String
    get() = when (this) {
        ExperienceCategory.PROACTIVE_EXECUTION -> "실행력"
        ExperienceCategory.TECHNICAL_EXPERTISE -> "전문성"
        ExperienceCategory.LOGICAL_ANALYSIS -> "분석력"
        ExperienceCategory.CREATIVE_PROBLEM_SOLVING -> "문제해결력"
        ExperienceCategory.COLLABORATIVE_COMMUNICATION -> "소통력"
        ExperienceCategory.TENACIOUS_RESPONSIBILITY -> "책임감"
        ExperienceCategory.FLEXIBLE_ADAPTABILITY -> "적응력"
        ExperienceCategory.CUSTOMER_VALUE_ORIENTATION -> "고객이해력"
    }

val ExperienceCategory.iconRes: Int
    get() = when(this) {
        ExperienceCategory.PROACTIVE_EXECUTION -> R.drawable.ic_execution
        ExperienceCategory.TECHNICAL_EXPERTISE -> R.drawable.ic_expertise
        ExperienceCategory.LOGICAL_ANALYSIS -> R.drawable.ic_analysis
        ExperienceCategory.CREATIVE_PROBLEM_SOLVING -> R.drawable.ic_problem_solving
        ExperienceCategory.COLLABORATIVE_COMMUNICATION -> R.drawable.ic_communication
        ExperienceCategory.TENACIOUS_RESPONSIBILITY -> R.drawable.ic_responsibility
        ExperienceCategory.FLEXIBLE_ADAPTABILITY -> R.drawable.ic_adaptability
        ExperienceCategory.CUSTOMER_VALUE_ORIENTATION -> R.drawable.ic_customer_value
    }

val ExperienceCategory.headerRes: Int
    get() = when(this) {
        ExperienceCategory.PROACTIVE_EXECUTION -> R.drawable.header_execution
        ExperienceCategory.TECHNICAL_EXPERTISE -> R.drawable.header_expertise
        ExperienceCategory.LOGICAL_ANALYSIS -> R.drawable.header_analysis
        ExperienceCategory.CREATIVE_PROBLEM_SOLVING -> R.drawable.header_problem_solving
        ExperienceCategory.COLLABORATIVE_COMMUNICATION -> R.drawable.header_communication
        ExperienceCategory.TENACIOUS_RESPONSIBILITY -> R.drawable.header_responsibility
        ExperienceCategory.FLEXIBLE_ADAPTABILITY -> R.drawable.header_adaptability
        ExperienceCategory.CUSTOMER_VALUE_ORIENTATION -> R.drawable.header_value_orientation
    }

val ExperienceCategory.backgroundColor: Color
    get() = when(this) {
        ExperienceCategory.PROACTIVE_EXECUTION -> Color(0xFFF2F4FF)
        ExperienceCategory.TECHNICAL_EXPERTISE -> Color(0xFFE1F6FC)
        ExperienceCategory.LOGICAL_ANALYSIS -> Color(0xFFF3F2FF)
        ExperienceCategory.CREATIVE_PROBLEM_SOLVING -> Color(0xFFF7F2FF)
        ExperienceCategory.COLLABORATIVE_COMMUNICATION -> Color(0xFFE3F5FF)
        ExperienceCategory.TENACIOUS_RESPONSIBILITY -> Color(0xFFFCF2F8)
        ExperienceCategory.FLEXIBLE_ADAPTABILITY -> Color(0xFFFCF2FF)
        ExperienceCategory.CUSTOMER_VALUE_ORIENTATION -> Color(0xFFEBF7F7)
    }
