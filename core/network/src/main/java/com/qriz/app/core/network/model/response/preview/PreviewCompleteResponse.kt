package com.qriz.app.core.network.model.response.preview

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate

@Serializable
data class PreviewCompleteResponse(
    @SerialName("preview_id") val previewId: Long,
    @SerialName("user_id") val userId: Long,
    @SerialName("skill_id") val skillId: Long,
    @SerialName("completed") val completed: Boolean,
    @SerialName("completion_date") val completionDate: LocalDate,
)
