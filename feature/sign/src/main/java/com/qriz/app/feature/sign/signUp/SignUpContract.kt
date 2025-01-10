package com.qriz.app.feature.sign.signUp

import android.util.Patterns
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState
import com.qriz.app.feature.sign.R
import com.qriz.app.feature.sign.signUp.SignUpViewModel.Companion.AUTHENTICATION_LIMIT_MILS
import java.util.regex.Pattern

@Immutable
data class SignUpUiState(
    val name: String,
    val email: String,
    val id: String,
    val authenticationNumber: String,
    val authenticationState: AuthenticationState,
    val password: String,
    val passwordCheck: String,
    val nameErrorMessage: String,
    val emailErrorMessage: String,
    val authenticationNumberErrorMessage: String,
    val idErrorMessage: String,
    val passwordErrorMessage: String,
    val passwordCheckErrorMessage: String,
    val isAvailableId: Boolean,
    val page: Int,
    val timer: Long,
) : UiState {
    val emailVerified: Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    val topBarTitleResId: Int = when (page) {
        0 -> R.string.screen_title_enter_name
        1, 2 -> R.string.screen_title_email_auth
        3, 4 -> R.string.screen_title_sign_up
        else -> throw IllegalStateException("잘못된 페이지입니다.")
    }

    val timerText: String =
        "${(timer / 60000)}:${(timer % 60000 / 1000).toString().padStart(2, '0')}"

    val canSignUp: Boolean = Pattern.compile("^[a-zA-Z0-9]{8,10}$").matcher(password).matches()
            && password == passwordCheck

    val validName: Boolean = """[가-힣]{2,}""".toRegex().matches(name)

    enum class AuthenticationState {
        None, Verified, Unverified;
    }

    companion object {
        val Default = SignUpUiState(
            name = "",
            email = "",
            authenticationNumber = "",
            authenticationState = AuthenticationState.None,
            id = "",
            password = "",
            passwordCheck = "",
            emailErrorMessage = "",
            authenticationNumberErrorMessage = "",
            idErrorMessage = "",
            passwordErrorMessage = "",
            nameErrorMessage = "",
            passwordCheckErrorMessage = "",
            isAvailableId = false,
            page = 0,
            timer = AUTHENTICATION_LIMIT_MILS,
        )
    }
}

sealed interface SignUpUiAction : UiAction

sealed interface SignUpUiEffect : UiEffect {
    data object SignUpUiComplete : SignUpUiEffect
    data class ShowSnackBer(
        @StringRes val defaultResId: Int,
        val message: String? = null
    ) : SignUpUiEffect
}
