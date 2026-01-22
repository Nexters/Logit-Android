package com.useai.core.model.chat

sealed interface Chat {

    val message: String

    sealed interface AI : Chat {

        data class Streaming(override val message: String) : AI
        data class Done(val isLetter: Boolean, override val message: String) : AI
    }

    data class User(override val message: String) : Chat
}
