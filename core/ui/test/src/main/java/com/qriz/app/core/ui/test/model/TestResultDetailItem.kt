package com.qriz.app.core.ui.test.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class TestResultDetailItem(
    val name: String,
    val score: Int,
    val items: ImmutableList<ScoreDetailItem>
)

@Immutable
data class ScoreDetailItem(
    val name: String,
    val score: Int,
)
