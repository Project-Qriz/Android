package com.qriz.app.core.network.user.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileResponse(
    @SerialName("userId") val userId: String,
    @SerialName("email") val email: String?,
    @SerialName("name") val name: String,
    @SerialName("previewTestStatus") val previewStatus: String,
    @SerialName("provider") val provider: String?
)
