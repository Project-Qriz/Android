package com.qriz.app.core.network.user.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("id") val id: Long,
    @SerialName("username") val userName: String,
    @SerialName("nickname") val nickname: String,
    @SerialName("createdAt") val createdAt: String,
)
