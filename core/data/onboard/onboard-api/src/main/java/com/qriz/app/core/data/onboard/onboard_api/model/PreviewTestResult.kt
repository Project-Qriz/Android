package com.qriz.app.core.data.onboard.onboard_api.model

data class PreviewTestResult(
    val estimatedScore: Float,
    val totalScore: Int,
    val part1Score: Int,
    val part2Score: Int,
    val totalQuestions: Int,
    val weakAreas: List<WeakArea>,
    val topConceptsToImprove: List<String>,
)

data class WeakArea(
    val topic: String,
    val incorrectCount: Int,
)
