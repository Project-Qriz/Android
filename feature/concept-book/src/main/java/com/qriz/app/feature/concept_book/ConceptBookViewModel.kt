package com.qriz.app.feature.concept_book

import androidx.lifecycle.viewModelScope
import com.qriz.app.core.data.conceptbook.conceptbook_api.repository.ConceptBookRepository
import com.qriz.app.feature.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConceptBookViewModel @Inject constructor(
    private val conceptBookRepository: ConceptBookRepository,
) : BaseViewModel<ConceptBookUiState, ConceptBookUiEffect, ConceptBookUiAction>(ConceptBookUiState.Default) {

    override fun process(action: ConceptBookUiAction): Job = viewModelScope.launch {
        when(action) {
            ConceptBookUiAction.Initialize -> loadSubjects()
        }
    }

    private suspend fun loadSubjects() {
        val subjects = conceptBookRepository.getData()
        updateState { copy(subjects = subjects.toImmutableList()) }
    }

}
