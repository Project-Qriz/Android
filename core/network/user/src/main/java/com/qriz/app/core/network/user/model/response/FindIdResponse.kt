package com.qriz.app.core.network.user.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FindIdResponse(
    @SerialName("nickname") val username: String,
    @SerialName("date") val date: String
)