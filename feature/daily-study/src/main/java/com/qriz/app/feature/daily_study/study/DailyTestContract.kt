package com.qriz.app.feature.daily_study.study

import com.qriz.app.core.ui.test.model.OptionItem
import com.qriz.app.core.ui.test.model.QuestionTestItem
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState
import kotlinx.collections.immutable.ImmutableList

data class DailyTestUiState(
    val state: LoadState,
    val currentIndex: Int,
    val remainTimeMs: Long,
    val errorMessage: String? = null,
    val showSubmitConfirmationDialog: Boolean,
    val showCancelConfirmationDialog: Boolean,
) : UiState {
    val showErrorDialog = errorMessage != null

    val remainTimeText: String
        get() {
            val remainedSeconds = remainTimeMs / 1000
            val minutes = remainedSeconds / 60
            val seconds = remainedSeconds % 60
            return "${
                minutes.toString().padStart(
                    2,
                    '0'
                )
            }:${
                seconds.toString().padStart(
                    2,
                    '0'
                )
            }"
        }

    val progressPercent: Float = if (state is LoadState.Success) {
        (currentIndex + 1) / state.questions.size.toFloat()
    } else {
        0f
    }

    companion object {
        val DEFAULT = DailyTestUiState(
            state = LoadState.Loading,
            currentIndex = 0,
            remainTimeMs = 0,
            showSubmitConfirmationDialog = false,
            showCancelConfirmationDialog = false,
        )
    }

    sealed interface LoadState {
        data object Loading : LoadState
        data class Success(val questions: ImmutableList<QuestionTestItem>) : LoadState
        data class Failure(val errorMessage: String) : LoadState
    }
}

sealed interface DailyTestUiAction : UiAction {
    data object LoadData : DailyTestUiAction
    data object NextQuestion : DailyTestUiAction
    data class SelectOption(val questionId: Long, val option: OptionItem) : DailyTestUiAction
    data object ShowSubmitConfirmationDialog : DailyTestUiAction
    data object DismissSubmitConfirmationDialog : DailyTestUiAction
    data object SubmitTest : DailyTestUiAction
    data object ShowCancelConfirmationDialog : DailyTestUiAction
    data object DismissCancelConfirmationDialog : DailyTestUiAction
    data object CancelTest : DailyTestUiAction
    data object DismissErrorDialog : DailyTestUiAction
}

sealed interface DailyStudyUiEffect : UiEffect {
    data class MoveToResult(val day: Int) : DailyStudyUiEffect
    data object Cancel : DailyStudyUiEffect
    data class ShowSnackbar(val message: String) : DailyStudyUiEffect
}
