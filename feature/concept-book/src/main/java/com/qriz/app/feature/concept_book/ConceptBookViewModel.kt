package com.qriz.app.feature.concept_book

import com.qriz.app.feature.base.BaseViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject

class ConceptBookViewModel @Inject constructor(
) : BaseViewModel<ConceptBookUiState, ConceptBookUiEffect, ConceptBookUiAction>(ConceptBookUiState.Default) {
    override fun process(action: ConceptBookUiAction): Job {
        TODO("Not yet implemented")
    }

}
