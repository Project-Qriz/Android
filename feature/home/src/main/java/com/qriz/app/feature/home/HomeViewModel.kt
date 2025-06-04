package com.qriz.app.feature.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.qriz.app.core.data.application.application_api.model.DdayType
import com.qriz.app.core.data.application.application_api.model.UserExam
import com.qriz.app.core.data.application.application_api.repository.ExamRepository
import com.qriz.app.core.model.ApiResult
import com.qriz.app.feature.base.BaseViewModel
import com.qriz.app.feature.home.component.ExamScheduleUiState
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    userRepository: UserRepository,
    examRepository: ExamRepository,
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
            scheduleState = exam.data.toUiState()
        )
    }

    override fun process(action: HomeUiAction): Job = viewModelScope.launch {
        when (action) {
            is HomeUiAction.ObserveClient -> observeClient()
            is HomeUiAction.ChangeTodayStudyCard -> onChangeTodayStudyCard(action.day)
            is HomeUiAction.ClickTestDateRegister -> onClickTestDateRegister()
            is HomeUiAction.DismissTestDateBottomSheet -> onDismissTestDateBottomSheet()
            is HomeUiAction.MoveToPreviewTest -> sendEffect(HomeUiEffect.MoveToPreviewTest)
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
    }

    private fun onDismissTestDateBottomSheet() {
        updateState { copy(isShowTestDateBottomSheet = false) }
    }

    private fun handleError(result: ApiResult<*>) {
        Log.d("HandlingError", "Error :: ${result}")
    }

    private fun UserExam?.toUiState(): ExamScheduleUiState {
        if (this == null) return ExamScheduleUiState.NoSchedule
        if (this.ddayType == DdayType.AFTER) return ExamScheduleUiState.NoSchedule

        return ExamScheduleUiState.Scheduled(
            ddayType = this.ddayType,
            examDate = this.examDate,
            examName = this.examName,
            examPeriod = this.period,
            dday = this.dday,
        )
    }
}
