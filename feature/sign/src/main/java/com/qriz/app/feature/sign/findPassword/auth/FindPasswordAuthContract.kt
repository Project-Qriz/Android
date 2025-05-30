package com.qriz.app.feature.sign.findPassword.auth

import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState
import com.qriz.app.feature.sign.R
import com.quiz.app.core.data.user.user_api.model.EMAIL_REGEX

data class FindPasswordAuthUiState(
    val email: String,
    val authNumber: String,
    val authTimerMs: Long,
    val resetToken: String,
    val showAuthNumberLayout: Boolean,
    val verifiedAuthNumber: Boolean,
    val enableInputAuthNumber: Boolean,
    val showFailSendEmailDialog: Boolean,
    val showFailVerifyAuthNumberDialog: Boolean,
    val emailSupportingTextResId: Int,
    val authNumberSupportingTextResId: Int,
    val showNetworkErrorDialog: Boolean,
) : UiState {

    val isValidEmailFormat = EMAIL_REGEX.matches(email)

    val isValidAuthNumberFormat = authNumber.length == 6

    val authTimerText: String
        get() {
            val remainedSeconds = authTimerMs / 1000
            val minutes = remainedSeconds / 60
            val seconds = remainedSeconds % 60
            return "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
        }

    companion object {
        val DEFAULT = FindPasswordAuthUiState(
            email = "",
            authNumber = "",
            authTimerMs = 0,
            resetToken = "",
            showAuthNumberLayout = false,
            verifiedAuthNumber = false,
            enableInputAuthNumber = true,
            showFailSendEmailDialog = false,
            emailSupportingTextResId = R.string.empty,
            authNumberSupportingTextResId = R.string.empty,
            showNetworkErrorDialog = false,
            showFailVerifyAuthNumberDialog = false
        )
    }
}

sealed interface FindPasswordAuthUiAction : UiAction {
    data class OnChangeEmail(val email: String) : FindPasswordAuthUiAction

    data class OnChangeAuthNumber(val authNumber: String) : FindPasswordAuthUiAction

    data object SendAuthNumberEmail : FindPasswordAuthUiAction

    data object VerifyAuthNumber : FindPasswordAuthUiAction

    data object ClickReset : FindPasswordAuthUiAction

    data object ConfirmNetworkErrorDialog : FindPasswordAuthUiAction

    data object ConfirmSendingEmailFailDialog : FindPasswordAuthUiAction

    data object ConfirmVerifyingAuthNumberDialog : FindPasswordAuthUiAction
}

sealed interface FindPasswordAuthUiEffect: UiEffect {
    data class NavigateToResetPassword(val resetToken: String) : FindPasswordAuthUiEffect
}
