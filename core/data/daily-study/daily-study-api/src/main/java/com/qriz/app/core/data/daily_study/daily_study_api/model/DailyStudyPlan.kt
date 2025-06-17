package com.qriz.app.core.data.daily_study.daily_study_api.model

import androidx.compose.runtime.Immutable

@Immutable
data class DailyStudyPlan(
    val id: Long,
    val completed: Boolean,
    val planDate: String,
    val completionDate: String?,
    val plannedSkills: List<PlannedSkill>,
    val reviewDay: Boolean,
    val comprehensiveReviewDay: Boolean
)
