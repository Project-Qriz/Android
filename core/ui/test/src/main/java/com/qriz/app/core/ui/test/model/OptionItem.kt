package com.qriz.app.core.ui.test.model

import androidx.compose.runtime.Immutable

@Immutable
sealed class OptionItem(
    open val description: String,
)

@Immutable
data class SelectedOrCorrectOptionItem(
    override val description: String,
) : OptionItem(description)

@Immutable
data class SelectedAndIncorrectOptionItem(
    override val description: String,
) : OptionItem(description)

@Immutable
data class GeneralOptionItem(
    override val description: String,
) : OptionItem(description)
