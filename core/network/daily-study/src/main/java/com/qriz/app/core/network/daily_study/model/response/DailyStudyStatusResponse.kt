package com.qriz.app.core.network.daily_study.model.response

data class DailyStudyStatusResponse(
    val attemptCount: Int,
    val passed: Boolean,
    val retestEligible: Boolean,
    val totalScore: Double,
    val available: Boolean
)
