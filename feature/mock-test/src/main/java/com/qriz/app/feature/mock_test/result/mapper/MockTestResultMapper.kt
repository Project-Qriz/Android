package com.qriz.app.feature.mock_test.result.mapper

import com.qriz.app.core.data.mock_test.mock_test_api.model.MockTestResult
import com.qriz.app.core.data.mock_test.mock_test_api.model.MockTestSubjectScore
import com.qriz.app.core.ui.test.model.ScoreDetailItem
import com.qriz.app.core.ui.test.model.ScoreDetailSubjectFilter
import com.qriz.app.core.ui.test.model.TestResultDetailItem
import com.qriz.app.core.ui.test.model.TestResultItem
import com.qriz.app.feature.mock_test.result.MockTestResultUiState
import com.qriz.app.feature.mock_test.result.model.HistoricalResultItem
import com.qriz.app.feature.mock_test.result.model.HistoricalSubjectResultItem
import com.qriz.app.feature.mock_test.result.model.MockTestQuestionResultItem
import com.qriz.app.feature.mock_test.result.model.MockTestResultItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap

internal fun MockTestResult.toResultItem() = MockTestResultItem(
    historicalScoreFilter = MockTestResultUiState.HistoricalScoreFilter.TOTAL,
    totalScores = mapOf(
        ScoreDetailSubjectFilter.TOTAL to subjectScores.sumOf { it.score },
        ScoreDetailSubjectFilter.FIRST to subjectScores.filter { it.title == "1과목" }.sumOf { it.score },
        ScoreDetailSubjectFilter.SECOND to subjectScores.filter { it.title == "2과목" }.sumOf { it.score },
    ).toImmutableMap(),
    skillScores = subjectScores.flatMap { it.categoryScores }.sortedByDescending { it.score }.map {
            TestResultItem(
                score = it.score,
                scoreName = it.title,
            )
        }.toImmutableList(),
    scoreDetails = mapOf(
        ScoreDetailSubjectFilter.TOTAL to subjectScores.toTestResultDetails(),
        ScoreDetailSubjectFilter.FIRST to subjectScores.filter { it.title == "1과목" }.toTestResultDetails(),
        ScoreDetailSubjectFilter.SECOND to subjectScores.filter { it.title == "2과목" }.toTestResultDetails()
    ).toImmutableMap(),
    questionResults = questionResults.map {
        MockTestQuestionResultItem(
            id = it.id,
            question = it.question,
            correct = it.correct,
            tags = persistentListOf(it.tag)
        )
    }.toImmutableList(),
    historicalResults = historicalScores.map { history ->
        HistoricalResultItem(
            totalScore = history.totalScore,
            completionDate = history.completionDateTime,
            subjectResults = history.itemScores.map {
                HistoricalSubjectResultItem(
                    subject = it.type,
                    score = it.score,
                )
            }.toImmutableList()
        )
    }.reversed().toImmutableList(),
)

private fun List<MockTestSubjectScore>.toTestResultDetails(): ImmutableList<TestResultDetailItem> =
    flatMap { it.categoryScores }
        .sortedByDescending { it.score }
        .map { category ->
            TestResultDetailItem(
                score = category.score,
                name = category.title,
                items = category.skillScores.map { skillScore ->
                    ScoreDetailItem(
                        score = skillScore.score,
                        name = skillScore.title,
                    )
                }.toImmutableList()
            )
        }.toImmutableList()
