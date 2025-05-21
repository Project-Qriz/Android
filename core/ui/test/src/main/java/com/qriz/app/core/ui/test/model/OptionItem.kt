package com.qriz.app.core.ui.test.model

import androidx.compose.runtime.Immutable

@Immutable
sealed class OptionItem(
    open val id: Long,
    open val description: String,
)

@Immutable
data class SelectedOrCorrectOptionItem(
    override val id: Long,
    override val description: String,
) : OptionItem(id, description)

@Immutable
data class SelectedAndIncorrectOptionItem(
    override val id: Long,
    override val description: String,
) : OptionItem(id, description)

@Immutable
data class GeneralOptionItem(
    override val id: Long,
    override val description: String,
) : OptionItem(id, description)
