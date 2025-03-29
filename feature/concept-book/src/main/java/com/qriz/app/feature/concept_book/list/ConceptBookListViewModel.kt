package com.qriz.app.feature.concept_book.list

import androidx.lifecycle.viewModelScope
import com.qriz.app.core.data.conceptbook.conceptbook_api.repository.ConceptBookRepository
import com.qriz.app.feature.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConceptBookListViewModel @Inject constructor(
    private val conceptBookRepository: ConceptBookRepository,
) : BaseViewModel<ConceptBookListUiState, ConceptBookListUiEffect, ConceptBookListUiAction>(
    initialState = ConceptBookListUiState.Default
) {
    override fun process(action: ConceptBookListUiAction): Job = viewModelScope.launch {
        when (action) {
            is ConceptBookListUiAction.Initialize -> loadData(action.categoryName)

            is ConceptBookListUiAction.ClickConceptBook -> sendEffect(ConceptBookListUiEffect.NavigateToConceptBook(action.conceptBookId))
        }
    }

    private fun loadData(categoryName: String) {
        updateState { copy(isLoading = true) }
        viewModelScope.launch {
            runCatching {
                conceptBookRepository.getCategoryData(categoryName)
            }.onSuccess {
                updateState {
                    copy(
                        isLoading = false,
                        subjectNumber = it.subjectNumber,
                        categoryName = it.name,
                        conceptBooks = it.conceptBooks.toImmutableList(),
                        errorMessage = ""
                    )
                }
            }.onFailure {
                updateState {
                    copy(
                        errorMessage = it.message ?: "알 수 없는 오류가 발생했습니다."
                    )
                }
            }
        }
    }
}
