package com.qriz.app.core.network.daily_study.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeeklyRecommendationResponseContainer(
    val recommendations: List<WeeklyRecommendationResponse>
)

@Serializable
data class WeeklyRecommendationResponse(
    @SerialName("skillId") val skillId: Long,
    @SerialName("keyConcepts") val keyConcepts: String,
    @SerialName("description") val description: String,
    @SerialName("importanceLevel") val importanceLevel: String,
    @SerialName("frequency") val frequency: Int,
    @SerialName("incorrectRate") val incorrectRate: Double?
)
