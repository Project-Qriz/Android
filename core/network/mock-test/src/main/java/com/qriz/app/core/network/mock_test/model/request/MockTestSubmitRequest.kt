package com.qriz.app.core.network.mock_test.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MockTestSubmitRequest(
    @SerialName("activities") val activities: List<MockTestSubmitActivity>
)

@Serializable
data class MockTestSubmitActivity(
    @SerialName("question") val question: MockTestSubmitQuestion,
    @SerialName("questionNum") val questionNum: Int,
    @SerialName("optionId") val optionId: Long,
)

@Serializable
data class MockTestSubmitQuestion(
    @SerialName("questionId") val questionId: Long,
    @SerialName("category") val category: Int
)
