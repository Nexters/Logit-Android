package com.useai.core.model.chat

data class ChattingHistory(
    val chattings: List<ChattingContent>,
)

sealed interface ChattingContent {

    val id: String
    val message: String
    val createdAt: String

    data class AI(
        override val id: String,
        override val message: String,
        override val createdAt: String,
        val isLetter: Boolean
    ) : ChattingContent

    data class User(
        override val id: String,
        override val message: String,
        override val createdAt: String
    ) : ChattingContent
}
