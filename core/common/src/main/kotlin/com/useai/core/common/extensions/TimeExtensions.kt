package com.useai.core.common.extensions

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun String?.toLocalDateTime(pattern: String = "yyyy-MM-dd'T'HH:mm:ss"): LocalDateTime? {
    if (this.isNullOrBlank()) return null

    return try {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        LocalDateTime.parse(this, formatter)
    } catch (e: DateTimeParseException) {
        e.printStackTrace()
        null
    }
}

fun String?.toLocalDate(pattern: String = "yyyy-MM-dd"): LocalDate? {
    if (this.isNullOrBlank()) return null

    return try {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        LocalDate.parse(this, formatter)
    } catch (e: DateTimeParseException) {
        e.printStackTrace()
        null
    }
}

fun LocalDateTime?.toFormattedString(pattern: String = "yyyy-MM-dd'T'HH:mm:ss"): String? {
    if (this == null) return null

    return try {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        this.format(formatter)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun LocalDate?.toFormattedString(pattern: String = "yyyy-MM-dd"): String? {
    if (this == null) return null

    return try {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        this.format(formatter)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
