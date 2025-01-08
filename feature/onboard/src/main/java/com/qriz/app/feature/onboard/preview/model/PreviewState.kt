package com.qriz.app.feature.onboard.preview.model

import com.qriz.app.core.data.onboard.onboard_api.model.Question

data class PreviewState(
    val questions: List<Question> = emptyList(),
    val myAnswer: Map<Long, String> = emptyMap(),
    val remainTime: Long = 0,
    val totalTimeLimit: Long = 0,
    val currentPage: Int = 0,
) {
    val isLoading = questions.isEmpty()

    val canTurnNextPage: Boolean = isLoading.not() && myAnswer.containsKey(questions[currentPage].id)

    val progressPercent: Float = if (isLoading) 0f else remainTime.toFloat() / totalTimeLimit.toFloat()

    val remainTimeText: String
        get() {
            val remainedSeconds = remainTime / 1000
            val minutes = remainedSeconds / 60
            val seconds = remainedSeconds % 60

            return "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
        }
}
