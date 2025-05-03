package com.qriz.app.core.network.user.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JoinResponse(
    @SerialName("id") val id: Long,
    @SerialName("username") val userId: String,
    @SerialName("nickname") val name: String,
)
