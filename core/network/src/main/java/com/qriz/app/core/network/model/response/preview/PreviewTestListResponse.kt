package com.qriz.app.core.network.model.response.preview

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PreviewTestListResponse(
    val questions: List<QuestionResponseModel>
)

@Serializable
data class QuestionResponseModel(
    @SerialName("id")val id: Long,
    @SerialName("question")val question: String,
    @SerialName("option1")val option1: String,
    @SerialName("option2")val option2: String,
    @SerialName("option3")val option3: String,
    @SerialName("option4")val option4: String,
    @SerialName("skill")val skill: List<SkillResponseModel>,
    @SerialName("timeLimit")val timeLimit: Int,
)

@Serializable
data class SkillResponseModel(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("type") val type: String,
    @SerialName("keyConcepts")val keyConcepts: String,
    @SerialName("frequency")val frequency: Int,
    @SerialName("description")val description: String,
)
