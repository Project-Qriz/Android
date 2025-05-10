package com.qriz.app.core.network.user.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyPwdResetRespoonse(
    @SerialName("resetToken") val resetToken: String,
)
