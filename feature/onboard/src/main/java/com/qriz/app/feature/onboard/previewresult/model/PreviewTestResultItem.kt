package com.qriz.app.feature.onboard.previewresult.model

import androidx.compose.runtime.Immutable
import com.qriz.app.core.data.test.test_api.model.SQLDConcept
import com.qriz.app.core.ui.test.model.TestResultItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class PreviewTestResultItem(
    val totalScore: Int,
    val estimatedScore: Float,
    val testResultItems: ImmutableList<TestResultItem>,
    val weakAreas: ImmutableList<WeakAreaItem>,
    val topConceptsToImprove: ImmutableList<SQLDConcept>,
    val totalQuestions: Int,
) {
    companion object {
        val Default = PreviewTestResultItem(
            totalScore = 0,
            estimatedScore = 0F,
            totalQuestions = 0,
            testResultItems = persistentListOf(),
            weakAreas = persistentListOf(),
            topConceptsToImprove = persistentListOf()
        )
    }
}

@Immutable
data class WeakAreaItem(
    val ranking: Ranking,
    val topic: SQLDConcept,
    val incorrectCount: Int,
)

enum class Ranking {
    FIRST,
    SECOND,
    THIRD,
}
