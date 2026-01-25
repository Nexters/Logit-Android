package com.useai.core.model.chat

sealed interface ChattingStreaming {

    data class Streaming(val data: String) : ChattingStreaming
    data class Done(val chatId: String, val isDraft: Boolean) : ChattingStreaming
}
