package com.qriz.app.feature.home

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.qriz.app.core.data.application.application_api.model.Schedule
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState
import com.qriz.app.feature.home.component.UserExamUiState
import com.quiz.app.core.data.user.user_api.model.User
import kotlinx.collections.immutable.ImmutableList
import com.qriz.app.core.designsystem.R as DSR

@Immutable
data class HomeUiState(
    val isLoading: Boolean,
    val isShowExamScheduleBottomSheet: Boolean,
    val user: User,
    val userApplicationId: Long?,
    val userExamState: UserExamUiState,
    val schedulesState: SchedulesLoadState,
    val todayStudyConcepts: List<Int>,
    val currentTodayStudyDay: Int,
    val examSchedulesErrorMessage: String?,
    val applyExamErrorMessage: String?,
) : UiState {
    companion object {
        val Default = HomeUiState(
            isLoading = false,
            isShowExamScheduleBottomSheet = false,
            user = User.Default,
            schedulesState = SchedulesLoadState.Loading,
            userExamState = UserExamUiState.NoSchedule,
            todayStudyConcepts = List(30) { it + 1 },
            currentTodayStudyDay = 1,
            examSchedulesErrorMessage = null,
            userApplicationId = null,
            applyExamErrorMessage = null,
        )
    }

    @Stable
    sealed interface SchedulesLoadState {
        @Immutable
        data object Loading : SchedulesLoadState

        @Immutable
        data class Failure(val message: String) : SchedulesLoadState

        @Immutable
        data class Success(val data: ImmutableList<Schedule>) : SchedulesLoadState
    }
}

sealed interface HomeUiAction : UiAction {
    data object ObserveClient : HomeUiAction
    data class ChangeTodayStudyCard(val day: Int) : HomeUiAction
    data object ClickApply : HomeUiAction
    data object DismissTestDateBottomSheet : HomeUiAction
    data object MoveToPreviewTest : HomeUiAction
    data object LoadToExamSchedules: HomeUiAction
    data class OnClickExamSchedule(val examId: Long) : HomeUiAction
    data object RetryApplyExam : HomeUiAction
    data object DismissExamSchedulesErrorDialog : HomeUiAction
    data object DismissApplyExamErrorDialog : HomeUiAction
}

sealed interface HomeUiEffect : UiEffect {
    data object MoveToPreviewTest: HomeUiEffect
    data class ShowSnackBar(
        @StringRes val defaultResId: Int = DSR.string.empty,
        val message: String? = null
    ) : HomeUiEffect
}
