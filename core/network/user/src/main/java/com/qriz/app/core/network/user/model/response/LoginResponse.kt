package com.qriz.app.core.network.user.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("refreshExpiry") val refreshExpiry: String,
    @SerialName("user") val user: UserProfileResponse,
    @SerialName("refreshToken") val refreshToken: String,
    @SerialName("accessToken") val accessToken: String,
)
