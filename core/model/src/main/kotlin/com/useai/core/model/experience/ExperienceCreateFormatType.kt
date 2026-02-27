package com.useai.core.model.experience

enum class ExperienceCreateFormatType(
    val requestValue: String,
) {
    STAR("STAR"),
    PSI("PSI"),
    FREEFORM("FREE"),
    ;

    companion object {
        fun fromRequestValue(responseValue: String): ExperienceCreateFormatType? {
            return entries.find { it.requestValue == responseValue }
        }
    }
}
