package com.qriz.app.feature.daily_study.status

import com.qriz.app.core.data.daily_study.daily_study_api.model.SimplePlannedSkill
import com.qriz.app.core.designsystem.theme.Blue500
import com.qriz.app.core.designsystem.theme.Gray100
import com.qriz.app.core.designsystem.theme.Gray200
import com.qriz.app.core.designsystem.theme.Gray600
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.Red700
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.featrue.daily_study.R
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class DailyStudyPlanStatusUiState(
    val isLoading: Boolean,
    val errorMessage: String?,
    val attemptCount: Int,
    val isReview: Boolean,
    val isComprehensiveReview: Boolean,
    val available: Boolean,
    val canRetry: Boolean,
    val score: Double,
    val passed: Boolean,
    val showRetryConfirmationDialog: Boolean,
    val skills: ImmutableList<SimplePlannedSkill>
) : UiState {

    val skillTitleResId = if (isComprehensiveReview) R.string.comprehensive_review_skills
    else if (isReview) R.string.weekly_review_skills
    else R.string.daily_study

    val testMessageResId = if (available.not()) R.string.please_check_before_plan
    else if (available && attemptCount == 0) R.string.need_test_to_next_plan
    else if (available && attemptCount > 0 && canRetry) R.string.can_retry
    else R.string.completed_plan

    val isComplete: Boolean = passed || attemptCount == 2

    val testStatusTextResId = when {
        isComplete -> R.string.study_complete
        available.not() -> R.string.study_not_available
        else -> R.string.before_study
    }

    val testStatusTextColor = when {
        available.not() -> Red700
        isComplete -> Blue500
        else -> Gray600
    }

    val testCardBackgroundColor = if (available) White else Gray100

    val testCardIconColor = if (available) Gray800 else Gray200

    val canTest = available && (attemptCount == 0 || canRetry)

    companion object {
        val DEFAULT = DailyStudyPlanStatusUiState(
            isLoading = true,
            errorMessage = null,
            attemptCount = 0,
            isReview = false,
            isComprehensiveReview = false,
            available = false,
            canRetry = false,
            score = 0.0,
            passed = false,
            showRetryConfirmationDialog = false,
            skills = persistentListOf()
        )
    }
}

sealed interface DailyStudyPlanStatusUiEffect : UiEffect {
    data class MoveToTest(val day: Int) : DailyStudyPlanStatusUiEffect
}

sealed interface DailyStudyPlanStatusUiAction : UiAction {
    data object LoadData : DailyStudyPlanStatusUiAction
    data object ShowRetryConfirmDialog : DailyStudyPlanStatusUiAction
    data object DismissRetryConfirmDialog : DailyStudyPlanStatusUiAction
    data object MoveToTest : DailyStudyPlanStatusUiAction
    data object ClickTestCard : DailyStudyPlanStatusUiAction
}
