package com.qriz.app.core.data.onboard.onboard.mapper

import com.qriz.app.core.data.onboard.onboard_api.model.PreviewTestResult
import com.qriz.app.core.data.onboard.onboard_api.model.WeakArea
import com.qriz.app.core.data.test.test_api.model.SQLDConcept
import com.qriz.app.core.network.onboard.model.response.AnalyzePreviewResponse

fun AnalyzePreviewResponse.toPreviewTestResult() =
    PreviewTestResult(
        estimatedScore = estimatedScore,
        totalScore = scoreBreakdown.totalScore,
        part1Score = scoreBreakdown.part1Score,
        part2Score = scoreBreakdown.part2Score,
        totalQuestions = weakAreaAnalysis.totalQuestions,
        weakAreas = weakAreaAnalysis.weakAreas.map {
            WeakArea(
                topic = SQLDConcept.find(it.topic),
                incorrectCount = it.incorrectCount
            )
        },
        topConceptsToImprove = topConceptsToImprove.map { SQLDConcept.find(it) }
    )
