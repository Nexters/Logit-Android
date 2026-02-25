package com.useai.core.model.chat

data class Question(
    val id: String,
    val title: String,
    val maxLength: Int,
    val letter: String,
    val isCompleted: Boolean = false,
) {

    companion object {
        val EMPTY = Question("", "", 0, "", false)
    }
}
