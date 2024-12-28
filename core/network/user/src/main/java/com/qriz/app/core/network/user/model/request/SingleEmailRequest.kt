package com.qriz.app.core.network.user.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SingleEmailRequest(
    @SerialName("email") val email: String
)