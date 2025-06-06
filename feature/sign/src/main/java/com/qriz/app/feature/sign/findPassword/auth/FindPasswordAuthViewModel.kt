package com.qriz.app.feature.sign.findPassword.auth

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.viewModelScope
import com.qriz.app.core.model.ApiResult
import com.qriz.app.feature.base.BaseViewModel
import com.qriz.app.feature.sign.R
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class FindPasswordAuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : BaseViewModel<FindPasswordAuthUiState, FindPasswordAuthUiEffect, FindPasswordAuthUiAction>(
    FindPasswordAuthUiState.DEFAULT
) {
    private var timerJob: Job? = null

    override fun process(action: FindPasswordAuthUiAction): Job = viewModelScope.launch {
        when (action) {
            is FindPasswordAuthUiAction.OnChangeEmail -> updateState { copy(email = action.email) }

            is FindPasswordAuthUiAction.SendAuthNumberEmail -> sendEmail()

            is FindPasswordAuthUiAction.OnChangeAuthNumber -> updateState { copy(authNumber = action.authNumber) }

            is FindPasswordAuthUiAction.VerifyAuthNumber -> verifyAuthNumber()

            is FindPasswordAuthUiAction.ClickReset -> if (uiState.value.verifiedAuthNumber) {
                sendEffect(FindPasswordAuthUiEffect.NavigateToResetPassword(uiState.value.resetToken))
            }

            is FindPasswordAuthUiAction.ConfirmNetworkErrorDialog -> updateState { copy(showNetworkErrorDialog = false) }

            is FindPasswordAuthUiAction.ConfirmSendingEmailFailDialog -> updateState { copy(showFailSendEmailDialog = false) }

            is FindPasswordAuthUiAction.ConfirmVerifyingAuthNumberDialog -> updateState { copy(showFailVerifyAuthNumberDialog = false) }
        }
    }

    private suspend fun sendEmail() {
        val email = uiState.value.email
        val isEmailEmpty = email.isEmpty()
        val isValidEmailFormat = uiState.value.isValidEmailFormat

        if (isEmailEmpty) {
            updateState { copy(emailSupportingTextResId = R.string.email_is_empty) }
            return
        }

        if (isValidEmailFormat.not()) {
            updateState { copy(emailSupportingTextResId = R.string.email_is_invalid_format) }
            return
        }

        when(userRepository.sendEmailToFindPassword(email = email)) {
            is ApiResult.Success -> {
                updateState {
                    copy(
                        emailSupportingTextResId = R.string.empty,
                        authNumberSupportingTextResId = R.string.success_send_email_auth_number,
                        enableInputAuthNumber = true,
                        showAuthNumberLayout = true,
                        verifiedAuthNumber = false,
                    )
                }
                startTimer()
            }

            is ApiResult.Failure -> {
                updateState { copy(authNumberSupportingTextResId = R.string.email_is_not_exist) }
            }

            is ApiResult.NetworkError -> {
                updateState { copy(showNetworkErrorDialog = true) }
            }

            is ApiResult.UnknownError -> {
                updateState { copy(showFailSendEmailDialog = true) }
            }
        }
    }

    private suspend fun verifyAuthNumber() {
        val authNumber = uiState.value.authNumber
        val email = uiState.value.email

        if (uiState.value.isValidAuthNumberFormat.not()) {
            updateState { copy(authNumberSupportingTextResId = R.string.invalid_auth_number_format) }
            return
        }

        when(val result = userRepository.verifyPasswordAuthNumber(email = email, authNumber = authNumber)) {
            is ApiResult.Success -> {
                cancelTimer()
                updateState {
                    copy(
                        authNumberSupportingTextResId = R.string.success_verify_auth_number,
                        verifiedAuthNumber = true,
                        resetToken = result.data,
                    )
                }
            }
            is ApiResult.Failure -> {
                updateState { copy(authNumberSupportingTextResId = R.string.fail_verify_auth_number) }
            }
            is ApiResult.NetworkError -> {
                updateState { copy(showNetworkErrorDialog = true) }
            }
            is ApiResult.UnknownError -> {
                updateState { copy(showFailVerifyAuthNumberDialog = true) }
            }
        }
    }

    private fun startTimer() {
        updateState { copy(authTimerMs = AUTH_TIME_LIMIT_MILS) }

        timerJob = viewModelScope.launch {
            val interval = 1000L
            var currentTime = uiState.value.authTimerMs
            while (isActive) {
                if (currentTime <= 0) break

                delay(interval)
                currentTime -= interval
                updateState { copy(authTimerMs = currentTime) }
            }

            updateState {
                copy(
                    enableInputAuthNumber = false,
                    verifiedAuthNumber = false,
                    authNumberSupportingTextResId = R.string.time_over_auth_number_verify,
                )
            }
        }
    }

    private fun cancelTimer() {
        timerJob?.cancel()
        timerJob = null
        updateState { copy(authTimerMs = 0) }
    }

    @VisibleForTesting
    internal fun isTimerJobNull() = timerJob == null

    @VisibleForTesting
    internal fun isTimerJobNonNull() = timerJob != null

    companion object {
        const val AUTH_TIME_LIMIT_MILS = 180000L
    }
}
