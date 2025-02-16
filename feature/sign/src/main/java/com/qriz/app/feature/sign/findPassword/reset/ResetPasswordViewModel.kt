package com.qriz.app.feature.sign.findPassword.reset

import androidx.lifecycle.viewModelScope
import com.qriz.app.feature.base.BaseViewModel
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel<ResetPasswordUiState, ResetPasswordUiEffect, ResetPasswordUiAction>(
    ResetPasswordUiState.DEFAULT
) {
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
        }
    }

    private fun resetPassword() {
        viewModelScope.launch {
            runCatching {
                userRepository.resetPassword(uiState.value.password)
            }.onSuccess {
                sendEffect(ResetPasswordUiEffect.ResetComplete)
            }.onFailure {
                //TODO: Handle error
            }
        }
    }
}
