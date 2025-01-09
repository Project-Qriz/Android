package com.qriz.app.feature.onboard.preview

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
class PreviewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val onBoardRepository: OnBoardRepository
) : BaseViewModel<PreviewUiState, PreviewUiEffect, PreviewUiAction>(PreviewUiState.Default) {
    private val isTest = savedStateHandle.get<Boolean>(IS_TEST_FLAG) ?: false

    private var timerJob: Job? = null

    init {
        if (isTest.not()) process(PreviewUiAction.LoadPreviewTest)
    }

    override fun process(action: PreviewUiAction): Job = viewModelScope.launch {
        when (action) {
            PreviewUiAction.LoadPreviewTest -> getPreviewTest()
            is PreviewUiAction.SelectOption -> onSelectOption(
                questionID = action.questionID,
                option = action.option
            )

            PreviewUiAction.ClickNextPage -> onClickNextPage()
            PreviewUiAction.ClickPreviousPage -> onClickPreviousPage()
            PreviewUiAction.ClickSubmit -> onClickSubmit()
            PreviewUiAction.ClickCancel -> onClickBack()
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

    private fun onClickBack() {
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
                    PreviewUiEffect.ShowSnackBer(
                        defaultResId = R.string.failed_to_get_test,
                        message = it.message
                    )
                )
            }
            .also { updateState { copy(isLoading = false) } }
    }

    private fun submitTest(answer: Map<Long, Option>) = viewModelScope.launch {
        if (uiState.value.isLoading) return@launch
        updateState { copy(isLoading = true) }
        runCatching { onBoardRepository.submitPreviewTest(answer) }
            .onSuccess { sendEffect(PreviewUiEffect.MoveToGuide) }
            .onFailure {
                sendEffect(
                    PreviewUiEffect.ShowSnackBer(
                        defaultResId = R.string.failed_to_submit_test,
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
                if (currentTime == 0L) break

                delay(interval)
                currentTime -= interval
                updateState { copy(remainTimeMs = currentTime) }
            }
        }
    }

    companion object {
        internal const val IS_TEST_FLAG = "IS_TEST_FLAG"
        private fun Int.toMilliSecond() = this.times(1000).toLong()
    }
}
