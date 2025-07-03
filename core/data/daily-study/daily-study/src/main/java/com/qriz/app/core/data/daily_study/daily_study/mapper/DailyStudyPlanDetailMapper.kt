package com.qriz.app.core.data.daily_study.daily_study.mapper

import com.qriz.app.core.data.daily_study.daily_study_api.model.DailyStudyPlanDetail
import com.qriz.app.core.data.daily_study.daily_study_api.model.SimplePlannedSkill
import com.qriz.app.core.network.daily_study.model.response.DailyStudyDetailResponse
import com.qriz.app.core.network.daily_study.model.response.SimplePlannedSkillResponse

fun DailyStudyDetailResponse.toDailyStudyDetail() = DailyStudyPlanDetail(
    dayNumber = dayNumber,
    skills = skills.map { it.toSimplePlannedSkill() },
    attemptCount = status.attemptCount,
    retestEligible = status.retestEligible,
    available = status.available,
    passed = status.passed,
    totalScore = status.totalScore,
)

private fun SimplePlannedSkillResponse.toSimplePlannedSkill() = SimplePlannedSkill(
    id = id,
    keyConcept = keyConcept,
    description = description,
)
