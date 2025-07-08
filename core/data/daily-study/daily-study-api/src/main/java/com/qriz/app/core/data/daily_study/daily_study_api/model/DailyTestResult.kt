package com.qriz.app.core.data.daily_study.daily_study_api.model

data class DailyTestResult(
    val passed: Boolean,
    val isReview: Boolean,
    val isComprehensiveReview: Boolean,
    val totalScore: Int,
    val skillItems: List<SkillItem>,
    val questionResults: List<QuestionResult>
)

//각 기술별 점수 합
data class SkillItem(
    val skillId: Long,
    val score: Double,
)

//각 문제에 대한 결과
data class QuestionResult(
    val questionId: Long,
    val detailType: String,
    val question: String,
    val correct: Boolean,
)
