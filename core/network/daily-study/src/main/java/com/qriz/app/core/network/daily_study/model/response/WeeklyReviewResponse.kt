package com.qriz.app.core.network.daily_study.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeeklyReviewResponse(
    @SerialName("subjects") val subjects: List<SubjectResponse>,
    @SerialName("totalScore") val totalScore: Int,
)

@Serializable
data class SubjectResponse(
    @SerialName("title") val title: String,
    @SerialName("totalScore") val totalScore: Int,
    @SerialName("majorItems") val majorItems: List<MajorItemResponse>,
)

@Serializable
data class MajorItemResponse(
    @SerialName("majorItem") val majorItem: String,
    @SerialName("score") val score: Int,
    @SerialName("subItemScores") val subItemScores: List<SubItemScoreResponse>,
)

@Serializable
data class SubItemScoreResponse(
    @SerialName("subItem") val subItem: String,
    @SerialName("score") val score: Int,
)
