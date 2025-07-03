package com.qriz.app.core.network.daily_study.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DailyStudyStatusResponse(
    @SerialName("attemptCount") val attemptCount: Int,
    @SerialName("passed") val passed: Boolean,
    @SerialName("retestEligible") val retestEligible: Boolean,
    @SerialName("totalScore") val totalScore: Double,
    @SerialName("available") val available: Boolean
)
