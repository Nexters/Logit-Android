package com.useai.core.network.response

import com.useai.core.common.extensions.toLocalDateTime
import com.useai.core.model.account.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class UserResponse(
    @SerialName("id") val id: String,
    @SerialName("oauth_provider") val oauthProvider: String,
    @SerialName("email") val email: String,
    @SerialName("full_name") val fullName: String,
    @SerialName("profile_image_url") val profileImageUrl: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("is_active") val isActive: Boolean,
)

fun UserResponse.toUser() = User(
    id = id,
    oauthProvider = oauthProvider,
    email = email,
    fullName = fullName,
    profileImageUrl = profileImageUrl,
    createdAt = createdAt.toLocalDateTime() ?: LocalDateTime.MIN,
    isActive = isActive
)
