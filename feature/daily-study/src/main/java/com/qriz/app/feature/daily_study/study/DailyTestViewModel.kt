package com.qriz.app.feature.daily_study.study

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.qriz.app.core.data.daily_study.daily_study_api.repository.DailyStudyRepository
import com.qriz.app.core.data.test.test_api.model.Option
import com.qriz.app.core.data.test.test_api.model.Test
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.navigation.route.DailyStudyRoute
import com.qriz.app.core.ui.common.resource.NETWORK_IS_UNSTABLE
import com.qriz.app.core.ui.common.resource.UNKNOWN_ERROR
import com.qriz.app.core.ui.test.mapper.toOption
import com.qriz.app.core.ui.test.mapper.toQuestionTestItem
import com.qriz.app.core.ui.test.model.OptionItem
import com.qriz.app.core.ui.test.model.QuestionTestItem
import com.qriz.app.feature.base.BaseViewModel
import com.qriz.app.feature.daily_study.study.DailyTestUiState.LoadState.Failure
import com.qriz.app.feature.daily_study.study.DailyTestUiState.LoadState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailyTestViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val dailyStudyRepository: DailyStudyRepository,
) : BaseViewModel<DailyTestUiState, DailyStudyUiEffect, DailyTestUiAction>(
    DailyTestUiState.DEFAULT
) {
    private val dayNumber: Int = savedStateHandle.toRoute<DailyStudyRoute.DailyTest>().dayNumber

    private var timerJob: Job? = null

    private val selectOptions = MutableStateFlow<Map<Long, SelectOptionData>>(emptyMap())

    override fun process(action: DailyTestUiAction): Job = viewModelScope.launch {
        when (action) {
            is DailyTestUiAction.LoadData -> observeTestItems()
            is DailyTestUiAction.NextQuestion -> nextQuestion()
            is DailyTestUiAction.SelectOption -> selectOption(
                questionId = action.questionId,
                option = action.option
            )

            is DailyTestUiAction.ShowSubmitConfirmationDialog -> updateState { copy(showSubmitConfirmationDialog = true) }
            is DailyTestUiAction.DismissSubmitConfirmationDialog -> updateState { copy(showSubmitConfirmationDialog = false) }
            is DailyTestUiAction.SubmitTest -> submitTest()
            is DailyTestUiAction.CancelTest -> sendEffect(DailyStudyUiEffect.Cancel)
            is DailyTestUiAction.ShowCancelConfirmationDialog -> updateState { copy(showCancelConfirmationDialog = true) }
            is DailyTestUiAction.DismissCancelConfirmationDialog -> updateState { copy(showCancelConfirmationDialog = false) }
            is DailyTestUiAction.DismissErrorDialog -> updateState { copy(errorMessage = null) }
        }
    }

    private fun observeTestItems() = viewModelScope.launch {
        val test = getDailyStudy() ?: return@launch
        startTimer(test.questions.first().timeLimit)
        selectOptions.collect { selectOptions ->
            updateState {
                copy(
                    state = Success(
                        questions = test.questions
                            .toQuestionTestItem(isSelectedOptionMap = selectOptions.mapValues { it.value.option })
                            .toImmutableList()
                    ),
                    errorMessage = null,
                )
            }
        }
    }

    private suspend fun getDailyStudy(): Test? {
        return when (val result = dailyStudyRepository.getDailyStudy(dayNumber = dayNumber)) {
            is ApiResult.Success -> result.data

            is ApiResult.Failure -> {
                updateState {
                    copy(
                        state = Failure(result.message)
                    )
                }
                null
            }

            is ApiResult.NetworkError -> {
                updateState {
                    copy(
                        state = Failure(NETWORK_IS_UNSTABLE),
                        errorMessage = null,
                    )
                }
                null
            }

            is ApiResult.UnknownError -> {
                updateState {
                    copy(
                        state = Failure(UNKNOWN_ERROR),
                        errorMessage = null,
                    )
                }
                null
            }
        }
    }

    private fun selectOption(questionId: Long, option: OptionItem) {
        selectOptions.update {
            val question = requireQuestions.first { question -> question.id == questionId }
            val timeSpent = question.timeLimit - uiState.value.remainTimeMs.toSecond()
            it + (questionId to SelectOptionData(
                option = option.toOption(),
                timeSpent = timeSpent,
            ))
        }
    }

    private fun startTimer(timeLimit: Int) {
        timerJob = viewModelScope.launch {
            var currentRemainingTime = timeLimit.toMilliSecond()
            val interval = 1000L
            while (isActive) {
                val currentIndex = uiState.value.currentIndex
                if (currentRemainingTime <= 0) {
                    setForcedAnswer(timeLimit)
                    cancelTimer()
                    if (currentIndex < requireQuestions.size - 1) {
                        process(DailyTestUiAction.NextQuestion)
                    } else {
                        process(DailyTestUiAction.ShowSubmitConfirmationDialog) //마지막 문제 타이머 종료 시
                    }
                    return@launch
                }

                delay(interval)
                currentRemainingTime -= interval
                updateState { copy(remainTimeMs = currentRemainingTime) }
            }
        }
    }

    private fun cancelTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    private fun nextQuestion() {
        val currentIndex = uiState.value.currentIndex
        val question = requireQuestions[currentIndex + 1]
        updateState {
            copy(
                currentIndex = currentIndex + 1,
                remainTimeMs = question.timeLimit.toMilliSecond(),
            )
        }
        cancelTimer()
        startTimer(question.timeLimit)
    }

    private suspend fun submitTest() {
        val questions = requireQuestions
        if (selectOptions.value.size != questions.size) {
            return updateState {
                copy(
                    errorMessage = "모든 문항에 답변해주세요."
                )
            }
        }

        val activities = selectOptions.value.map {
            Triple(
                it.key,
                it.value.option,
                it.value.timeSpent
            )
        }

        when (val result = dailyStudyRepository.submitTest(
            dayNumber,
            activities
        )) {
            is ApiResult.Success -> {
                updateState { copy(showSubmitConfirmationDialog = false) }
                sendEffect(DailyStudyUiEffect.MoveToResult)
            }
            is ApiResult.Failure -> updateState {
                copy(
                    errorMessage = result.message,
                    showSubmitConfirmationDialog = false,
                )
            }

            is ApiResult.NetworkError -> updateState {
                copy(
                    errorMessage = NETWORK_IS_UNSTABLE,
                    showSubmitConfirmationDialog = false,
                )
            }

            is ApiResult.UnknownError -> updateState {
                copy(
                    errorMessage = UNKNOWN_ERROR,
                    showSubmitConfirmationDialog = false,
                )
            }
        }
    }

    private fun setForcedAnswer(timeLimit: Int) {
        val currentIndex = uiState.value.currentIndex
        val question = requireQuestions[currentIndex]

        selectOptions.update {
            it + (question.id to SelectOptionData(
                option = Option(
                    id = 0,
                    content = ""
                ),
                timeSpent = timeLimit
            ))
        }
    }

    private data class SelectOptionData(
        val option: Option,
        val timeSpent: Int,
    )

    private fun Int.toMilliSecond() = this.times(1000).toLong()
    private fun Long.toSecond() = this.div(1000).toInt()

    //state가 Success인 확실한 상태에서만 사용
    private val requireQuestions: List<QuestionTestItem>
        get() = (uiState.value.state as? Success)?.questions
            ?: throw IllegalStateException("올바른 상태가 아닙니다.")

    @VisibleForTesting
    fun isTimerJobNotNull() = timerJob != null

    @VisibleForTesting
    fun isTimerJobNull() = timerJob == null
}
