package com.qriz.app.core.network.user.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmailAuthenticationRequest(
    @SerialName("authNum") val authNum: String,
    @SerialName("email") val email: String,
)
