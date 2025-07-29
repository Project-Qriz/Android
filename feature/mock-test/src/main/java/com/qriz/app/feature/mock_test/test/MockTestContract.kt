package com.qriz.app.feature.mock_test.test

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.qriz.app.core.ui.test.model.OptionItem
import com.qriz.app.core.ui.test.model.QuestionTestItem
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class MockTestUiState(
    val state: LoadState,
    val remainTimeMs: Long,
    val totalTimeLimitMs: Long,
    val currentIndex: Int,
    val isVisibleTestSubmitWarningDialog: Boolean,
    val isVisibleTestEndWarningDialog: Boolean,
    val submitErrorMessage: String?,
) : UiState {
    val progressPercent: Float = when {
        (totalTimeLimitMs == 0L) -> 0f
        else -> remainTimeMs.toFloat() / totalTimeLimitMs.toFloat()
    }

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

    companion object {
        val Default = MockTestUiState(
            state = LoadState.Loading,
            remainTimeMs = 0,
            totalTimeLimitMs = 0,
            currentIndex = 0,
            isVisibleTestSubmitWarningDialog = false,
            isVisibleTestEndWarningDialog = false,
            submitErrorMessage = null,
        )
    }

    sealed interface LoadState {
        data object Loading : LoadState
        data class Success(val questions: ImmutableList<QuestionTestItem>) : LoadState
        data class Failure(val errorMessage: String) : LoadState
    }
}

sealed interface MockTestUiAction : UiAction {
    data object ObserveMockTestItem : MockTestUiAction
    data class SelectOption(
        val questionID: Long, val option: OptionItem
    ) : MockTestUiAction

    data object ClickNextPage : MockTestUiAction
    data object ClickPreviousPage : MockTestUiAction
    data object ClickSubmit : MockTestUiAction
    data object ClickCancel : MockTestUiAction
    data object ClickConfirmTestSubmitWarningDialog : MockTestUiAction
    data object ClickDismissTestSubmitWarningDialog : MockTestUiAction
    data object ClickConfirmTestEndWarningDialog : MockTestUiAction
    data object ClickDismissTestEndWarningDialog : MockTestUiAction
}

sealed interface MockTestUiEffect : UiEffect {
    data object MoveToResult : MockTestUiEffect
    data class ShowSnackBar(
        @StringRes val defaultResId: Int, val message: String? = null
    ) : MockTestUiEffect
    data object CancelTest : MockTestUiEffect
}
