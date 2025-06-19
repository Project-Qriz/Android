package com.qriz.app.core.data.daily_study.daily_study_api.model

import java.time.LocalDate

data class DailyStudyPlan(
    val id: Long,
    val completed: Boolean,
    val planDate: LocalDate,
    val completionDate: LocalDate?,
    val plannedSkills: List<PlannedSkill>,
    val reviewDay: Boolean,
    val comprehensiveReviewDay: Boolean
)
