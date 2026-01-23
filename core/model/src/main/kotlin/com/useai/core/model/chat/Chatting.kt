package com.useai.core.model.chat

sealed interface Chatting {

    val message: String

    sealed interface AI : Chatting {

        data class Streaming(override val message: String) : AI
        data class Done(val isLetter: Boolean, override val message: String) : AI
    }

    data class User(override val message: String) : Chatting
}
