package com.qriz.app.feature.daily_study.result.model

import androidx.compose.runtime.Immutable
import com.qriz.app.core.ui.test.model.TestResultItem
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class DailyTestResultItem(
    val totalScore: Int,
    val day: Int,
    val passed: Boolean,
    val skillScores: ImmutableList<TestResultItem>,
    val questionResults: ImmutableList<DailyTestQuestionResultItem>,
)


@Immutable
data class DailyTestQuestionResultItem(
    val id: Long,
    val question: String,
    val correct: Boolean,
    val tags: ImmutableList<String>,
)
