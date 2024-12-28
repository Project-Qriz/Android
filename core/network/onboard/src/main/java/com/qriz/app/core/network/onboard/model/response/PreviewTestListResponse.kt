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
    @SerialName("question") val question: String,
    @SerialName("option1") val option1: String,
    @SerialName("option2") val option2: String,
    @SerialName("option3") val option3: String,
    @SerialName("option4") val option4: String,
    @SerialName("timeLimit") val timeLimit: Int,
    //@SerialName("skill") val skill: List<SkillResponseModel>,
)

//TODO : API 문서상 정의되지 않은 내용 (확인 후 삭제)
@Serializable
data class SkillResponseModel(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("type") val type: String,
    @SerialName("keyConcepts") val keyConcepts: String,
    @SerialName("frequency") val frequency: Int,
    @SerialName("description") val description: String,
)