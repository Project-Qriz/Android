package com.qriz.app.core.data.daily_study.daily_study_api.model

import androidx.compose.runtime.Immutable

@Immutable
data class WeeklyRecommendation(
    val skillId: Int,
    val keyConcepts: String,
    val description: String,
    val importanceLevel: ImportanceLevel,
    val frequency: Int,
    val incorrectRate: Double
)
