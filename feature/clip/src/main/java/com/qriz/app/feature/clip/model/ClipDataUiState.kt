package com.qriz.app.feature.clip.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList

@Stable
sealed interface ClipDataUiState {
    @Immutable
    data object Loading: ClipDataUiState

    @Immutable
    data class Success(
        val onlyIncorrect: Boolean,
        val info: ImmutableList<String>,
        val filter: ImmutableList<ClipFilterSubjectUiModel>,
        val clips: ImmutableList<ClipUiModel>
    ): ClipDataUiState

    @Immutable
    data class Failure(val message: String): ClipDataUiState
}
