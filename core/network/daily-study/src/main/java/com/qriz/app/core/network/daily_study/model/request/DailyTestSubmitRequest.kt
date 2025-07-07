package com.qriz.app.core.network.daily_study.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DailyTestSubmitRequest(
    @SerialName("activities") val activities: List<DailyTestSubmitActivity>
)

@Serializable
data class DailyTestSubmitActivity(
    @SerialName("question") val question: DailyTestSubmitQuestion,
    @SerialName("questionNum") val questionNum: Int,
    @SerialName("optionId") val optionId: Long?,
    @SerialName("timeSpent") val timeSpent: Int,
)

@Serializable
data class DailyTestSubmitQuestion(
    @SerialName("questionId") val questionId: Long,
    @SerialName("category") val category: Int,
)
