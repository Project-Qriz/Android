package com.qriz.app.feature.home

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState
import com.qriz.app.feature.home.component.ExamScheduleUiState
import com.quiz.app.core.data.user.user_api.model.User

@Immutable
data class HomeUiState(
    val isLoading: Boolean,
    val isShowTestDateBottomSheet: Boolean,
    val user: User,
    val scheduleState: ExamScheduleUiState,
    val todayStudyConcepts: List<Int>,
    val currentTodayStudyDay: Int
) : UiState {
    companion object {
        val Default = HomeUiState(
            isLoading = false,
            isShowTestDateBottomSheet = false,
            user = User.Default,
            scheduleState = ExamScheduleUiState.NoSchedule,
            todayStudyConcepts = List(30) { it + 1 },
            currentTodayStudyDay = 1,
        )
    }
}

sealed interface HomeUiAction : UiAction {
    data object ObserveClient : HomeUiAction
    data class ChangeTodayStudyCard(val day: Int) : HomeUiAction
    data object ClickTestDateRegister : HomeUiAction
    data object DismissTestDateBottomSheet : HomeUiAction
    data object MoveToPreviewTest : HomeUiAction
}

sealed interface HomeUiEffect : UiEffect {
    data object MoveToPreviewTest: HomeUiEffect
    data class ShowSnackBar(
        @StringRes val defaultResId: Int,
        val message: String? = null
    ) : HomeUiEffect
}
