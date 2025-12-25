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
) {
    fun isNotAvailable(): Boolean = plannedSkills.isEmpty()
}

/**
 * 이 학습 카드가 열리기 위해 완료되어야 하는 마지막 day 번호를 반환합니다.
 * @param allPlans 전체 학습 계획 리스트
 * @param currentDayNumber 현재 카드의 day 번호 (1-based)
 * @return 완료되어야 하는 마지막 day 번호, 조건이 없으면 null
 */
fun DailyStudyPlan.getRequiredCompletionDay(
    allPlans: List<DailyStudyPlan>,
    currentDayNumber: Int
): Int? {
    if (!reviewDay) return null

    return if (comprehensiveReviewDay) {
        allPlans.withIndex()
            .lastOrNull { !it.value.comprehensiveReviewDay }
            ?.index
            ?.plus(1)
    } else {
        allPlans.take(currentDayNumber - 1) // 현재 day 이전까지만
            .withIndex()
            .lastOrNull { !it.value.reviewDay }
            ?.index
            ?.plus(1)
    }
}

/**
 * 이 학습 카드가 잠겨있는지 확인합니다.
 * @param allPlans 전체 학습 계획 리스트
 * @param currentDayNumber 현재 카드의 day 번호 (1-based)
 * @return 잠겨있으면 true, 열려있으면 false
 */
fun DailyStudyPlan.isLocked(
    allPlans: List<DailyStudyPlan>,
    currentDayNumber: Int
): Boolean {
    val requiredDay = getRequiredCompletionDay(allPlans, currentDayNumber)
        ?: return false // 조건이 없으면 잠기지 않음

    // requiredDay까지의 학습이 모두 완료되었는지 확인
    return !allPlans
        .take(requiredDay)
        .all { it.completed }
}
