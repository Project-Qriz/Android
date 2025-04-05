package com.qriz.app.feature.concept_book.detail

import androidx.compose.runtime.Immutable
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState

@Immutable
data class ConceptBookDetailUiState(
    val isLoading: Boolean,
    val subjectNumber: Int,
    val categoryName: String,
    val conceptBookTitle: String,
    val filePath: String,
    val isImageLoading: Boolean,
    val errorMessage: String?,
) : UiState {
    companion object {
        val Default = ConceptBookDetailUiState(
            isLoading = true,
            subjectNumber = 0,
            categoryName = "",
            conceptBookTitle = "",
            filePath = "",
            isImageLoading = false,
            errorMessage = null,
        )
    }
}

sealed interface ConceptBookDetailUiAction : UiAction {
    data class Initialize(val id: Long) : ConceptBookDetailUiAction
}

sealed interface ConceptBookDetailUiEffect : UiEffect {

}
