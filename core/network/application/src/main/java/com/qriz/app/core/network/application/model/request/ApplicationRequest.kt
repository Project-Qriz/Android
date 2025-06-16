package com.qriz.app.core.network.application.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApplicationRequest(
    @SerialName("applyId") val applyId: Long,
)
