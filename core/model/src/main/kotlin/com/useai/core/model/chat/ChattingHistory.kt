package com.useai.core.model.chat

import java.time.LocalDateTime

data class ChattingHistory(
    val chattings: List<ChattingContent>,
    val projectCreatedAt: LocalDateTime,
    val experienceIds: Set<String>,
    val questionId: String,
    val projectName: String,
    val questionTitle: String,
    val nextCursor: String?,
    val hasMore: Boolean,
    val remainingChats: Int
)

sealed interface ChattingContent {

    val id: String
    val message: String
    val createdAt: LocalDateTime

    data class AI(
        override val id: String,
        override val message: String,
        override val createdAt: LocalDateTime,
        val isLetter: Boolean
    ) : ChattingContent

    data class User(
        override val id: String,
        override val message: String,
        override val createdAt: LocalDateTime
    ) : ChattingContent
}
