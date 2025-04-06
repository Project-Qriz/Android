package com.qriz.app.feature.concept_book.list

import androidx.compose.runtime.Immutable
import com.qriz.app.core.data.conceptbook.conceptbook_api.model.ConceptBook
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class ConceptBookListUiState(
    val subjectNumber: Int,
    val categoryName: String,
    val conceptBooks: ImmutableList<ConceptBook>,
    val isLoading: Boolean,
    val errorMessage: String,
) : UiState {
    companion object {
        val Default = ConceptBookListUiState(
            subjectNumber = 0,
            categoryName = "",
            conceptBooks = persistentListOf(),
            isLoading = true,
            errorMessage = "",
        )
    }
}

sealed interface ConceptBookListUiAction : UiAction {
    data class Initialize(val categoryName: String) : ConceptBookListUiAction
    data class ClickConceptBook(val conceptBookId: Long) : ConceptBookListUiAction
}

sealed interface ConceptBookListUiEffect : UiEffect {
    data class NavigateToConceptBook(val id: Long) : ConceptBookListUiEffect
}
