package com.qriz.app.feature.onboard.preview

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.qriz.app.core.data.test.test_api.model.Option
import com.qriz.app.core.data.test.test_api.model.Question
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

@Immutable
data class PreviewUiState(
    val isLoading: Boolean,
    val questions: ImmutableList<Question>,
    val selectedOptions: ImmutableMap<Long, Option>,
    val remainTimeMs: Long,
    val totalTimeLimitMs: Long,
    val currentIndex: Int,
) : UiState {
    val canTurnNextPage: Boolean =
        questions.isNotEmpty()
                && selectedOptions.containsKey(questions[currentIndex].id)

    val progressPercent: Float = when {
        (totalTimeLimitMs == 0L) -> 0f
        else -> remainTimeMs.toFloat() / totalTimeLimitMs.toFloat()
    }

    val remainTimeText: String
        get() {
            val remainedSeconds = remainTimeMs / 1000
            val minutes = remainedSeconds / 60
            val seconds = remainedSeconds % 60
            return "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
        }

    companion object {
        val Default = PreviewUiState(
            isLoading = false,
            questions = persistentListOf(),
            selectedOptions = persistentMapOf(),
            remainTimeMs = 0,
            totalTimeLimitMs = 0,
            currentIndex = 0,
        )
    }
}

sealed interface PreviewUiAction : UiAction {
    data object LoadPreviewTest : PreviewUiAction
    data class SelectOption(
        val questionID: Long,
        val option: Option
    ) : PreviewUiAction

    data object ClickNextPage : PreviewUiAction
    data object ClickPreviousPage : PreviewUiAction
    data object ClickSubmit : PreviewUiAction
    data object ClickCancel : PreviewUiAction
}

sealed interface PreviewUiEffect : UiEffect {
    data object MoveToGuide : PreviewUiEffect
    data object MoveToBack : PreviewUiEffect
    data class ShowSnackBer(
        @StringRes val defaultResId: Int,
        val message: String? = null
    ) : PreviewUiEffect
}
