package com.qriz.app.core.network.user.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("id") val id: Long,
    @SerialName("userName") val userName: String,
    @SerialName("createdAt") val createdAt: String,
)