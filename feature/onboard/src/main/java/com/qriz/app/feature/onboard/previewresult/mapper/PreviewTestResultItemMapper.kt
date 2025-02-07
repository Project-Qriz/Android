package com.qriz.app.feature.onboard.previewresult.mapper

import com.qriz.app.core.data.onboard.onboard_api.model.PreviewTestResult
import com.qriz.app.core.data.test.test_api.model.SQLDSubject
import com.qriz.app.core.ui.test.model.TEST_RESULT_COLORS
import com.qriz.app.core.ui.test.model.TestResultItem
import com.qriz.app.feature.onboard.previewresult.model.PreviewTestResultItem
import com.qriz.app.feature.onboard.previewresult.model.WeakAreaItem
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

fun PreviewTestResult.toPreviewTestResultItem(): PreviewTestResultItem {
    return PreviewTestResultItem(
        //TODO : 시험결과의 예상 점수 UI 추가 시, estimatedScore 추가될 예정
        totalScore = totalScore,
        totalQuestions = totalQuestions,
        testResultItems = persistentListOf(
            TestResultItem(
                scoreName = SQLDSubject.PART_1.title,
                score = part1Score,
                color = TEST_RESULT_COLORS[0]
            ),
            TestResultItem(
                scoreName = SQLDSubject.PART_2.title,
                score = part2Score,
                color = TEST_RESULT_COLORS[1]
            )
        ),
        weakAreas = weakAreas.map {
            WeakAreaItem(
                topic = it.topic,
                incorrectCount = it.incorrectCount
            )
        }.toImmutableList(),
        topConceptsToImprove = topConceptsToImprove.toImmutableList(),
    )
}
