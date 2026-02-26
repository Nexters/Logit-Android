package com.useai.core.model.experience

enum class ExperienceType(
    val displayName: String,
    val typeName: String,
) {
    PartTime(
        displayName = "아르바이트",
        typeName = "아르바이트",
    ),
    FullTime(
        displayName = "정규직",
        typeName = "정규직",
    ),
    Intern(
        displayName = "인턴",
        typeName = "인턴",
    ),
    Contract(
        displayName = "계약직",
        typeName = "계약직",
    ),
    Volunteer(
        displayName = "봉사 활동",
        typeName = "봉사 활동",
    ),
    Club(
        displayName = "동아리 활동",
        typeName = "동아리 활동",
    ),
    Research(
        displayName = "연구 활동",
        typeName = "연구 활동",
    ),
    Military(
        displayName = "군복무",
        typeName = "군복무",
    ),
    Scholarship(
        displayName = "수상경력",
        typeName = "수상경력",
    ),
    Personal(
        displayName = "개인활동",
        typeName = "개인 활동",
    ),
    ;

    companion object {
        val DefaultType = Personal

        fun fromTypeName(typeName: String): ExperienceType? {
            return entries.find { it.typeName == typeName }
        }
    }
}
