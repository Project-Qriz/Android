package com.qriz.app.core.data.daily_study.daily_study.mapper

import com.qriz.app.core.data.daily_study.daily_study_api.model.DailyStudyPlan
import com.qriz.app.core.data.daily_study.daily_study_api.model.PlannedSkill
import com.qriz.app.core.network.daily_study.model.response.DailyStudyPlanResponse
import com.qriz.app.core.network.daily_study.model.response.PlannedSkillResponse
import java.time.LocalDate

internal fun List<DailyStudyPlanResponse>.toDailyStudyPlan() = map { it.toDailyStudyPlan() }

private fun DailyStudyPlanResponse.toDailyStudyPlan() = DailyStudyPlan(
    id = id,
    planDate = LocalDate.parse(planDate),
    completionDate = if (completionDate != null) LocalDate.parse(completionDate) else null,
    plannedSkills = plannedSkills.map { it.toPlannedSkill() },
    completed = completed,
    reviewDay = reviewDay,
    comprehensiveReviewDay = comprehensiveReviewDay,
)

private fun PlannedSkillResponse.toPlannedSkill() = PlannedSkill(
    id = id,
    type = type,
    keyConcept = keyConcept,
    description = description,
)
