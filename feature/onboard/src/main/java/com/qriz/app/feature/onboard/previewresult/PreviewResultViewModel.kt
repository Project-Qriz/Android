package com.qriz.app.feature.onboard.previewresult

import androidx.lifecycle.viewModelScope
import com.qriz.app.core.data.onboard.onboard_api.repository.OnBoardRepository
import com.qriz.app.core.model.ApiResult
import com.qriz.app.feature.base.BaseViewModel
import com.qriz.app.feature.onboard.previewresult.PreviewResultUiState.State
import com.qriz.app.feature.onboard.previewresult.mapper.toPreviewTestResultItem
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreviewResultViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val onBoardRepository: OnBoardRepository,
) : BaseViewModel<PreviewResultUiState, PreviewResultUiEffect, PreviewResultUiAction>(
    PreviewResultUiState.Default
) {
    override fun process(action: PreviewResultUiAction) = viewModelScope.launch {
        when (action) {
            is PreviewResultUiAction.LoadPreviewResult -> loadPreviewResult()
            is PreviewResultUiAction.ObserveClient -> observeClient()
            is PreviewResultUiAction.ClickClose -> onClickClose()
            is PreviewResultUiAction.DismissTooltip -> updateState { copy(showTooltip = false) }
            is PreviewResultUiAction.ShowTooltip -> updateState { copy(showTooltip = true) }
        }
    }

    private fun onClickClose() {
        sendEffect(PreviewResultUiEffect.MoveToWelcomeGuide(userName = uiState.value.userName))
    }

    private fun loadPreviewResult() = viewModelScope.launch {
        when (val result = onBoardRepository.getPreviewTestResult()) {
            is ApiResult.Success -> {
                updateState {
                    copy(
                        previewTestResultItem = result.data.toPreviewTestResultItem(),
                        state = State.SUCCESS
                    )
                }
            }

            is ApiResult.Failure, is ApiResult.NetworkError, is ApiResult.UnknownError -> {
                updateState { copy(state = State.FAILURE) }
            }
        }
    }

    private fun observeClient() = viewModelScope.launch {
        userRepository.getUserFlow().collect { user ->
            updateState {
                copy(userName = user.name)
            }
        }
    }
}
