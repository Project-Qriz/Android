package com.qriz.app.feature.onboard.preview

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.qriz.app.core.data.onboard.onboard_api.repository.OnBoardRepository
import com.qriz.app.core.data.test.test_api.model.Option
import com.qriz.app.core.data.test.test_api.model.Test
import com.qriz.app.core.domain.usecase_api.onboard.SubmitPreviewTestUseCase
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.ui.common.resource.NETWORK_IS_UNSTABLE
import com.qriz.app.core.ui.common.resource.UNKNOWN_ERROR
import com.qriz.app.core.ui.test.mapper.toOption
import com.qriz.app.core.ui.test.mapper.toQuestionTestItem
import com.qriz.app.core.ui.test.model.OptionItem
import com.qriz.app.feature.base.BaseViewModel
import com.qriz.app.feature.onboard.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.qriz.app.feature.onboard.preview.PreviewUiState.LoadStatus.*
import com.qriz.app.core.designsystem.R as DSR

@HiltViewModel
open class PreviewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val onBoardRepository: OnBoardRepository,
    private val submitTestUseCase: SubmitPreviewTestUseCase,
) : BaseViewModel<PreviewUiState, PreviewUiEffect, PreviewUiAction>(PreviewUiState.Default) {
    private val isTest = savedStateHandle.get<Boolean>(IS_TEST_FLAG) ?: false

    private val isSelectedOption = MutableStateFlow<Map<Long, Option>>(emptyMap())

    @VisibleForTesting
    var timerJob: Job? = null

    init {
        if (isTest.not()) process(PreviewUiAction.ObservePreviewTestItem)
    }

    final override fun process(action: PreviewUiAction): Job = viewModelScope.launch {
        when (action) {
            is PreviewUiAction.ObservePreviewTestItem -> observePreviewTestItems()
            is PreviewUiAction.SelectOption -> onSelectOption(
                questionID = action.questionID,
                option = action.option
            )

            is PreviewUiAction.ClickNextPage -> onClickNextPage()
            is PreviewUiAction.ClickPreviousPage -> onClickPreviousPage()
            is PreviewUiAction.ClickSubmit -> onClickSubmit()
            is PreviewUiAction.ClickCancel -> onClickCancel()
            is PreviewUiAction.ClickConfirmTestSubmitWarningDialog -> onClickConfirmTestSubmitWarningDialog()
            is PreviewUiAction.ClickDismissTestSubmitWarningDialog -> onClickDismissTestSubmitWarningDialog()
            is PreviewUiAction.ClickConfirmTestEndWarningDialog -> onClickConfirmTestEndWarningDialog()
            is PreviewUiAction.ClickDismissTestEndWarningDialog -> onClickDismissTestEndWarningDialog()
        }
    }

    private fun onSelectOption(questionID: Long, option: OptionItem) {
        isSelectedOption.update { isSelectedOption.value + (questionID to option.toOption()) }
    }

    private fun onClickNextPage() {
        updateState { copy(currentIndex = uiState.value.currentIndex + 1) }
    }

    private fun onClickPreviousPage() {
        if (uiState.value.currentIndex == 0) {
            onClickCancel()
            return
        }
        updateState { copy(currentIndex = uiState.value.currentIndex - 1) }
    }

    private fun onClickCancel() {
        updateState { copy(isVisibleTestEndWarningDialog = true) }
    }

    private fun onClickConfirmTestSubmitWarningDialog() {
        updateState { copy(isVisibleTestSubmitWarningDialog = false) }
        sendEffect(PreviewUiEffect.MoveToHome)
    }

    private fun onClickDismissTestSubmitWarningDialog() {
        updateState { copy(isVisibleTestSubmitWarningDialog = false) }
    }

    private fun onClickConfirmTestEndWarningDialog() {
        updateState { copy(isVisibleTestEndWarningDialog = false) }
        sendEffect(PreviewUiEffect.MoveToHome)
    }

    private fun onClickDismissTestEndWarningDialog() {
        updateState { copy(isVisibleTestEndWarningDialog = false) }
    }

    private fun onClickSubmit() {
        submitTest(isSelectedOption.value)
    }

    private fun observePreviewTestItems() = viewModelScope.launch {
        val questions = getPreviewTest()?.questions ?: return@launch
        startTimer()
        isSelectedOption.collect { isSelectedOptionsMap ->
            updateState {
                copy(
                    questions = questions
                        .toQuestionTestItem(isSelectedOptionsMap)
                        .toImmutableList()
                )
            }
        }
    }

    private suspend fun getPreviewTest(): Test? {
        return when(val result = onBoardRepository.getPreviewTest()) {
            is ApiResult.Success -> {
                val test = result.data
                updateState {
                    copy(
                        questions = test.questions
                            .toQuestionTestItem(isSelectedOption.value)
                            .toImmutableList(),
                        remainTimeMs = test.totalTimeLimit.toMilliSecond(),
                        totalTimeLimitMs = test.totalTimeLimit.toMilliSecond(),
                        loadStatus = Success,
                    )
                }
                test
            }

            is ApiResult.Failure -> {
                updateState {
                    copy(loadStatus = Failure)
                }
                null
            }

            is ApiResult.NetworkError,
            is ApiResult.UnknownError -> {
                updateState {
                    copy(loadStatus = Failure)
                }
                null
            }
        }
    }

    private fun submitTest(selectedOptions: Map<Long, Option>) = viewModelScope.launch {
        if (uiState.value.loadStatus == PreviewUiState.LoadStatus.Loading) return@launch
        val isExistUnresolvedProblem = selectedOptions.size < uiState.value.questions.size
        if (isExistUnresolvedProblem) {
            sendEffect(
                PreviewUiEffect.ShowSnackBar(
                    defaultResId = R.string.failed_submit_test_problems_remain,
                )
            )
            return@launch
        }
        updateState { copy(loadStatus = Loading) }
        when(val result = submitTestUseCase(selectedOptions)) {
            is ApiResult.Success -> {
                sendEffect(PreviewUiEffect.MoveToResult)
            }

            is ApiResult.Failure -> {
                updateState { copy(loadStatus = Success) }
                sendEffect(
                    PreviewUiEffect.ShowSnackBar(
                        defaultResId = R.string.failed_submit_test,
                        message = result.message
                    )
                )
            }

            is ApiResult.NetworkError -> {
                updateState { copy(loadStatus = Success) }
                PreviewUiEffect.ShowSnackBar(
                    defaultResId = DSR.string.empty,
                    message = NETWORK_IS_UNSTABLE,
                )
            }

            is ApiResult.UnknownError -> {
                updateState { copy(loadStatus = Success) }
                PreviewUiEffect.ShowSnackBar(
                    defaultResId = DSR.string.empty,
                    message = UNKNOWN_ERROR,
                )
            }
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var currentTime = uiState.value.remainTimeMs
            val interval = 1000L
            while (isActive) {
                if (currentTime <= 0L) {
                    submitTest(getForcedAnswer())
                    break
                }

                delay(interval)
                currentTime -= interval
                updateState { copy(remainTimeMs = currentTime) }
            }
        }
    }

    private fun getForcedAnswer() =
        with(uiState.value) {
            questions.associate { it.id to Option(id = 0,"") } + isSelectedOption.value
        }


    companion object {
        internal const val IS_TEST_FLAG = "IS_TEST_FLAG"
        fun Int.toMilliSecond() = this.times(1000).toLong()
    }
}
