package com.qriz.app.core.network.user.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResetPwdRequest(
    @SerialName("newPassword") val password: String,
    @SerialName("resetToken") val resetToken: String,
)
