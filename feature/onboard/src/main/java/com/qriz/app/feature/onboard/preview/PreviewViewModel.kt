package com.qriz.app.feature.onboard.preview

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.qriz.app.core.data.onboard.onboard_api.repository.OnBoardRepository
import com.qriz.app.core.data.test.test_api.model.Option
import com.qriz.app.feature.base.BaseViewModel
import com.qriz.app.feature.onboard.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class PreviewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val onBoardRepository: OnBoardRepository
) : BaseViewModel<PreviewUiState, PreviewUiEffect, PreviewUiAction>(PreviewUiState.Default) {
    private val isTest = savedStateHandle.get<Boolean>(IS_TEST_FLAG) ?: false

    @VisibleForTesting
    var timerJob: Job? = null

    init {
        if (isTest.not()) process(PreviewUiAction.LoadPreviewTest)
    }

    final override fun process(action: PreviewUiAction): Job = viewModelScope.launch {
        when (action) {
            PreviewUiAction.LoadPreviewTest -> getPreviewTest()
            is PreviewUiAction.SelectOption -> onSelectOption(
                questionID = action.questionID,
                option = action.option
            )

            PreviewUiAction.ClickNextPage -> onClickNextPage()
            PreviewUiAction.ClickPreviousPage -> onClickPreviousPage()
            PreviewUiAction.ClickSubmit -> onClickSubmit()
            PreviewUiAction.ClickCancel -> onClickCancel()
        }
    }

    private fun onSelectOption(questionID: Long, option: Option) {
        updateState {
            copy(
                selectedOptions =
                (uiState.value.selectedOptions + (questionID to option)).toImmutableMap()
            )
        }
    }

    private fun onClickNextPage() {
        updateState { copy(currentIndex = uiState.value.currentIndex + 1) }
    }

    private fun onClickPreviousPage() {
        updateState { copy(currentIndex = uiState.value.currentIndex - 1) }
    }

    private fun onClickCancel() {
        sendEffect(PreviewUiEffect.MoveToBack)
    }

    private fun onClickSubmit() {
        submitTest(uiState.value.selectedOptions.toMap())
    }

    private fun getPreviewTest() = viewModelScope.launch {
        if (uiState.value.isLoading) return@launch
        updateState { copy(isLoading = true) }
        runCatching { onBoardRepository.getPreviewTest() }
            .onSuccess { test ->
                updateState {
                    copy(
                        questions = test.questions.toImmutableList(),
                        remainTimeMs = test.totalTimeLimit.toMilliSecond(),
                        totalTimeLimitMs = test.totalTimeLimit.toMilliSecond(),
                    )
                }
                startTimer()
            }
            .onFailure {
                //TODO : 추후 재시도 UI 나오면 실패 상태로 변경(재시도 UI 노출)
                sendEffect(
                    PreviewUiEffect.ShowSnackBar(
                        defaultResId = R.string.failed_get_test,
                        message = it.message
                    )
                )
            }
            .also { updateState { copy(isLoading = false) } }
    }

    private fun submitTest(selectedOptions: Map<Long, Option>) = viewModelScope.launch {
        if (uiState.value.isLoading) return@launch
        val isExistUnresolvedProblem = selectedOptions.size < uiState.value.questions.size
        if (isExistUnresolvedProblem) {
            sendEffect(
                PreviewUiEffect.ShowSnackBar(
                    defaultResId = R.string.failed_submit_test_problems_remain,
                )
            )
            return@launch
        }
        updateState { copy(isLoading = true) }
        runCatching { onBoardRepository.submitPreviewTest(selectedOptions) }
            .onSuccess { sendEffect(PreviewUiEffect.MoveToGuide) }
            .onFailure {
                sendEffect(
                    PreviewUiEffect.ShowSnackBar(
                        defaultResId = R.string.failed_submit_test,
                        message = it.message
                    )
                )
            }
            .also { updateState { copy(isLoading = false) } }
    }

    private fun startTimer() {
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
            questions.associate { it.id to Option("") } + selectedOptions
        }


    companion object {
        internal const val IS_TEST_FLAG = "IS_TEST_FLAG"
        fun Int.toMilliSecond() = this.times(1000).toLong()
    }
}
