package com.qriz.app.feature.clip

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState
import com.qriz.app.feature.clip.model.ClipDataUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class ClipUiState(
    val dailyStudyClipOnlyIncorrect: Boolean,
    val dailyMockTestOnlyIncorrect: Boolean,
    val clipDays: ImmutableList<String>,
    val clipSessions: ImmutableList<String>,
    val selectedClipDay: String,
    val selectedClipSession: String,
    val dailyStudyClipUiState: ClipDataUiState,
    val mockTestClipUiState: ClipDataUiState,
    val page: Int,
    val dailyStudyShowFilterBottomSheet: Boolean,
    val dailyStudyFilterInitialPage: Int,
    val mockTestShowFilterBottomSheet: Boolean,
    val mockTestFilterInitialPage: Int,
) : UiState {

    companion object {
        const val DAILY_STUDY = 0
        const val MOCK_TEST = 1

        val Default = ClipUiState(
            dailyStudyClipOnlyIncorrect = false,
            dailyMockTestOnlyIncorrect = false,
            clipDays = persistentListOf(),
            clipSessions = persistentListOf(),
            selectedClipDay = "",
            selectedClipSession = "",
            dailyStudyClipUiState = ClipDataUiState.Loading,
            mockTestClipUiState = ClipDataUiState.Loading,
            page = DAILY_STUDY,
            dailyStudyShowFilterBottomSheet = false,
            dailyStudyFilterInitialPage = 0,
            mockTestShowFilterBottomSheet = false,
            mockTestFilterInitialPage = 0,
        )
    }
}

sealed interface ClipUiAction : UiAction {
    data object Init : ClipUiAction
    data object TabDailyStudy: ClipUiAction
    data object TabMockTest: ClipUiAction
    data class ClickFirstSubjectFilter(val tab: Int) : ClipUiAction
    data class ClickSecondSubjectFilter(val tab: Int) : ClipUiAction
    data class DismissFilterBottomSheet(val tab: Int) : ClipUiAction
    data class SelectClipDay(val day: String) : ClipUiAction
    data class SelectClipSession(val session: String) : ClipUiAction
    data class ApplyDailyStudyFilter(val concepts: ImmutableList<String>) : ClipUiAction
    data class ApplyMockTestFilter(val concepts: ImmutableList<String>) : ClipUiAction
    data class ToggleOnlyIncorrect(val tab: Int, val onlyIncorrect: Boolean) : ClipUiAction
    data object MoveToDailyStudy: ClipUiAction
    data object MoveToMockTestSessions: ClipUiAction
    data class MoveToClipDetail(val clipId: Long) : ClipUiAction
}

sealed interface ClipUiEffect : UiEffect {
    data class ShowSnackBar(
        @StringRes val defaultResId: Int,
        val message: String? = null
    ) : ClipUiEffect

    data class MoveToDailyStudy(
        val dayNumber: Int,
        val isReview: Boolean,
        val isComprehensiveReview: Boolean,
    ): ClipUiEffect

    data object MoveToMockTestSessions: ClipUiEffect
    data class MoveToClipDetail(val clipId: Long) : ClipUiEffect
}
