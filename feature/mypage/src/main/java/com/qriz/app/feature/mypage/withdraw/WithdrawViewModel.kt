package com.qriz.app.feature.mypage.withdraw

import androidx.lifecycle.viewModelScope
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.ui.common.resource.NETWORK_IS_UNSTABLE
import com.qriz.app.core.ui.common.resource.UNKNOWN_ERROR
import com.qriz.app.feature.base.BaseViewModel
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WithdrawViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : BaseViewModel<WithdrawUiState, WithdrawUiEffect, WithdrawUiAction>(
    WithdrawUiState.Default
) {

    override fun process(action: WithdrawUiAction): Job = viewModelScope.launch {
        when (action) {
            is WithdrawUiAction.ClickWithdraw -> updateState { copy(showConfirmDialog = true) }
            is WithdrawUiAction.DismissConfirmDialog -> updateState { copy(showConfirmDialog = false) }
            is WithdrawUiAction.ConfirmWithdraw -> withdraw()
        }
    }

    private suspend fun withdraw() {
        when(val result = userRepository.withdraw()) {
            is ApiResult.Success<*> -> {
                sendEffect(WithdrawUiEffect.NavigateToLogin)
            }
            is ApiResult.Failure -> {
                sendEffect(WithdrawUiEffect.ShowSnackBar(message = result.message))
            }
            is ApiResult.NetworkError -> {
                sendEffect(WithdrawUiEffect.ShowSnackBar(message = NETWORK_IS_UNSTABLE))
            }
            is ApiResult.UnknownError -> {
                sendEffect(WithdrawUiEffect.ShowSnackBar(message = UNKNOWN_ERROR))
            }
        }
    }
}
