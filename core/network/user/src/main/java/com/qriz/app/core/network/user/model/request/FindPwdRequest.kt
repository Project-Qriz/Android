package com.qriz.app.core.network.user.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FindPwdRequest(
    @SerialName("username") val username: String,
    @SerialName("email") val email: String,
)