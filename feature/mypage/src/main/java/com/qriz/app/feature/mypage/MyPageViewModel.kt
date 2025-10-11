package com.qriz.app.feature.mypage

import androidx.lifecycle.viewModelScope
import com.qriz.app.core.data.application.application_api.model.Schedule
import com.qriz.app.core.data.application.application_api.repository.ExamRepository
import com.qriz.app.core.data.daily_study.daily_study_api.repository.DailyStudyRepository
import com.qriz.app.core.designsystem.R
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.model.requireValue
import com.qriz.app.core.ui.common.const.ExamScheduleState
import com.qriz.app.core.ui.common.resource.NETWORK_IS_UNSTABLE
import com.qriz.app.core.ui.common.resource.UNKNOWN_ERROR
import com.qriz.app.feature.base.BaseViewModel
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val examRepository: ExamRepository,
    private val dailyStudyRepository: DailyStudyRepository,
) : BaseViewModel<MyPageUiState, MyPageUiEffect, MyPageUiAction>(
    MyPageUiState.Default
) {
    private var userApplicationId: Long? = null

    override fun process(action: MyPageUiAction): Job = viewModelScope.launch {
        when (action) {
            is MyPageUiAction.LoadData -> loadData()
            is MyPageUiAction.DismissExamBottomSheet -> updateState { copy(showExamBottomSheet = false) }
            is MyPageUiAction.DismissResetPlanDialog -> updateState { copy(showResetPlanDialog = false) }
            is MyPageUiAction.ShowExamBottomSheet -> updateState { copy(showExamBottomSheet = true) }
            is MyPageUiAction.ShowResetPlanDialog -> updateState { copy(showResetPlanDialog = true) }
            is MyPageUiAction.ResetPlan -> initializePlan()
            is MyPageUiAction.ClickExamSchedule -> applyExam(action.examId)
            is MyPageUiAction.ClickProfile -> sendEffect(MyPageUiEffect.NavigateSetting)
        }
    }

    private fun applyExam(examId: Long) = viewModelScope.launch {
        val result = if (userApplicationId != null && userApplicationId!! > 0) {
            examRepository.editExam(
                userApplicationId!!,
                examId
            )
        } else {
            examRepository.applyExam(examId)
        }

        fun sendShowSnackbarEffect(message: String) {
            sendEffect(
                MyPageUiEffect.ShowSnackBar(
                    defaultResId = R.string.empty,
                    message = message
                )
            )
        }

        updateState { copy(showExamBottomSheet = false) }

        when (result) {
            is ApiResult.Failure -> sendShowSnackbarEffect(result.message)

            is ApiResult.NetworkError -> sendShowSnackbarEffect(NETWORK_IS_UNSTABLE)

            is ApiResult.Success -> {
                sendShowSnackbarEffect(EXAM_APPLICATION_SUCCESS)
            }

            is ApiResult.UnknownError -> {
                sendShowSnackbarEffect(UNKNOWN_ERROR)
            }
        }
    }

    private suspend fun initializePlan() {
        val result = dailyStudyRepository.resetDailyStudyPlan()
        updateState { copy(showResetPlanDialog = false) }
        when (result) {
            is ApiResult.Success<*> -> sendEffect(
                MyPageUiEffect.ShowSnackBar(
                    defaultResId = R.string.empty,
                    message = RESET_PLAN_SUCCESS
                )
            )

            is ApiResult.Failure, is ApiResult.NetworkError, is ApiResult.UnknownError -> sendEffect(
                MyPageUiEffect.ShowSnackBar(
                    defaultResId = R.string.empty,
                    message = RESET_PLAN_FAILURE
                )
            )
        }
    }

    private suspend fun loadData() {
        val user = userRepository.getUser().requireValue
        updateState { copy(user = user) }

        when (val result = examRepository.getExamSchedules()) {
            is ApiResult.Success<List<Schedule>> -> {
                updateState {
                    userApplicationId = result.data.firstNotNullOfOrNull { it.userApplyId }
                    copy(
                        examScheduleState = ExamScheduleState.Success(
                            result.data.toImmutableList()
                        )
                    )
                }
            }

            is ApiResult.Failure -> {
                updateState {
                    copy(
                        examScheduleState = ExamScheduleState.Error(
                            message = result.message
                        )
                    )
                }
            }

            is ApiResult.NetworkError -> {
                updateState {
                    copy(
                        examScheduleState = ExamScheduleState.Error(
                            message = NETWORK_IS_UNSTABLE
                        )
                    )
                }
            }

            is ApiResult.UnknownError -> {
                updateState {
                    copy(
                        examScheduleState = ExamScheduleState.Error(
                            message = UNKNOWN_ERROR
                        )
                    )
                }
            }
        }
    }

    companion object {
        private const val EXAM_APPLICATION_SUCCESS = "시험 일정을 등록했어요!"
        private const val RESET_PLAN_SUCCESS = "오늘의 공부 플랜이 초기화 되었습니다."
        private const val RESET_PLAN_FAILURE = "오늘의 공부 플랜 초기화에 실패했습니다."
    }
}
