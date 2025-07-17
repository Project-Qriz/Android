package com.qriz.app.core.network.daily_study.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DailyTestResultResponse(
    @SerialName("dayNumber")
    val dayNumber: String,
    @SerialName("passed")
    val passed: Boolean,
    @SerialName("reviewDay")
    val reviewDay: Boolean,
    @SerialName("comprehensiveReviewDay")
    val comprehensiveReviewDay: Boolean,
    @SerialName("items")
    val items: List<SkillScore>,
    @SerialName("subjectResultsList")
    val subjectResultsList: List<SubjectResultResponse>,
    @SerialName("totalScore")
    val totalScore: Double
)

@Serializable
data class SkillScore(
    @SerialName("skillId")
    val skillId: Int,
    @SerialName("score")
    val score: Double
)

@Serializable
data class SubjectResultResponse(
    @SerialName("questionId")
    val questionId: Int,
    @SerialName("detailType")
    val detailType: String,
    @SerialName("question")
    val question: String,
    @SerialName("correction")
    val correction: Boolean
)
