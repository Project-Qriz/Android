package com.qriz.app.core.data.daily_study.daily_study.mapper

import com.qriz.app.core.data.daily_study.daily_study_api.model.ImportanceLevel
import com.qriz.app.core.data.daily_study.daily_study_api.model.WeeklyRecommendation
import com.qriz.app.core.network.daily_study.model.response.WeeklyRecommendationResponse
import com.qriz.app.core.network.daily_study.model.response.WeeklyRecommendationResponseContainer

internal fun WeeklyRecommendationResponseContainer.toWeeklyRecommendation(): List<WeeklyRecommendation> =
    recommendations.map { it.toWeeklyRecommendation() }

private fun WeeklyRecommendationResponse.toWeeklyRecommendation(): WeeklyRecommendation = WeeklyRecommendation(
    skillId = skillId,
    description = description,
    keyConcepts = keyConcepts,
    importanceLevel = ImportanceLevel.fromDisplayName(importanceLevel),
    frequency = frequency,
    incorrectRate = incorrectRate,
)
