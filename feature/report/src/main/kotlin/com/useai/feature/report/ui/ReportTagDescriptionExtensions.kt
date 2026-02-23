package com.useai.feature.report.ui

import androidx.annotation.StringRes
import com.useai.core.designsystem.R

private enum class ReportTagGroup {
    COMMON,
    IT_TECH,
    DESIGN_ART,
    PLANNING_BUSINESS,
    MARKETING_SALES,
    OPERATIONS_SUPPORT,
}

private val itTechKeywords = listOf(
    "개발", "프론트", "백엔드", "서버", "api", "db", "데이터", "ai", "ml", "llm",
    "android", "ios", "kotlin", "java", "python", "javascript", "typescript",
    "react", "node", "클라우드", "infra", "보안", "코딩", "알고리즘",
)

private val designArtKeywords = listOf(
    "디자인", "ui", "ux", "그래픽", "일러스트", "편집", "영상", "모션", "아트", "예술", "figma", "photoshop",
)

private val planningBusinessKeywords = listOf(
    "기획", "전략", "사업", "비즈니스", "서비스기획", "pm", "po", "프로덕트", "로드맵", "신사업",
)

private val marketingSalesKeywords = listOf(
    "마케팅", "영업", "광고", "세일즈", "캠페인", "그로스", "crm", "전환", "리텐션", "브랜드", "sns",
)

private val operationsSupportKeywords = listOf(
    "운영", "지원", "cs", "고객지원", "qa", "품질", "정산", "물류", "총무", "재무", "회계", "인사", "hr", "관리", "모니터링",
)

private fun String.toReportTagGroup(): ReportTagGroup {
    val keyword = trim().lowercase()

    return when {
        itTechKeywords.any { keyword.contains(it) } -> ReportTagGroup.IT_TECH
        designArtKeywords.any { keyword.contains(it) } -> ReportTagGroup.DESIGN_ART
        planningBusinessKeywords.any { keyword.contains(it) } -> ReportTagGroup.PLANNING_BUSINESS
        marketingSalesKeywords.any { keyword.contains(it) } -> ReportTagGroup.MARKETING_SALES
        operationsSupportKeywords.any { keyword.contains(it) } -> ReportTagGroup.OPERATIONS_SUPPORT
        else -> ReportTagGroup.COMMON
    }
}

@StringRes
internal fun String.reportTagDescriptionRes(): Int = when (toReportTagGroup()) {
    ReportTagGroup.COMMON -> R.string.report_tag_desc_common
    ReportTagGroup.IT_TECH -> R.string.report_tag_desc_it_tech
    ReportTagGroup.DESIGN_ART -> R.string.report_tag_desc_design_art
    ReportTagGroup.PLANNING_BUSINESS -> R.string.report_tag_desc_planning_business
    ReportTagGroup.MARKETING_SALES -> R.string.report_tag_desc_marketing_sales
    ReportTagGroup.OPERATIONS_SUPPORT -> R.string.report_tag_desc_operations_support
}
