package com.qriz.app.core.data.daily_study.daily_study_api.model

data class WeeklyRecommendation(
    val skillId: Long,
    val keyConcepts: String,
    val description: String,
    val importanceLevel: ImportanceLevel,
    val frequency: Int,
    val incorrectRate: Double
)
