package com.useai.core.model.account

import java.time.LocalDateTime

data class User(
    val id: String,
    val oauthProvider: String,
    val email: String,
    val fullName: String,
    val profileImageUrl: String,
    val createdAt: LocalDateTime,
    val isActive: Boolean,
)
