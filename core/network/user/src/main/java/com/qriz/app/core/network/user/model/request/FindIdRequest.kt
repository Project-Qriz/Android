package com.qriz.app.core.network.user.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FindIdRequest(
    @SerialName("nickname") val nickname: String,
    @SerialName("email") val email: String,
)