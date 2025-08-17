package com.qriz.app.core.data.mock_test.mock_test_api.model

import kotlinx.datetime.LocalDateTime

data class MockTestResult(
    val totalScore: Int,
    val questionResults: List<MockTestQuestionResult>,
    val historicalScores: List<MockTestScoreHistory>,
    val subjectScores: List<MockTestSubjectScore>,
)

data class MockTestQuestionResult(
    val id: Long,
    val correct: Boolean,
    val question: String,
    val tag: String
)

data class MockTestScoreHistory(
    val completionDateTime: LocalDateTime,
    val itemScores: List<MockTestScoreHistoryItem>,
    val attemptCount: Int,
    val displayDate: String,
    val totalScore: Int
)

data class MockTestSubjectScore(
    val title: String,
    val score: Int,
    val categoryScores: List<MockTestCategoryScore>
)

data class MockTestCategoryScore(
    val title: String,
    val score: Int,
    val skillScores: List<MockTestSkillScore>
)

data class MockTestSkillScore(
    val title: String,
    val score: Int,
)

data class MockTestScoreHistoryItem(
    val type: String,
    val score: Int,
)
