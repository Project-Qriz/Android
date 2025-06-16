package com.qriz.app.feature.home

import androidx.lifecycle.viewModelScope
import com.qriz.app.core.data.application.application_api.model.DdayType
import com.qriz.app.core.data.application.application_api.model.Schedule
import com.qriz.app.core.data.application.application_api.model.UserExam
import com.qriz.app.core.data.application.application_api.repository.ExamRepository
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.ui.common.resource.NETWORK_IS_UNSTABLE
import com.qriz.app.core.ui.common.resource.UNKNOWN_ERROR
import com.qriz.app.feature.base.BaseViewModel
import com.qriz.app.feature.home.HomeUiState.SchedulesLoadState
import com.qriz.app.feature.home.component.UserExamUiState
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
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

    private val lastTryApplyExamIdState = MutableStateFlow(0L)
    private val retryApplyExam = MutableStateFlow(true)

    private val applyExamFlow =
        retryApplyExam
            .filter { it }
            .flatMapLatest { lastTryApplyExamIdState }
            .filter { it > 0 }
            .onEach { examId ->
                applyExam(examId)
                retryApplyExam.value = false
            }

    override fun process(action: HomeUiAction): Job = viewModelScope.launch {
        when (action) {
            is HomeUiAction.ObserveClient -> observeClient()
            is HomeUiAction.ChangeTodayStudyCard -> onChangeTodayStudyCard(action.day)
            is HomeUiAction.ClickApply -> showExamSchedules()
            is HomeUiAction.DismissTestDateBottomSheet -> onDismissTestDateBottomSheet()
            is HomeUiAction.MoveToPreviewTest -> sendEffect(HomeUiEffect.MoveToPreviewTest)
            is HomeUiAction.LoadToExamSchedules -> loadToExamSchedules()
            is HomeUiAction.DismissExamSchedulesErrorDialog -> updateState { copy(examSchedulesErrorMessage = null) }
            is HomeUiAction.OnClickExamSchedule -> onClickExamSchedule(examId = action.examId)
            is HomeUiAction.DismissApplyExamErrorDialog -> updateState { copy(applyExamErrorMessage = null) }
            is HomeUiAction.RetryApplyExam -> retryApplyExam.update { true }
        }
    }

    private fun onChangeTodayStudyCard(day: Int) {
        updateState { copy(currentTodayStudyDay = day) }
    }

    private fun observeClient() {
        viewModelScope.launch { dataFlow.collect { updateState { it } } }
        applyExamFlow.launchIn(viewModelScope)
    }

    private fun showExamSchedules() {
        loadToExamSchedules()
    }

    private fun onDismissTestDateBottomSheet() {
        updateState { copy(isShowExamScheduleBottomSheet = false) }
    }

    private fun onClickExamSchedule(examId: Long) {
        lastTryApplyExamIdState.update { examId }
    }

    private fun applyExam(examId: Long) = viewModelScope.launch {
        val uaid = uiState.value.userApplicationId
        val result = if (uaid != null && uaid > 0) {
            examRepository.editExam(
                uaid,
                examId
            )
        } else {
            examRepository.applyExam(examId)
        }

        when (result) {
            is ApiResult.Failure ->
                sendEffect(HomeUiEffect.ShowSnackBar(message = result.message))

            is ApiResult.NetworkError ->
                sendEffect(HomeUiEffect.ShowSnackBar(message = NETWORK_IS_UNSTABLE))

            is ApiResult.Success -> {
                updateState { copy(isShowExamScheduleBottomSheet = false) }
                sendEffect(HomeUiEffect.ShowSnackBar(defaultResId = R.string.success_to_apply_exam))
            }

            is ApiResult.UnknownError -> {
                sendEffect(HomeUiEffect.ShowSnackBar(message = UNKNOWN_ERROR))
            }
        }
    }

    private fun loadToExamSchedules() = viewModelScope.launch {
        updateState {
            copy(
                schedulesState = SchedulesLoadState.Loading,
                isShowExamScheduleBottomSheet = true,
                examSchedulesErrorMessage = null,
            )
        }

        when (val result = examRepository.getExamSchedules()) {
            is ApiResult.Failure -> updateState {
                copy(
                    schedulesState = SchedulesLoadState.Failure(result.message),
                    isShowExamScheduleBottomSheet = false,
                    examSchedulesErrorMessage = result.message
                )
            }

            is ApiResult.NetworkError -> updateState {
                copy(
                    schedulesState = SchedulesLoadState.Failure(NETWORK_IS_UNSTABLE),
                    isShowExamScheduleBottomSheet = false,
                    examSchedulesErrorMessage = NETWORK_IS_UNSTABLE,
                )
            }

            is ApiResult.UnknownError -> updateState {
                copy(
                    schedulesState = SchedulesLoadState.Failure(UNKNOWN_ERROR),
                    isShowExamScheduleBottomSheet = false,
                    examSchedulesErrorMessage = UNKNOWN_ERROR,
                )
            }

            is ApiResult.Success -> {
                updateState {
                    copy(
                        schedulesState = SchedulesLoadState.Success(result.data.toImmutableList()),
                        userApplicationId = result.data.findUaid(),
                    )
                }
            }
        }
    }

    private fun handleError(result: ApiResult<*>) {

    }

    private fun UserExam?.toUiState(): UserExamUiState {
        if (this == null) return UserExamUiState.NoSchedule
        if (this.ddayType == DdayType.AFTER) return UserExamUiState.PastExam

        return UserExamUiState.Scheduled(
            ddayType = this.ddayType,
            examDate = this.examDate,
            examName = this.examName,
            examPeriod = this.period,
            dday = this.dday,
        )
    }

    private fun List<Schedule>.findUaid(): Long? {
        return this.firstOrNull { it.userApplyId != null }?.userApplyId
    }
}
