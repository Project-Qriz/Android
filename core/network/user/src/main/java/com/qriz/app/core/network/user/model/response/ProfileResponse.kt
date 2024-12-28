package com.qriz.app.core.network.user.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    @SerialName("nickname") val nickname: String,
    @SerialName("username") val username: String,
    @SerialName("email") val email: String,
)