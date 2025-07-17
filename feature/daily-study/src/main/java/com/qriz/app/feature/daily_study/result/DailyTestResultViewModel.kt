package com.qriz.app.feature.daily_study.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.qriz.app.core.data.conceptbook.conceptbook_api.repository.ConceptBookRepository
import com.qriz.app.core.data.daily_study.daily_study_api.model.DailyTestResult
import com.qriz.app.core.data.daily_study.daily_study_api.model.WeeklyReviewResult
import com.qriz.app.core.data.daily_study.daily_study_api.repository.DailyStudyRepository
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.model.requireValue
import com.qriz.app.core.navigation.route.DailyStudyRoute
import com.qriz.app.core.ui.common.resource.NETWORK_IS_UNSTABLE
import com.qriz.app.core.ui.common.resource.UNKNOWN_ERROR
import com.qriz.app.feature.base.BaseViewModel
import com.qriz.app.feature.daily_study.result.DailyTestResultUiState.LoadState.Failure
import com.qriz.app.feature.daily_study.result.DailyTestResultUiState.LoadState.Success
import com.qriz.app.feature.daily_study.result.mapper.toDailyTestResultItem
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailyTestResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val dailyStudyRepository: DailyStudyRepository,
    private val conceptBookRepository: ConceptBookRepository,
) : BaseViewModel<DailyTestResultUiState, DailyTestResultUiEffect, DailyTestResultUiAction>(
    DailyTestResultUiState.DEFAULT.copy(day = savedStateHandle.toRoute<DailyStudyRoute.DailyTestResult>().dayNumber)
) {
    override fun process(action: DailyTestResultUiAction): Job = viewModelScope.launch {
        when (action) {
            is DailyTestResultUiAction.LoadData -> loadData()
            is DailyTestResultUiAction.ClickFilter -> updateState { copy(showFilterDropDown = showFilterDropDown.not()) }
            is DailyTestResultUiAction.ShowDetail -> showDetail()
            is DailyTestResultUiAction.ClickBackButton -> handleBackButtonClick()
        }
    }

    private fun handleBackButtonClick() {
        val viewType = uiState.value.viewType
        if (viewType == DailyTestResultUiState.ViewType.DETAIL) {
            updateState { copy(viewType = DailyTestResultUiState.ViewType.TOTAL) }
        } else {
            sendEffect(DailyTestResultUiEffect.Close)
        }
    }

    private suspend fun showDetail() {
        updateState { copy(viewType = DailyTestResultUiState.ViewType.DETAIL) }
        when (val result = dailyStudyRepository.getWeeklyReviewResult(uiState.value.day)) {
            is ApiResult.Success<WeeklyReviewResult> -> {
                updateState { copy(weeklyReviewState = DailyTestResultUiState.WeeklyReviewState.Success(data = result.data)) }
            }

            is ApiResult.Failure -> {
                updateState { copy(weeklyReviewState = DailyTestResultUiState.WeeklyReviewState.Failure(message = result.message)) }
            }

            is ApiResult.NetworkError -> {
                updateState { copy(weeklyReviewState = DailyTestResultUiState.WeeklyReviewState.Failure(message = NETWORK_IS_UNSTABLE)) }
            }

            is ApiResult.UnknownError -> {
                updateState { copy(weeklyReviewState = DailyTestResultUiState.WeeklyReviewState.Failure(message = UNKNOWN_ERROR)) }
            }
        }

    }

    private suspend fun loadData() {
        val user = userRepository.getUser().requireValue
        val subjects = conceptBookRepository.getData()

        when (val result = dailyStudyRepository.getDailyTestResult(day = uiState.value.day)) {
            is ApiResult.Success<DailyTestResult> -> updateState {
                copy(
                    state = Success(
                        data = result.data.toDailyTestResultItem(
                            day = day,
                            subjects = subjects,
                        )
                    ),
                    userName = user.name,
                    isReview = result.data.isReview,
                    isComprehensiveReview = result.data.isComprehensiveReview,
                )
            }

            is ApiResult.Failure -> updateState {
                copy(
                    state = Failure(
                        message = result.message,
                        canRetry = false,
                    ),
                )
            }

            is ApiResult.NetworkError -> updateState {
                copy(
                    state = Failure(
                        message = NETWORK_IS_UNSTABLE,
                        canRetry = true
                    )
                )
            }

            is ApiResult.UnknownError -> updateState {
                copy(
                    state = Failure(
                        message = UNKNOWN_ERROR,
                        canRetry = true
                    )
                )
            }
        }
    }
}
