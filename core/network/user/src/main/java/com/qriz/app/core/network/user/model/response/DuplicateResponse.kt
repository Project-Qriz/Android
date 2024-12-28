package com.qriz.app.core.network.user.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DuplicateResponse(
    @SerialName("available") val available: Boolean,
)