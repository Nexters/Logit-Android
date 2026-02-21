package com.useai.core.model.account

data class Login(
    val accessToken: String,
    val refreshToken: String,
    val isNewUser: Boolean,
)
