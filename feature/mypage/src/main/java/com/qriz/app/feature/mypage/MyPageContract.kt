package com.qriz.app.feature.mypage

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.qriz.app.core.ui.common.const.ExamScheduleState
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState
import com.quiz.app.core.data.user.user_api.model.User

@Immutable
data class MyPageUiState(
    val isLoading: Boolean,
    val user: User,
    val showExamBottomSheet: Boolean,
    val showResetPlanDialog: Boolean,
    val examScheduleState: ExamScheduleState,
) : UiState {

    companion object {
        val Default = MyPageUiState(
            isLoading = false,
            user = User.Default,
            showExamBottomSheet = false,
            showResetPlanDialog = false,
            examScheduleState = ExamScheduleState.Loading
        )
    }
}

sealed interface MyPageUiAction : UiAction {
    data object LoadData : MyPageUiAction
    data object ShowExamBottomSheet : MyPageUiAction
    data object ShowResetPlanDialog : MyPageUiAction
    data object DismissExamBottomSheet : MyPageUiAction
    data object DismissResetPlanDialog : MyPageUiAction
    data class ClickExamSchedule(val examId: Long) : MyPageUiAction
    data object ResetPlan : MyPageUiAction
    data object ClickProfile : MyPageUiAction
}

sealed interface MyPageUiEffect : UiEffect {
    data class ShowSnackBar(
        @StringRes val defaultResId: Int,
        val message: String? = null
    ) : MyPageUiEffect

    data object NavigateSetting : MyPageUiEffect
}
