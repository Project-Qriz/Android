package com.qriz.app.core.network.user.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JoinResponse(
    @SerialName("id") val id: Long,
    @SerialName("username") val username: String,
    @SerialName("name") val name: String,
    @SerialName("date") val date: String,
)