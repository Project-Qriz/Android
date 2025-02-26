package com.qriz.app.core.network.onboard.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TestSubmitRequest(
    @SerialName("activities") val activities: List<TestSubmitActivity>
)

@Serializable
data class TestSubmitActivity(
    @SerialName("question") val question: TestSubmitQuestion,
    @SerialName("questionNum") val questionNum: Int,
    @SerialName("checked") val checked: String
)

@Serializable
data class TestSubmitQuestion(
    @SerialName("questionId") val questionId: Long,
    @SerialName("category") val category: Int
)
