package com.qriz.app.feature.sign.findId

import androidx.lifecycle.viewModelScope
import com.qriz.app.core.ui.common.const.NETWORK_DISABLE
import com.qriz.app.core.ui.common.const.UNKNOWN_ERROR
import com.qriz.app.feature.base.BaseViewModel
import com.qriz.app.feature.sign.R
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class FindIdViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel<FindIdUiState, FindIdEffect, FindIdUiAction>(
    initialState = FindIdUiState.DEFAULT
) {
    override fun process(action: FindIdUiAction): Job = viewModelScope.launch {
        when (action) {
            is FindIdUiAction.ConfirmSuccessDialog -> {
                updateState { copy(successDialogState = DialogState.EMPTY) }
                sendEffect(FindIdEffect.Complete)
            }

            is FindIdUiAction.OnChangeEmail -> {
                updateState { copy(email = action.email) }
            }

            is FindIdUiAction.SendEmailToFindId -> {
                sendEmailToFindId()
            }

            is FindIdUiAction.ConfirmErrorDialog -> {
                updateState { copy(errorDialogState = DialogState.EMPTY) }
            }
        }
    }

    private fun sendEmailToFindId() = viewModelScope.launch {
        val email = uiState.value.email
        val isEmailEmpty = email.isEmpty()
        val isValidEmailFormat = uiState.value.isValidEmailFormat

        if (isEmailEmpty) {
            updateState { copy(errorMessageResId = R.string.email_is_empty) }
            return@launch
        }

        if (isValidEmailFormat.not()) {
            updateState { copy(errorMessageResId = R.string.email_is_invalid_format) }
            return@launch
        }

        runCatching {
            userRepository.sendEmailToFindId(email)
        }.onSuccess {
            updateState {
                copy(
                    errorMessageResId = R.string.empty,
                    successDialogState = DialogState(
                        title = SUCCESS_DIALOG_TITLE,
                        message = SUCCESS_DIALOG_MESSAGE,
                    ),
                )
            }
        }.onFailure { exception ->
            when (exception) {
                is UnknownHostException,
                is SocketTimeoutException -> {
                    updateState {
                        copy(
                            errorMessageResId = R.string.empty,
                            errorDialogState = DialogState(
                                title = FAIL_DIALOG_TITLE,
                                message = NETWORK_DISABLE
                            )
                        )
                    }
                }

                else -> {
                    updateState {
                        copy(
                            errorMessageResId = R.string.empty,
                            errorDialogState = DialogState(
                                title = FAIL_DIALOG_TITLE,
                                message = exception.message ?: UNKNOWN_ERROR
                            )
                        )
                    }
                }
            }
        }
    }

    companion object {
        const val SUCCESS_DIALOG_TITLE = "이메일 발송 완료"
        const val SUCCESS_DIALOG_MESSAGE = "입력하신 이메일 주소로 비밀번호가 발송되었습니다. 메일함을 확인해주세요."
        const val FAIL_DIALOG_TITLE = "이메일 전송 실패"
    }
}
