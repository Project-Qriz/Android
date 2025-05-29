package com.qriz.app.core.network.application.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApplicationModifyRequest(
    @SerialName("newApplyId") val newApplyId: Long,
)
