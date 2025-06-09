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

@Immutable
data class HomeUiState(
    val isLoading: Boolean,
    val isShowTestDateBottomSheet: Boolean,
    val user: User,
    val userExamState: UserExamUiState,
    val schedulesState: SchedulesLoadState,
    val todayStudyConcepts: List<Int>,
    val currentTodayStudyDay: Int,
    val examSchedulesErrorMessage: String?,
) : UiState {
    companion object {
        val Default = HomeUiState(
            isLoading = false,
            isShowTestDateBottomSheet = false,
            user = User.Default,
            schedulesState = SchedulesLoadState.Loading,
            userExamState = UserExamUiState.NoSchedule,
            todayStudyConcepts = List(30) { it + 1 },
            currentTodayStudyDay = 1,
            examSchedulesErrorMessage = null,
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
    data object ClickTestDateRegister : HomeUiAction
    data object DismissTestDateBottomSheet : HomeUiAction
    data object MoveToPreviewTest : HomeUiAction
    data object LoadToExamSchedules: HomeUiAction
    data object DismissExamSchedulesErrorDialog : HomeUiAction
}

sealed interface HomeUiEffect : UiEffect {
    data object MoveToPreviewTest: HomeUiEffect
    data class ShowSnackBar(
        @StringRes val defaultResId: Int,
        val message: String? = null
    ) : HomeUiEffect
}
