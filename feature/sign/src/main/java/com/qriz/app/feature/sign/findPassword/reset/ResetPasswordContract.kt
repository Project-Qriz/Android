package com.qriz.app.feature.sign.findPassword.reset

import androidx.compose.runtime.Immutable
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState
import com.qriz.app.feature.sign.R
import com.quiz.app.core.data.user.user_api.model.PW_MAX_LENGTH
import com.quiz.app.core.data.user.user_api.model.PW_MIN_LENGTH
import com.quiz.app.core.data.user.user_api.model.PW_REGEX

@Immutable
data class ResetPasswordUiState(
    val password: String,
    val passwordConfirm: String,
    val passwordConfirmSupportingTextResId: Int,
    val visiblePassword: Boolean,
    val visiblePasswordConfirm: Boolean,
    val isFocusedPassword: Boolean,
    val isFocusedPasswordConfirm: Boolean,
) : UiState {

    val isValidPasswordFormat: Boolean
        get() = PW_REGEX.matches(password)

    val isValidPasswordLength: Boolean
        get() = password.length in PW_MIN_LENGTH..PW_MAX_LENGTH

    val isEqualsPassword: Boolean
        get() = password.isNotEmpty()
                && passwordConfirm.isNotEmpty()
                && password == passwordConfirm

    val canResetPassword: Boolean
        get() = isValidPasswordFormat && isValidPasswordLength && isEqualsPassword

    val passwordConfirmErrorMessageResId: Int = if (passwordConfirm.isNotEmpty() && password != passwordConfirm) {
        R.string.passwords_do_not_match
    } else {
        R.string.empty
    }

    companion object {
        val DEFAULT = ResetPasswordUiState(
            password = "",
            passwordConfirm = "",
            passwordConfirmSupportingTextResId = 0,
            visiblePassword = false,
            visiblePasswordConfirm = false,
            isFocusedPassword = false,
            isFocusedPasswordConfirm = false,
        )
    }
}

sealed interface ResetPasswordUiAction : UiAction {
    data class OnChangePassword(val password: String) : ResetPasswordUiAction
    data class OnChangePasswordConfirm(val passwordConfirm: String) : ResetPasswordUiAction
    data object ResetPassword : ResetPasswordUiAction
    data class ChangePasswordVisibility(val visible: Boolean) : ResetPasswordUiAction
    data class ChangePasswordConfirmVisibility(val visible: Boolean) : ResetPasswordUiAction
    data class ChangeFocusPassword(val isFocused: Boolean) : ResetPasswordUiAction
    data class ChangeFocusPasswordConfirm(val isFocused: Boolean) : ResetPasswordUiAction
}

sealed interface ResetPasswordUiEffect : UiEffect {
    data object ResetComplete : ResetPasswordUiEffect
}
