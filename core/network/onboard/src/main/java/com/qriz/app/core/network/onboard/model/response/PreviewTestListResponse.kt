package com.qriz.app.core.network.onboard.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PreviewTestListResponse(
    @SerialName("questions") val questions: List<QuestionResponseModel>,
    @SerialName("totalTimeLimit") val totalTimeLimit: Int,
)

@Serializable
data class QuestionResponseModel(
    @SerialName("questionId") val questionId: Long,
    @SerialName("question") val question: String,
    @SerialName("skillId") val skillId: Long,
    @SerialName("category") val category: Int?,
    @SerialName("description") val description: String?,
    @SerialName("options") val options: List<QuestionOptionModel>,
    @SerialName("timeLimit") val timeLimit: Int,
    @SerialName("difficulty") val difficulty: Int,
)

@Serializable
data class QuestionOptionModel(
    @SerialName("id") val id: Long,
    @SerialName("content") val content: String,
)
