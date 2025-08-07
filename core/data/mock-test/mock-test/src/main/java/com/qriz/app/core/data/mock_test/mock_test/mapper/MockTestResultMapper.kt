package com.qriz.app.core.data.mock_test.mock_test.mapper

import com.qriz.app.core.data.mock_test.mock_test_api.model.MockTestCategoryScore
import com.qriz.app.core.data.mock_test.mock_test_api.model.MockTestResult
import com.qriz.app.core.data.mock_test.mock_test_api.model.MockTestScoreHistory
import com.qriz.app.core.data.mock_test.mock_test_api.model.MockTestScoreHistoryItem
import com.qriz.app.core.data.mock_test.mock_test_api.model.MockTestSkillScore
import com.qriz.app.core.data.mock_test.mock_test_api.model.MockTestSubjectScore
import com.qriz.app.core.network.mock_test.model.response.HistoricalScoreResponse
import com.qriz.app.core.network.mock_test.model.response.MajorItemScore
import com.qriz.app.core.network.mock_test.model.response.MockTestResultResponse
import com.qriz.app.core.network.mock_test.model.response.MockTestScoreResponse
import com.qriz.app.core.network.mock_test.model.response.SubItemScore
import kotlinx.datetime.LocalDateTime

fun toResult(
    mockTestResultResponse: MockTestResultResponse,
    mockTestSubjectScores: List<MockTestScoreResponse>,
): MockTestResult {
    return MockTestResult(
        totalScore = mockTestSubjectScores.sumOf { it.totalScore },
        subjectScores = mockTestSubjectScores.map { it.toSubjectScore() },
        historicalScores = mockTestResultResponse.historicalScores.map { it.toHistoricalScore() },
    )
}

private fun MockTestScoreResponse.toSubjectScore(): MockTestSubjectScore = MockTestSubjectScore(
    title = title,
    score = totalScore,
    categoryScores = majorItems.map { it.toCategoryScore() }
)

private fun MajorItemScore.toCategoryScore(): MockTestCategoryScore = MockTestCategoryScore(
    title = majorItem,
    score = score,
    skillScores = subItemScores.map { it.toSkillScore() }
)

private fun SubItemScore.toSkillScore(): MockTestSkillScore = MockTestSkillScore(
    title = subItem,
    score = score,
)

private fun HistoricalScoreResponse.toHistoricalScore(): MockTestScoreHistory {
    return MockTestScoreHistory(
        completionDateTime = completionDateTime,
        itemScores = itemScores.map { MockTestScoreHistoryItem(type = it.type, score = it.score) },
        attemptCount = attemptCount,
        displayDate = displayDate,
        totalScore = itemScores.sumOf { it.score }
    )
}
