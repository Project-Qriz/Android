package com.qriz.app.core.network.user.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyPwdResetRequest(
    @SerialName("authNumber") val authNumber: String,
)
