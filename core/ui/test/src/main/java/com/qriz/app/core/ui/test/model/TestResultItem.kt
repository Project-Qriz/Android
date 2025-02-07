package com.qriz.app.core.ui.test.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.qriz.app.core.designsystem.theme.Blue200
import com.qriz.app.core.designsystem.theme.Blue400
import com.qriz.app.core.designsystem.theme.Blue700

@Immutable
data class TestResultItem(
    val scoreName: String,
    val score: Int,
    val color: Color
)

val TEST_RESULT_COLORS = listOf(
    Blue700, Blue400, Blue200
)
