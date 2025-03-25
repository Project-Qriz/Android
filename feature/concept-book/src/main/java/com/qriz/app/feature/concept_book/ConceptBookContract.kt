package com.qriz.app.feature.concept_book

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.qriz.app.core.data.conceptbook.conceptbook_api.model.Subject
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class ConceptBookUiState(
    val isLoading: Boolean,
    val subjects: ImmutableList<Subject>,
) : UiState {

    companion object {
        val Default = ConceptBookUiState(
            isLoading = false,
            subjects = persistentListOf(),
        )
    }
}

sealed interface ConceptBookUiAction : UiAction {
    data object Initialize : ConceptBookUiAction
}

sealed interface ConceptBookUiEffect : UiEffect {
    data class ShowSnackBar(
        @StringRes val defaultResId: Int,
        val message: String? = null
    ) : ConceptBookUiEffect
}
