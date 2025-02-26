package com.qriz.app.core.data.onboard.onboard_api.model

import com.qriz.app.core.data.test.test_api.model.SQLDConcept

data class PreviewTestResult(
    val estimatedScore: Float,
    val totalScore: Int,
    val part1Score: Int,
    val part2Score: Int,
    val totalQuestions: Int,
    val weakAreas: List<WeakArea>,
    val topConceptsToImprove: List<SQLDConcept>,
)

data class WeakArea(
    val topic: SQLDConcept,
    val incorrectCount: Int,
)
