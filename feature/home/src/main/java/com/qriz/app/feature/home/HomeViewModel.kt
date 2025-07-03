package com.qriz.app.feature.home

import androidx.lifecycle.viewModelScope
import com.qriz.app.core.data.application.application_api.model.DdayType
import com.qriz.app.core.data.application.application_api.model.Schedule
import com.qriz.app.core.data.application.application_api.model.UserExam
import com.qriz.app.core.data.application.application_api.repository.ExamRepository
import com.qriz.app.core.data.daily_study.daily_study_api.model.DailyStudyPlan
import com.qriz.app.core.data.daily_study.daily_study_api.model.ImportanceLevel
import com.qriz.app.core.data.daily_study.daily_study_api.model.PlannedSkill
import com.qriz.app.core.data.daily_study.daily_study_api.model.WeeklyRecommendation
import com.qriz.app.core.data.daily_study.daily_study_api.repository.DailyStudyRepository
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.model.requireValue
import com.qriz.app.core.ui.common.resource.NETWORK_IS_UNSTABLE
import com.qriz.app.core.ui.common.resource.UNKNOWN_ERROR
import com.qriz.app.feature.base.BaseViewModel
import com.qriz.app.feature.home.HomeUiState.SchedulesLoadState
import com.qriz.app.feature.home.component.UserExamUiState
import com.quiz.app.core.data.user.user_api.model.PreviewTestStatus
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    userRepository: UserRepository,
    private val examRepository: ExamRepository,
    private val dailyStudyRepository: DailyStudyRepository,
) : BaseViewModel<HomeUiState, HomeUiEffect, HomeUiAction>(HomeUiState.Default) {

    private var isInitialized = false
    private val homeDataLoad = MutableStateFlow(true)

    private val dataFlow = homeDataLoad.filter { it }.flatMapLatest { userRepository.getUserFlow() }
        .flatMapLatest { user ->
            val dailyStudyPlanFlow =
                if (user.previewTestStatus == PreviewTestStatus.PREVIEW_COMPLETED) dailyStudyRepository.getDailyStudyPlanFlow()
                else flowOf(fakeDailyStudyPlanResult)
            val weeklyRecommendationFlow =
                if (user.previewTestStatus == PreviewTestStatus.PREVIEW_COMPLETED) dailyStudyRepository.getWeeklyRecommendation()
                else flowOf(fakeWeeklyRecommendationResult)

            combine(
                dailyStudyPlanFlow,
                weeklyRecommendationFlow,
                examRepository.getUserExams(),
            ) { dailyStudyPlan, weeklyRecommendation, exam ->
                val isAllSuccess = handleError(
                    dailyStudyPlan,
                    weeklyRecommendation,
                    exam
                )

                val dailyStudyPlans = dailyStudyPlan.requireValue.toImmutableList()

                initializePlanDate(dailyStudyPlans)

                if (isAllSuccess) {
                    uiState.value.copy(
                        user = user,
                        userExamState = exam.requireValue.toUiState(),
                        dataLoadState = HomeUiState.HomeDataLoadState.Success,
                        dailyStudyPlans = dailyStudyPlans,
                        weeklyRecommendation = weeklyRecommendation.requireValue.toImmutableList(),
                    )
                } else {
                    uiState.value
                }
            }
        }

    private val lastTryApplyExamIdState = MutableStateFlow(0L)
    private val retryApplyExam = MutableStateFlow(true)

    private val applyExamFlow =
        retryApplyExam.filter { it }.flatMapLatest { lastTryApplyExamIdState }.filter { it > 0 }
            .onEach { examId ->
                applyExam(examId)
                retryApplyExam.value = false
            }

    override fun process(action: HomeUiAction): Job = viewModelScope.launch {
        when (action) {
            is HomeUiAction.ObserveClient -> observeClient()
            is HomeUiAction.ChangeStudyPlanDate -> onChangeTodayStudyCard(action.day)
            is HomeUiAction.ClickApply -> showExamSchedules()
            is HomeUiAction.DismissTestDateBottomSheet -> onDismissTestDateBottomSheet()
            is HomeUiAction.MoveToPreviewTest -> sendEffect(HomeUiEffect.MoveToPreviewTest)
            is HomeUiAction.LoadToExamSchedules -> loadToExamSchedules()
            is HomeUiAction.DismissExamSchedulesErrorDialog -> updateState { copy(examSchedulesErrorMessage = null) }
            is HomeUiAction.ClickExamSchedule -> onClickExamSchedule(examId = action.examId)
            is HomeUiAction.DismissApplyExamErrorDialog -> updateState { copy(applyExamErrorMessage = null) }
            is HomeUiAction.RetryApplyExam -> retryApplyExam.update { true }
            is HomeUiAction.ClickRetryDataLoad -> homeDataLoad.update { true }
            is HomeUiAction.DismissPlanDayFilterBottomSheet -> { updateState { copy(showPlanDayFilterBottomSheet = false) } }
            is HomeUiAction.ShowPlanDayFilterBottomSheet -> { updateState { copy(showPlanDayFilterBottomSheet = true) } }
            is HomeUiAction.ChangeStudyPlanDateToToday -> onChangeStudyPlanDayToToday()
            is HomeUiAction.ShowResetPlanConfirmationDialog -> updateState { copy(showResetPlanConfirmationDialog = true) }
            is HomeUiAction.DismissResetPlanConfirmationDialog -> updateState { copy(showResetPlanConfirmationDialog = false) }
            is HomeUiAction.ResetDailyStudyPlans -> resetDailyStudyPlan()
            is HomeUiAction.DismissResetPlanErrorDialog -> { updateState { copy(resetPlanErrorMessage = null) } }
            is HomeUiAction.ClickMoveToDailyStudy -> moveToDailyStudy()
        }
    }

    private fun moveToDailyStudy() {
        with(uiState.value) {
            sendEffect(
                HomeUiEffect.MoveToDailyStudy(
                    dayNumber = selectedPlanDay,
                    isReview = dailyStudyPlans[selectedPlanDay - 1].reviewDay,
                    isComprehensiveReview = dailyStudyPlans[selectedPlanDay - 1].comprehensiveReviewDay,
                ),
            )
        }
    }

    private suspend fun onChangeTodayStudyCard(day: Int) {
        updateState { copy(selectedPlanDay = day) }

        //DayFilter를 통해 선택한 경우
        if (uiState.value.showPlanDayFilterBottomSheet) {
            delay(300)
            updateState { copy(showPlanDayFilterBottomSheet = false) }
        }
    }

    private suspend fun onChangeStudyPlanDayToToday() {
        val today = LocalDate.now()
        val plans = uiState.value.dailyStudyPlans
        updateState {
            copy(selectedPlanDay = plans.indexOfFirst { it.planDate == today } + 1)
        }
        delay(300)
        updateState { copy(showPlanDayFilterBottomSheet = false) }
    }

    private fun observeClient() {
        viewModelScope.launch {
            dataFlow.collect {
                updateState { it }
                homeDataLoad.update { false }
            }
        }
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
            is ApiResult.Failure -> sendEffect(HomeUiEffect.ShowSnackBar(message = result.message))

            is ApiResult.NetworkError -> sendEffect(HomeUiEffect.ShowSnackBar(message = NETWORK_IS_UNSTABLE))

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

    private fun handleError(vararg result: ApiResult<*>): Boolean {
        fun updateDataLoadState(message: String) {
            updateState { copy(dataLoadState = HomeUiState.HomeDataLoadState.Failure(message)) }
        }

        for (res in result) {
            when (res) {
                is ApiResult.Failure -> {
                    updateDataLoadState(res.message)
                    return false
                }

                is ApiResult.NetworkError -> {
                    updateDataLoadState(NETWORK_IS_UNSTABLE)
                    return false
                }

                is ApiResult.UnknownError -> {
                    updateDataLoadState(UNKNOWN_ERROR)
                    return false
                }

                is ApiResult.Success -> continue
            }
        }

        return true
    }

    private fun initializePlanDate(dailyStudyPlans: ImmutableList<DailyStudyPlan>) {
        if (isInitialized || dailyStudyPlans.isEmpty()) return

        val todayPlanIndex = dailyStudyPlans.indexOfFirst { it.planDate == LocalDate.now() }
        val todayPlanIsReview =
            if (todayPlanIndex >= 0) dailyStudyPlans[todayPlanIndex].reviewDay else false

        updateState {
            copy(
                selectedPlanDay = todayPlanIndex + 1,
                todayPlanIsReview = todayPlanIsReview,
            )
        }
    }

    private suspend fun resetDailyStudyPlan() {
        when (val result = dailyStudyRepository.resetDailyStudyPlan()) {
            is ApiResult.Success -> {
                sendEffect(HomeUiEffect.ShowSnackBar(R.string.plan_is_reset))
                homeDataLoad.update { true }
            }

            is ApiResult.Failure -> {
                updateState { copy(resetPlanErrorMessage = result.message) }
            }

            is ApiResult.NetworkError -> {
                updateState { copy(resetPlanErrorMessage = NETWORK_IS_UNSTABLE) }
            }

            is ApiResult.UnknownError -> {
                updateState { copy(resetPlanErrorMessage = UNKNOWN_ERROR) }
            }
        }
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

    companion object {
        private val fakeDailyStudyPlanResult = ApiResult.Success(
            listOf(
                DailyStudyPlan(
                    id = 0,
                    completed = false,
                    planDate = LocalDate.now(),
                    completionDate = null,
                    plannedSkills = listOf(
                        PlannedSkill(
                            id = 0,
                            type = "SQL 기본",
                            keyConcept = "WHERE 절",
                            description = ""
                        ),
                        PlannedSkill(
                            id = 0,
                            type = "SQL 기본",
                            keyConcept = "WHERE 절",
                            description = ""
                        )
                    ),
                    reviewDay = false,
                    comprehensiveReviewDay = false
                )
            ),
        )

        private val fakeWeeklyRecommendationResult = ApiResult.Success(
            listOf(
                WeeklyRecommendation(
                    skillId = 1,
                    keyConcepts = "데이터 모델의 이해",
                    description = "",
                    frequency = 1,
                    incorrectRate = null,
                    importanceLevel = ImportanceLevel.HIGH,
                ),
                WeeklyRecommendation(
                    skillId = 1,
                    keyConcepts = "SELECT 문",
                    description = "",
                    frequency = 1,
                    incorrectRate = null,
                    importanceLevel = ImportanceLevel.LOW,
                )
            )
        )
    }
}
