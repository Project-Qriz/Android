package com.qriz.app.feature.onboard.previewresult

import androidx.lifecycle.viewModelScope
import com.qriz.app.core.data.onboard.onboard_api.repository.OnBoardRepository
import com.qriz.app.feature.base.BaseViewModel
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class PreviewResultViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val onBoardRepository: OnBoardRepository,
) : BaseViewModel<PreviewResultUiState, PreviewResultUiEffect, PreviewResultUiAction>(
    PreviewResultUiState.Default
) {
    override fun process(action: PreviewResultUiAction) = viewModelScope.launch {
        when (action) {
            PreviewResultUiAction.LoadPreviewResult -> loadPreviewResult()
            PreviewResultUiAction.ObserveClient -> observeClient()
            PreviewResultUiAction.ClickClose -> onClickClose()
        }
    }

    private fun onClickClose() {
        sendEffect(PreviewResultUiEffect.MoveToWelcomeGuide(userName = uiState.value.userName))
    }

    private fun loadPreviewResult() = viewModelScope.launch {
        updateState { copy(isLoading = true) }
        runCatching { onBoardRepository.getPreviewTestResult() }
            .onSuccess { result ->
                updateState {
                    copy(
                        isLoading = false,
                        previewTestResult = result
                    )
                }
            }
            .onFailure { updateState { copy(isLoading = false) } }
    }

    private fun observeClient() = viewModelScope.launch {
        userRepository.getClientFlow()
            .collect { user -> updateState { copy(userName = user.name) } }
    }
}
