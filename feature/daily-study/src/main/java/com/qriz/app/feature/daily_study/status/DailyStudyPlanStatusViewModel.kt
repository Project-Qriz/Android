package com.qriz.app.feature.daily_study.status

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.qriz.app.core.data.daily_study.daily_study_api.repository.DailyStudyRepository
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.navigation.route.DailyStudyRoute
import com.qriz.app.core.ui.common.resource.NETWORK_IS_UNSTABLE
import com.qriz.app.core.ui.common.resource.UNKNOWN_ERROR
import com.qriz.app.feature.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailyStudyPlanStatusViewModel @Inject constructor(
    private val dailyStudyRepository: DailyStudyRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<DailyStudyPlanStatusUiState, DailyStudyPlanStatusUiEffect, DailyStudyPlanStatusUiAction>(
    savedStateHandle.toRoute<DailyStudyRoute.DailyStudyPlanStatus>().run {
        DailyStudyPlanStatusUiState.DEFAULT.copy(
            isReview = isReview,
            isComprehensiveReview = isComprehensiveReview
        )
    }
) {
    private val dayNumber: Int = savedStateHandle.toRoute<DailyStudyRoute.DailyStudyPlanStatus>().dayNumber

    override fun process(action: DailyStudyPlanStatusUiAction): Job = viewModelScope.launch {
        when (action) {
            is DailyStudyPlanStatusUiAction.LoadData -> loadData()
            is DailyStudyPlanStatusUiAction.DismissRetryConfirmDialog -> updateState { copy(showRetryConfirmDialog = false) }
            is DailyStudyPlanStatusUiAction.ShowRetryConfirmDialog -> updateState { copy(showRetryConfirmDialog = true) }
            is DailyStudyPlanStatusUiAction.MoveToTest -> sendEffect(DailyStudyPlanStatusUiEffect.MoveToTest)
        }
    }

    private suspend fun loadData() {
        fun convertError(message: String) {
            updateState {
                copy(
                    isLoading = false,
                    errorMessage = message,
                )
            }
        }

        when (val result = dailyStudyRepository.getDailyStudyPlanDetail(dayNumber)) {
            is ApiResult.Success -> {
                updateState {
                    copy(
                        isLoading = false,
                        passed = result.data.passed,
                        score = result.data.totalScore,
                        skills = result.data.skills.toImmutableList(),
                        available = result.data.available,
                        canRetry = result.data.retestEligible,
                        attemptCount = result.data.attemptCount,
                        errorMessage = null,
                    )
                }
            }
            is ApiResult.Failure -> convertError(result.message)
            is ApiResult.NetworkError -> convertError(NETWORK_IS_UNSTABLE)
            is ApiResult.UnknownError -> convertError(UNKNOWN_ERROR)
        }
    }
}
