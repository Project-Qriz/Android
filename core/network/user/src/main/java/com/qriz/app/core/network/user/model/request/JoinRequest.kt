package com.qriz.app.core.network.user.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JoinRequest(
    @SerialName("username") val username: String,
    @SerialName("password") val password: String,
    @SerialName("nickname") val nickname: String,
    @SerialName("email") val email: String,
)