package com.qriz.app.core.data.daily_study.daily_study_api.model

data class DailyStudyPlanDetail(
    val dayNumber: String,
    val skills: List<SimplePlannedSkill>,
    val attemptCount: Int,
    val passed: Boolean,
    val retestEligible: Boolean,
    val totalScore: Double,
    val available: Boolean,
)
