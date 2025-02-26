package com.qriz.app.feature.concept_book

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState

@Immutable
data class ConceptBookUiState(
    val isLoading: Boolean,
) : UiState {

    companion object {
        val Default = ConceptBookUiState(
            isLoading = false
        )
    }
}

sealed interface ConceptBookUiAction : UiAction {
}

sealed interface ConceptBookUiEffect : UiEffect {
    data class ShowSnackBar(
        @StringRes val defaultResId: Int,
        val message: String? = null
    ) : ConceptBookUiEffect
}
