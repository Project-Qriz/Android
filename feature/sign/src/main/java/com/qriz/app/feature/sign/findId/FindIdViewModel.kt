package com.qriz.app.feature.sign.findId

import androidx.lifecycle.viewModelScope
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
                updateState { copy(isVisibleSuccessDialog = false) }
                sendEffect(FindIdEffect.Complete)
            }

            is FindIdUiAction.OnChangeEmail -> {
                updateState { copy(email = action.email) }
            }

            is FindIdUiAction.SendEmailToFindId -> {
                sendEmailToFindId()
            }

            is FindIdUiAction.ConfirmErrorDialog -> {
                updateState { copy(errorDialogMessage = null) }
            }

            FindIdUiAction.ConfirmNetworkErrorDialog -> {
                updateState { copy(isVisibleNetworkErrorDialog = false) }
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
                    isVisibleSuccessDialog = true,
                )
            }
        }.onFailure { exception ->
            when (exception) {
                is UnknownHostException,
                is SocketTimeoutException -> {
                    updateState {
                        copy(
                            errorMessageResId = R.string.empty,
                            isVisibleNetworkErrorDialog = true,
                        )
                    }
                }

                else -> {
                    updateState {
                        copy(
                            errorMessageResId = R.string.empty,
                            errorDialogMessage = exception.message ?: "",
                        )
                    }
                }
            }
        }
    }
}
