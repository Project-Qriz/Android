package com.qriz.app.feature.incorrect_answers_note

import com.qriz.app.feature.base.BaseViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject

class IncorrectAnswersNoteViewModel @Inject constructor(
) : BaseViewModel<IncorrectAnswersNoteUiState, IncorrectAnswersNoteUiEffect, IncorrectAnswersNoteUiAction>(
    IncorrectAnswersNoteUiState.Default
) {
    override fun process(action: IncorrectAnswersNoteUiAction): Job {
        TODO("Not yet implemented")
    }

}
