package com.qriz.app.feature.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.qriz.app.core.data.application.application_api.model.DdayType
import com.qriz.app.core.data.application.application_api.model.UserExam
import com.qriz.app.core.data.application.application_api.repository.ExamRepository
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.ui.common.resource.NETWORK_IS_UNSTABLE
import com.qriz.app.core.ui.common.resource.UNKNOWN_ERROR
import com.qriz.app.feature.base.BaseViewModel
import com.qriz.app.feature.home.component.UserExamUiState
import com.qriz.app.feature.home.HomeUiState.*
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    userRepository: UserRepository,
    private val examRepository: ExamRepository,
) : BaseViewModel<HomeUiState, HomeUiEffect, HomeUiAction>(HomeUiState.Default) {

    private val dataFlow = combine(
        userRepository.getUserFlow(),
        examRepository.getUserExams(),
    ) { user, exam ->
        if (exam !is ApiResult.Success) {
            handleError(exam)
            return@combine uiState.value
        }

        uiState.value.copy(
            user = user,
            userExamState = exam.data.toUiState()
        )
    }

    override fun process(action: HomeUiAction): Job = viewModelScope.launch {
        when (action) {
            is HomeUiAction.ObserveClient -> observeClient()
            is HomeUiAction.ChangeTodayStudyCard -> onChangeTodayStudyCard(action.day)
            is HomeUiAction.ClickTestDateRegister -> onClickTestDateRegister()
            is HomeUiAction.DismissTestDateBottomSheet -> onDismissTestDateBottomSheet()
            is HomeUiAction.MoveToPreviewTest -> sendEffect(HomeUiEffect.MoveToPreviewTest)
            is HomeUiAction.LoadToExamSchedules -> loadToExamSchedules()
            is HomeUiAction.DismissExamSchedulesErrorDialog -> updateState {
                copy(examSchedulesErrorMessage = null)
            }
        }
    }

    private fun onChangeTodayStudyCard(day: Int) {
        updateState { copy(currentTodayStudyDay = day) }
    }

    private fun observeClient() = viewModelScope.launch {
        dataFlow.collect { updateState { it } }
    }

    private fun onClickTestDateRegister() {
        updateState { copy(isShowTestDateBottomSheet = true) }
        loadToExamSchedules()
    }

    private fun onDismissTestDateBottomSheet() {
        updateState { copy(isShowTestDateBottomSheet = false) }
    }

    private fun loadToExamSchedules() = viewModelScope.launch {
        updateState {
            copy(
                schedulesState = SchedulesLoadState.Loading,
                isShowTestDateBottomSheet = true,
                examSchedulesErrorMessage = null,
            )
        }

        when (val result = examRepository.getExamSchedules()) {
            is ApiResult.Failure -> updateState {
                copy(
                    schedulesState = SchedulesLoadState.Failure(result.message),
                    isShowTestDateBottomSheet = false,
                    examSchedulesErrorMessage = result.message
                )
            }

            is ApiResult.NetworkError -> updateState {
                copy(
                    schedulesState = SchedulesLoadState.Failure(NETWORK_IS_UNSTABLE),
                    isShowTestDateBottomSheet = false,
                    examSchedulesErrorMessage = NETWORK_IS_UNSTABLE,
                )
            }

            is ApiResult.UnknownError -> updateState {
                copy(
                    schedulesState = SchedulesLoadState.Failure(UNKNOWN_ERROR),
                    isShowTestDateBottomSheet = false,
                    examSchedulesErrorMessage = UNKNOWN_ERROR,
                )
            }

            is ApiResult.Success -> {
                updateState {
                    copy(
                        schedulesState = SchedulesLoadState.Success(result.data.toImmutableList())
                    )
                }
            }
        }
    }

    private fun handleError(result: ApiResult<*>) {
        Log.d(
            "HandlingError",
            "Error :: ${result}"
        )
    }

    private fun UserExam?.toUiState(): UserExamUiState {
        if (this == null) return UserExamUiState.NoSchedule
        if (this.ddayType == DdayType.AFTER) return UserExamUiState.NoSchedule

        return UserExamUiState.Scheduled(
            ddayType = this.ddayType,
            examDate = this.examDate,
            examName = this.examName,
            examPeriod = this.period,
            dday = this.dday,
        )
    }
}
