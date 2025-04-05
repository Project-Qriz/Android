package com.qriz.app.feature.concept_book.detail

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.qriz.app.core.data.conceptbook.conceptbook_api.repository.ConceptBookRepository
import com.qriz.app.feature.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConceptBookDetailViewModel @Inject constructor(
    private val conceptBookRepository: ConceptBookRepository,
): BaseViewModel<ConceptBookDetailUiState, ConceptBookDetailUiEffect, ConceptBookDetailUiAction>(
    initialState = ConceptBookDetailUiState.Default
) {
    override fun process(action: ConceptBookDetailUiAction): Job = viewModelScope.launch {
        when(action) {
            is ConceptBookDetailUiAction.Initialize -> loadConceptBook(action.id)
        }
    }

    private suspend fun loadConceptBook(id: Long) {
        runCatching {
            conceptBookRepository.getConceptBook(id)
        }.onSuccess {
            updateState {
                copy(
                    isLoading = false,
                    subjectNumber = it.subjectNumber,
                    categoryName = it.categoryName,
                    conceptBookTitle = it.name,
                    filePath = it.file,
                    isImageLoading = true,
                    errorMessage = null
                )
            }
        }.onFailure {
            updateState {
                copy(
                    errorMessage = it.message ?: "알 수 없는 오류가 발생했습니다.",
                )
            }
        }
    }
}
