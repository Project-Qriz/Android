package com.qriz.app.feature.daily_study.result.mapper

import com.qriz.app.core.data.conceptbook.conceptbook_api.model.Subject
import com.qriz.app.core.data.daily_study.daily_study_api.model.DailyTestResult
import com.qriz.app.core.data.daily_study.daily_study_api.model.QuestionResult
import com.qriz.app.core.ui.test.model.TestResultItem
import com.qriz.app.feature.daily_study.result.model.DailyTestQuestionResultItem
import com.qriz.app.feature.daily_study.result.model.DailyTestResultItem
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

internal fun DailyTestResult.toDailyTestResultItem(
    day: Int,
    subjects: List<Subject>,
) = DailyTestResultItem(
    totalScore = totalScore,
    passed = passed,
    day = day,
    skillScores = skillItems.mapNotNull { skillItem ->
        subjects.flatMap { it.categories }.flatMap { it.conceptBooks }
            .find { it.id == skillItem.skillId }?.let { category ->
                TestResultItem(
                    scoreName = category.name,
                    score = skillItem.score.toInt()
                )
            }
    }.sortedByDescending { it.score }.toImmutableList(),
    questionResults = questionResults.map { it.toQuestionResultItem() }
        .toImmutableList(),
)

private fun QuestionResult.toQuestionResultItem() = DailyTestQuestionResultItem(
    id = questionId,
    question = question,
    correct = correct,
    tags = persistentListOf(detailType)
)
