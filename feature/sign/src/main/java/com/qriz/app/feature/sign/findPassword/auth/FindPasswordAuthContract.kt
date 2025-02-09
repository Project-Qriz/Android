package com.qriz.app.feature.sign.findPassword.auth

import com.qriz.app.core.designsystem.theme.Gray200
import com.qriz.app.core.designsystem.theme.Gray300
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.Mint800
import com.qriz.app.core.designsystem.theme.Red500
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState
import com.qriz.app.feature.sign.R
import com.quiz.app.core.data.user.user_api.model.EMAIL_REGEX

data class FindPasswordAuthUiState(
    val email: String,
    val authNumber: String,
    val authTimerMs: Long,
    val showAuthNumberLayout: Boolean,
    val verifiedAuthNumber: Boolean,
    val enableInputAuthNumber: Boolean,
    val showFailSendEmailDialog: Boolean,
    val emailSupportingTextResId: Int,
    val authNumberSupportingTextResId: Int,
) : UiState {

    val isValidEmailFormat = EMAIL_REGEX.matches(email)

    val isValidAuthNumberFormat = authNumber.length == 6

    val emailSupportingTextColor = when(emailSupportingTextResId) {
        else -> Red500
    }

    val emailBorderColor = when(emailSupportingTextResId) {
        else -> Gray200
    }

    val authNumberSupportingTextColor = when(authNumberSupportingTextResId) {
        R.string.success_send_email_auth_number,
        R.string.success_verify_auth_number -> Mint800
        else -> Red500
    }

    val authNumberBorderColor = when(authNumberSupportingTextResId) {
        R.string.fail_verify_auth_number -> Red500
        else -> Gray200
    }

    val authNumberVerifiedFail: Boolean =
        authNumberSupportingTextResId == R.string.fail_verify_auth_number

    val authTimerText: String
        get() {
            val remainedSeconds = authTimerMs / 1000
            val minutes = remainedSeconds / 60
            val seconds = remainedSeconds % 60
            return "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
        }

    val emailButtonTextColor = if (showAuthNumberLayout) Gray800 else Gray300

    companion object {
        val DEFAULT = FindPasswordAuthUiState(
            email = "",
            authNumber = "",
            authTimerMs = 0,
            showAuthNumberLayout = false,
            verifiedAuthNumber = false,
            enableInputAuthNumber = true,
            showFailSendEmailDialog = false,
            emailSupportingTextResId = R.string.empty,
            authNumberSupportingTextResId = R.string.empty,
        )
    }
}

sealed interface FindPasswordAuthUiAction : UiAction {
    data class OnChangeEmail(val email: String) : FindPasswordAuthUiAction

    data class OnChangeAuthNumber(val authNumber: String) : FindPasswordAuthUiAction

    data object SendAuthNumberEmail : FindPasswordAuthUiAction

    data object VerifyAuthNumber : FindPasswordAuthUiAction
}

sealed interface FindPasswordAuthUiEffect: UiEffect {
    data object VerifiedAuthNumber : FindPasswordAuthUiEffect
}
