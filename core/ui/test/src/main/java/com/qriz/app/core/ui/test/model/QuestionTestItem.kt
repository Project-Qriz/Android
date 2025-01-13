package com.qriz.app.core.ui.test.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class QuestionTestItem(
    val id: Long,
    val question: String,
    val options: ImmutableList<OptionItem>,
    val description: String? = null,
    val timeLimit: Int,
    val isOptionSelected: Boolean,
)
