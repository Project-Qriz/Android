package com.qriz.app.core.network.mock_test.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MockTestResultResponse(
    @SerialName("problemResults") val problemResults: List<ProblemResultResponse>,
    @SerialName("historicalScores") val historicalScores: List<HistoricalScoreResponse>
)

@Serializable
data class ProblemResultResponse(
    @SerialName("questionId") val questionId: Long,
    @SerialName("questionNum") val questionNum: Int,
    @SerialName("skillName") val skillName: String,
    @SerialName("question") val question: String,
    @SerialName("correction") val correction: Boolean
)

@Serializable
data class HistoricalScoreResponse(
    @SerialName("completionDateTime") val completionDateTime: String,
    @SerialName("itemScores") val itemScores: List<ItemScoreResponse>,
    @SerialName("attemptCount") val attemptCount: Int,
    @SerialName("displayDate") val displayDate: String
)

@Serializable
data class ItemScoreResponse(
    @SerialName("type") val type: String, @SerialName("score") val score: Double
)
