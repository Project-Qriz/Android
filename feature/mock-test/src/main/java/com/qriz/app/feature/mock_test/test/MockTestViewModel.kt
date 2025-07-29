package com.qriz.app.feature.mock_test.test

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.qriz.app.core.data.mock_test.mock_test_api.repository.MockTestRepository
import com.qriz.app.core.data.test.test_api.model.Option
import com.qriz.app.core.data.test.test_api.model.Test
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.navigation.route.MockTestRoute
import com.qriz.app.core.ui.common.resource.NETWORK_IS_UNSTABLE
import com.qriz.app.core.ui.common.resource.UNKNOWN_ERROR
import com.qriz.app.core.ui.test.mapper.toOption
import com.qriz.app.core.ui.test.mapper.toQuestionTestItem
import com.qriz.app.core.ui.test.model.OptionItem
import com.qriz.app.feature.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.qriz.app.feature.mock_test.test.MockTestUiState.LoadState.*

@HiltViewModel
open class MockTestViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val mockTestRepository: MockTestRepository,
) : BaseViewModel<MockTestUiState, MockTestUiEffect, MockTestUiAction>(MockTestUiState.Default) {

    private val mockTestId = savedStateHandle.toRoute<MockTestRoute.MockTest>().id

    private val selectedOption = MutableStateFlow<Map<Long, Option>>(emptyMap())

    @VisibleForTesting
    var timerJob: Job? = null

    final override fun process(action: MockTestUiAction): Job = viewModelScope.launch {
        when (action) {
            is MockTestUiAction.ObserveMockTestItem -> observeMockTestItems()
            is MockTestUiAction.SelectOption -> onSelectOption(
                questionID = action.questionID,
                option = action.option
            )

            is MockTestUiAction.ClickNextPage -> onClickNextPage()
            is MockTestUiAction.ClickPreviousPage -> onClickPreviousPage()
            is MockTestUiAction.ClickSubmit -> onClickSubmit()
            is MockTestUiAction.ClickCancel -> onClickCancel()
            is MockTestUiAction.ClickConfirmTestSubmitWarningDialog -> submitTest(getForcedAnswer())
            is MockTestUiAction.ClickDismissTestSubmitWarningDialog -> onClickDismissTestSubmitWarningDialog()
            is MockTestUiAction.ClickConfirmTestEndWarningDialog -> onClickConfirmTestEndWarningDialog()
            is MockTestUiAction.ClickDismissTestEndWarningDialog -> onClickDismissTestEndWarningDialog()
        }
    }

    private fun onSelectOption(questionID: Long, option: OptionItem) {
        selectedOption.update { selectedOptions ->
            selectedOptions + (questionID to option.toOption())
        }
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

    private suspend fun submitTest(selectedOptions: Map<Long, Option>) {
        updateState { copy(isVisibleTestSubmitWarningDialog = false) }
        val isExistUnresolvedProblem = selectedOptions.size < (uiState.value.state as Success).questions.size
        if (isExistUnresolvedProblem) {
            sendEffect(
                MockTestUiEffect.ShowSnackBar(
                    defaultResId = 0,
                    message = "해결되지 않은 문제가 있습니다"
                )
            )
            return
        }

        when(val result = mockTestRepository.submitMockTest(id = mockTestId, activities = selectedOptions)) {
            is ApiResult.Success<Unit> -> {
                sendEffect(MockTestUiEffect.MoveToResult)
            }

            is ApiResult.Failure -> {
                updateState { copy(submitErrorMessage = result.message) }
            }

            is ApiResult.NetworkError -> {
                updateState { copy(submitErrorMessage = NETWORK_IS_UNSTABLE) }
            }

            is ApiResult.UnknownError -> {
                updateState { copy(submitErrorMessage = UNKNOWN_ERROR) }
            }
        }
    }

    private fun onClickDismissTestSubmitWarningDialog() {
        updateState { copy(isVisibleTestSubmitWarningDialog = false) }
    }

    private fun onClickConfirmTestEndWarningDialog() {
        updateState { copy(isVisibleTestEndWarningDialog = false) }
        sendEffect(MockTestUiEffect.CancelTest)
    }

    private fun onClickDismissTestEndWarningDialog() {
        updateState { copy(isVisibleTestEndWarningDialog = false) }
    }

    private fun onClickSubmit() {
        updateState { copy(isVisibleTestSubmitWarningDialog = true) }
    }

    private fun observeMockTestItems() = viewModelScope.launch {
        val test = getMockTest(mockTestId) ?: return@launch
        val questions = test.questions
        startTimer(test.totalTimeLimit.toMilliSecond())
        selectedOption.collect { isSelectedOptionsMap ->
            updateState {
                copy(
                    state = Success(
                        questions = questions.toQuestionTestItem(isSelectedOptionMap = isSelectedOptionsMap.mapValues { it.value })
                            .toImmutableList()
                    ),
                )
            }
        }
    }

    private suspend fun getMockTest(id: Long): Test? {
        return when (val result = mockTestRepository.getMockTest(id)) {
            is ApiResult.Success -> {
                updateState {
                    copy(
                        totalTimeLimitMs = result.data.totalTimeLimit.toMilliSecond(),
                        remainTimeMs = result.data.totalTimeLimit.toMilliSecond(),
                    )
                }
                result.data
            }

            is ApiResult.Failure -> {
                updateState { copy(state = Failure(result.message)) }
                null
            }

            is ApiResult.NetworkError -> {
                updateState { copy(state = Failure(NETWORK_IS_UNSTABLE)) }
                null
            }

            is ApiResult.UnknownError -> {
                updateState { copy(state = Failure(UNKNOWN_ERROR)) }
                null
            }
        }
    }

    private fun startTimer(time: Long) {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var currentTime = time
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
        with(uiState.value.state) {
            (this as Success).questions.associate { it.id to Option(id = 0,"") } + selectedOption.value
        }

    companion object {
        internal const val IS_TEST_FLAG = "IS_TEST_FLAG"
        fun Int.toMilliSecond() = this.times(1000).toLong()
    }
}
