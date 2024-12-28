package com.qriz.app.core.network.onboard.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PreviewCompleteResponse(
    @SerialName("previewId") val previewId: Long,
    @SerialName("userId") val userId: Long,
    @SerialName("skillId") val skillId: Long,
    @SerialName("completed") val completed: Boolean,
    @SerialName("completionDate") val completionDate: String,
)