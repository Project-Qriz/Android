package com.qriz.app.feature.home

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.qriz.app.core.data.application.application_api.model.Schedule
import com.qriz.app.core.data.daily_study.daily_study_api.model.DailyStudyPlan
import com.qriz.app.core.data.daily_study.daily_study_api.model.WeeklyRecommendation
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState
import com.qriz.app.feature.home.component.UserExamUiState
import com.quiz.app.core.data.user.user_api.model.User
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import com.qriz.app.core.designsystem.R as DSR

@Immutable
data class HomeUiState(
    val isLoading: Boolean,
    val isShowExamScheduleBottomSheet: Boolean,
    val user: User,
    val userApplicationId: Long?,
    val dataLoadState: HomeDataLoadState,
    val userExamState: UserExamUiState,
    val selectedPlanDay: Int,
    val todayPlanIsReview: Boolean,
    val schedulesState: SchedulesLoadState,
    val dailyStudyPlans: ImmutableList<DailyStudyPlan>,
    val weeklyRecommendation: ImmutableList<WeeklyRecommendation>,
    val examSchedulesErrorMessage: String?,
    val applyExamErrorMessage: String?,
    val showPlanDayFilterBottomSheet: Boolean,
) : UiState {
    companion object {
        val Default = HomeUiState(
            isLoading = false,
            isShowExamScheduleBottomSheet = false,
            user = User.Default,
            dataLoadState = HomeDataLoadState.Loading,
            schedulesState = SchedulesLoadState.Loading,
            userExamState = UserExamUiState.NoSchedule,
            dailyStudyPlans = persistentListOf(),
            weeklyRecommendation = persistentListOf(),
            examSchedulesErrorMessage = null,
            userApplicationId = null,
            applyExamErrorMessage = null,
            selectedPlanDay = 0,
            todayPlanIsReview = false,
            showPlanDayFilterBottomSheet = false,
        )
    }

    @Stable
    sealed interface HomeDataLoadState {
        @Immutable
        data object Success: HomeDataLoadState

        @Immutable
        data class Failure(val message: String): HomeDataLoadState

        @Immutable
        data object Loading: HomeDataLoadState
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
    data class ChangeStudyPlanDate(val day: Int) : HomeUiAction
    data object ChangeStudyPlanDateToToday : HomeUiAction
    data object ClickApply : HomeUiAction
    data object DismissTestDateBottomSheet : HomeUiAction
    data object MoveToPreviewTest : HomeUiAction
    data object LoadToExamSchedules: HomeUiAction
    data class ClickExamSchedule(val examId: Long) : HomeUiAction
    data object RetryApplyExam : HomeUiAction
    data object DismissExamSchedulesErrorDialog : HomeUiAction
    data object DismissApplyExamErrorDialog : HomeUiAction
    data object ClickRetryDataLoad : HomeUiAction
    data object ShowPlanDayFilterBottomSheet : HomeUiAction
    data object DismissPlanDayFilterBottomSheet : HomeUiAction
}

sealed interface HomeUiEffect : UiEffect {
    data object MoveToPreviewTest: HomeUiEffect
    data class ShowSnackBar(
        @StringRes val defaultResId: Int = DSR.string.empty,
        val message: String? = null
    ) : HomeUiEffect
}
