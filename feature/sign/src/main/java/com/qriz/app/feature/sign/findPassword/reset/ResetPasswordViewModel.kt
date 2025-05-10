package com.qriz.app.feature.sign.findPassword.reset

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.qriz.app.core.model.ApiResult
import com.qriz.app.feature.base.BaseViewModel
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
) : BaseViewModel<ResetPasswordUiState, ResetPasswordUiEffect, ResetPasswordUiAction>(
    ResetPasswordUiState.DEFAULT
) {
    private val resetToken = savedStateHandle.get<String>("resetToken")
        ?: throw IllegalStateException("리셋 토큰이 넘어오지 않은 상태입니다.")

    override fun process(action: ResetPasswordUiAction) = viewModelScope.launch {
        when (action) {
            is ResetPasswordUiAction.OnChangePassword -> {
                updateState { copy(password = action.password) }
            }

            is ResetPasswordUiAction.OnChangePasswordConfirm -> {
                updateState { copy(passwordConfirm = action.passwordConfirm) }
            }

            is ResetPasswordUiAction.ResetPassword -> {
                if (uiState.value.canResetPassword) {
                    resetPassword()
                }
            }

            is ResetPasswordUiAction.ChangePasswordConfirmVisibility -> {
                updateState { copy(visiblePasswordConfirm = action.visible) }
            }

            is ResetPasswordUiAction.ChangePasswordVisibility -> {
                updateState { copy(visiblePassword = action.visible) }
            }

            is ResetPasswordUiAction.ChangeFocusPassword -> {
                updateState { copy(isFocusedPassword = action.isFocused) }
            }

            is ResetPasswordUiAction.ChangeFocusPasswordConfirm -> {
                updateState { copy(isFocusedPasswordConfirm = action.isFocused) }
            }

            is ResetPasswordUiAction.ConfirmNetworkErrorDialog -> {
                updateState { copy(showNetworkErrorDialog = false) }
            }

            is ResetPasswordUiAction.ConfirmUnknownErrorDialog -> {
                updateState { copy(showUnknownErrorDialog = false) }
            }
        }
    }

    private fun resetPassword() = viewModelScope.launch {
        when (userRepository.resetPassword(
            password = uiState.value.password,
            resetToken = resetToken
        )) {
            is ApiResult.Success -> {
                sendEffect(ResetPasswordUiEffect.ResetComplete)
                sendEffect(ResetPasswordUiEffect.OnShowSnackbar(message = "비밀번호 변경이 완료되었습니다.\n변경된 비밀번호로 로그인 해주세요"))
            }

            is ApiResult.NetworkError -> {
                updateState { copy(showNetworkErrorDialog = true) }
            }

            is ApiResult.Failure, is ApiResult.UnknownError -> {
                updateState { copy(showUnknownErrorDialog = true) }
            }
        }
    }
}
