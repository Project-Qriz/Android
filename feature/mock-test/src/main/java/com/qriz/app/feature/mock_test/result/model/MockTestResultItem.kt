package com.qriz.app.feature.mock_test.result.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.qriz.app.core.designsystem.theme.Blue100
import com.qriz.app.core.designsystem.theme.Blue300
import com.qriz.app.core.designsystem.theme.Blue500
import com.qriz.app.core.designsystem.theme.Blue800
import com.qriz.app.core.designsystem.theme.Gray300
import com.qriz.app.core.ui.test.model.ScoreDetailSubjectFilter
import com.qriz.app.core.ui.test.model.TestResultDetailItem
import com.qriz.app.core.ui.test.model.TestResultItem
import com.qriz.app.feature.mock_test.result.MockTestResultUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalDateTime

private val TEST_RESULT_COLORS = listOf(
    Blue800,
    Blue500,
    Blue300,
    Blue100,
    Gray300
)

@Immutable
data class MockTestResultItem(
    val historicalScoreFilter: MockTestResultUiState.HistoricalScoreFilter,
    val totalScores: ImmutableMap<ScoreDetailSubjectFilter, Int>,
    val skillScores: ImmutableList<TestResultItem>,
    val scoreDetails: ImmutableMap<ScoreDetailSubjectFilter, ImmutableList<TestResultDetailItem>>,
    val questionResults: ImmutableList<MockTestQuestionResultItem>,
    private val historicalResults: ImmutableList<HistoricalResultItem>,
) {
    private val colors: Map<String, Color> = skillScores
        .mapIndexed { index, item -> item.scoreName to TEST_RESULT_COLORS[index] }.toMap()

    fun getScoreColor(scoreName: String): Color =
        colors[scoreName] ?: throw IllegalStateException("존재 하지 않는 점수명입니다.")

    val filterHistoricalResults: ImmutableList<ImmutableList<HistoricalScoreChartItem>> = when(historicalScoreFilter) {
        MockTestResultUiState.HistoricalScoreFilter.TOTAL -> persistentListOf(
            historicalResults.map {
                HistoricalScoreChartItem(
                    date = it.completionDate,
                    score = it.totalScore,
                )
            }.toImmutableList()
        )
        MockTestResultUiState.HistoricalScoreFilter.SUBJECT -> persistentListOf(
            historicalResults.map { result ->
                HistoricalScoreChartItem(
                    date = result.completionDate,
                    score = result.subjectResults.first { it.subject == "1과목" }.score
                )
            }.toImmutableList(),
            historicalResults.map { result ->
                HistoricalScoreChartItem(
                    date = result.completionDate,
                    score = result.subjectResults.first { it.subject == "2과목" }.score
                )
            }.toImmutableList()
        )
    }
}

@Immutable
data class HistoricalScoreChartItem(
    val date: LocalDateTime,
    val score: Int,
)

@Immutable
data class HistoricalResultItem(
    val completionDate: LocalDateTime,
    val totalScore: Int,
    val subjectResults: ImmutableList<HistoricalSubjectResultItem>,
)

@Immutable
data class HistoricalSubjectResultItem(
    val subject: String,
    val score: Int,
)

@Immutable
data class MockTestQuestionResultItem(
    val id: Long,
    val question: String,
    val correct: Boolean,
    val tags: ImmutableList<String>,
)
