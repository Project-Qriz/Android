package com.qriz.app.core.network.user.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsernameDuplicateRequest(
    @SerialName("available") val username: String
)