package com.qriz.app.core.data.daily_study.daily_study.mapper

import com.qriz.app.core.data.daily_study.daily_study_api.model.CategoryResult
import com.qriz.app.core.data.daily_study.daily_study_api.model.ConceptResult
import com.qriz.app.core.data.daily_study.daily_study_api.model.SubjectResult
import com.qriz.app.core.data.daily_study.daily_study_api.model.WeeklyReviewResult
import com.qriz.app.core.network.daily_study.model.response.MajorItemResponse
import com.qriz.app.core.network.daily_study.model.response.SubItemScoreResponse
import com.qriz.app.core.network.daily_study.model.response.SubjectResponse
import com.qriz.app.core.network.daily_study.model.response.WeeklyReviewResponse

internal fun WeeklyReviewResponse.toWeeklyResult() = WeeklyReviewResult(
    totalScore = totalScore,
    subjectItems = subjects.map { it.toSubjectResult() }
)

private fun SubjectResponse.toSubjectResult() = SubjectResult(
    subjectName = title,
    score = totalScore,
    categoryItems = majorItems.map { it.toCategoryResult() }
)

private fun MajorItemResponse.toCategoryResult() = CategoryResult(
    categoryName = majorItem,
    score = score,
    conceptItems = subItemScores.map { it.toConceptResult() }
)

private fun SubItemScoreResponse.toConceptResult() = ConceptResult(
    score = score,
    conceptName = subItem,
)
