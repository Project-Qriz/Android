package com.qriz.app.core.data.daily_study.daily_study.mapper

import com.qriz.app.core.data.daily_study.daily_study_api.model.DailyTestResult
import com.qriz.app.core.data.daily_study.daily_study_api.model.QuestionResult
import com.qriz.app.core.data.daily_study.daily_study_api.model.SkillItem
import com.qriz.app.core.network.daily_study.model.response.DailyTestResultResponse
import com.qriz.app.core.network.daily_study.model.response.SkillScore
import com.qriz.app.core.network.daily_study.model.response.SubjectResultResponse

fun DailyTestResultResponse.toDailyTestResult(): DailyTestResult {
    return DailyTestResult(
        passed = passed,
        isReview = reviewDay,
        isComprehensiveReview = comprehensiveReviewDay,
        totalScore = totalScore.toInt(),
        skillItems = items.map { it.toSkillItem() },
        questionResults = subjectResultsList.map { it.toQuestionResult() }
    )
}

fun SkillScore.toSkillItem(): SkillItem {
    return SkillItem(
        skillId = skillId.toLong(),
        score = score
    )
}

fun SubjectResultResponse.toQuestionResult(): QuestionResult {
    return QuestionResult(
        questionId = questionId.toLong(),
        detailType = detailType,
        question = question,
        correct = correction
    )
}
