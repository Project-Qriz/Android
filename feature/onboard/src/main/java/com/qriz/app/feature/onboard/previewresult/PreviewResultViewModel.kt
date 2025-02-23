package com.qriz.app.feature.onboard.previewresult

import androidx.lifecycle.viewModelScope
import com.qriz.app.core.data.onboard.onboard_api.repository.OnBoardRepository
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
        }
    }

    private fun onClickClose() {
        sendEffect(PreviewResultUiEffect.MoveToWelcomeGuide(userName = uiState.value.userName))
    }

    private fun loadPreviewResult() = viewModelScope.launch {
        updateState { copy(state = State.LOADING) }
        runCatching { onBoardRepository.getPreviewTestResult() }
            .onSuccess { result ->
                updateState {
                    copy(
                        previewTestResultItem = result.toPreviewTestResultItem(),
                        state = State.SUCCESS
                    )
                }
            }
            .onFailure { updateState { copy(state = State.FAILURE) } }
    }

    private fun observeClient() = viewModelScope.launch {
        userRepository.getClientFlow()
            .collect { user -> updateState { copy(userName = user.name) } }
    }
}
