package com.qriz.app.core.data.daily_study.daily_study_api.model

data class WeeklyReviewResult(
    val totalScore: Int,
    val subjectItems: List<SubjectResult>,
)

data class SubjectResult(
    val subjectName: String,
    val score: Int,
    val categoryItems: List<CategoryResult>
)

data class CategoryResult(
    val categoryName: String,
    val score: Int,
    val conceptItems: List<ConceptResult>
)

data class ConceptResult(
    val conceptName: String,
    val score: Int,
)
