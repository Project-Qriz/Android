package com.qriz.app.core.ui.test.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class QuestionResultItem(
    val skillName: String,
    val question: String,
    val options: ImmutableList<OptionItem>,
    val solution: String,
    val correction: Boolean
)
