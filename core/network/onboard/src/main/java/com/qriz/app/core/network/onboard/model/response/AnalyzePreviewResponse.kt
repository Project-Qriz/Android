package com.qriz.app.core.network.onboard.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnalyzePreviewResponse(
    @SerialName("estimatedScore") val estimatedScore: Float,
    @SerialName("scoreBreakdown") val scoreBreakdown: ScoreBreakdownResponseModel,
    @SerialName("weakAreaAnalysis") val weakAreaAnalysis: WeakAreaAnalysisResponseModel,
    @SerialName("topConceptsToImprove") val topConceptsToImprove: List<String>,
)

@Serializable
data class ScoreBreakdownResponseModel(
    @SerialName("totalScore") val totalScore: Int,
    @SerialName("part1Score") val part1Score: Int,
    @SerialName("part2Score") val part2Score: Int,
)

@Serializable
data class WeakAreaAnalysisResponseModel(
    @SerialName("totalQuestions") val totalQuestions: Int,
    @SerialName("weakAreas") val weakAreas: List<WeakAreaResponseModel>,
)

@Serializable
data class WeakAreaResponseModel(
    @SerialName("topic") val topic: String,
    @SerialName("incorrectCount") val incorrectCount: Int,
)
