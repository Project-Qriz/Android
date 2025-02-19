package com.qriz.app.feature.onboard.previewresult.mapper

import com.qriz.app.core.data.onboard.onboard_api.model.PreviewTestResult
import com.qriz.app.core.data.test.test_api.model.SQLDSubject
import com.qriz.app.core.ui.test.model.TestResultItem
import com.qriz.app.feature.onboard.previewresult.model.PreviewTestResultItem
import com.qriz.app.feature.onboard.previewresult.model.Ranking
import com.qriz.app.feature.onboard.previewresult.model.WeakAreaItem
import kotlinx.collections.immutable.toImmutableList

fun PreviewTestResult.toPreviewTestResultItem(): PreviewTestResultItem {
    var firstMaxIncorrectCount = Int.MIN_VALUE
    var secondMaxIncorrectCount = Int.MIN_VALUE
    var thirdMaxIncorrectCount = Int.MIN_VALUE
    var fourthMaxIncorrectCount = Int.MIN_VALUE

    for (concept in weakAreas) {
        val count = concept.incorrectCount
        if (count == firstMaxIncorrectCount
            || count == secondMaxIncorrectCount
            || count == thirdMaxIncorrectCount
            || count == fourthMaxIncorrectCount
        ) continue

        when {
            count > firstMaxIncorrectCount -> {
                fourthMaxIncorrectCount = thirdMaxIncorrectCount
                thirdMaxIncorrectCount = secondMaxIncorrectCount
                secondMaxIncorrectCount = firstMaxIncorrectCount
                firstMaxIncorrectCount = count
            }
            count > secondMaxIncorrectCount -> {
                fourthMaxIncorrectCount = thirdMaxIncorrectCount
                thirdMaxIncorrectCount = secondMaxIncorrectCount
                secondMaxIncorrectCount = count
            }
            count > thirdMaxIncorrectCount -> {
                fourthMaxIncorrectCount = thirdMaxIncorrectCount
                thirdMaxIncorrectCount = count
            }
            count > fourthMaxIncorrectCount -> {
                fourthMaxIncorrectCount = count
            }
        }
    }

    return PreviewTestResultItem(
        totalScore = totalScore,
        estimatedScore = estimatedScore,
        totalQuestionsCount = totalQuestions,
        testResultItems = listOf(
            TestResultItem(
                scoreName = SQLDSubject.PART_1.title,
                score = part1Score,
            ),
            TestResultItem(
                scoreName = SQLDSubject.PART_2.title,
                score = part2Score,
            )
        ).sortedByDescending { it.score }
            .toImmutableList(),
        weakAreas = weakAreas
            .sortedByDescending { it.incorrectCount }
            .map { weakArea ->
                WeakAreaItem(
                    ranking = when (weakArea.incorrectCount) {
                        firstMaxIncorrectCount -> Ranking.FIRST
                        secondMaxIncorrectCount -> Ranking.SECOND
                        thirdMaxIncorrectCount -> Ranking.THIRD
                        else -> Ranking.OTHERS
                    },
                    topic = weakArea.topic,
                    incorrectCount = weakArea.incorrectCount
                )
            }
            .toImmutableList(),
        topConceptsToImprove = topConceptsToImprove.toImmutableList(),
    )
}
