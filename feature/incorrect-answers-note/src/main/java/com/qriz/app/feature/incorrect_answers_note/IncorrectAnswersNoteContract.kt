package com.qriz.app.feature.incorrect_answers_note

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState

@Immutable
data class IncorrectAnswersNoteUiState(
    val isLoading: Boolean,
) : UiState {

    companion object {
        val Default = IncorrectAnswersNoteUiState(
            isLoading = false
        )
    }
}

sealed interface IncorrectAnswersNoteUiAction : UiAction {
}

sealed interface IncorrectAnswersNoteUiEffect : UiEffect {
    data class ShowSnackBar(
        @StringRes val defaultResId: Int,
        val message: String? = null
    ) : IncorrectAnswersNoteUiEffect
}
