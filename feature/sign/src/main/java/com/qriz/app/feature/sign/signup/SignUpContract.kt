package com.qriz.app.feature.sign.signup

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState
import com.qriz.app.feature.sign.R
import com.qriz.app.feature.sign.signup.SignUpUiState.SignUpPage.AUTH
import com.qriz.app.feature.sign.signup.SignUpViewModel.Companion.AUTHENTICATION_LIMIT_MILS
import com.quiz.app.core.data.user.user_api.model.AUTH_NUMBER_MAX_LENGTH
import com.quiz.app.core.data.user.user_api.model.EMAIL_REGEX
import com.quiz.app.core.data.user.user_api.model.ID_REGEX
import com.quiz.app.core.data.user.user_api.model.PW_REGEX
import com.quiz.app.core.data.user.user_api.model.USER_NAME_REGEX

@Immutable
data class SignUpUiState(
    val name: String,
    val email: String,
    val id: String,
    val pw: String,
    val pwCheck: String,
    val emailAuthNumber: String,
    val emailAuthState: AuthenticationState,
    val focusState: FocusState,
    val idValidationState: UserIdValidationState,
    val nameErrorMessageResId: Int,
    val emailSupportingTextResId: Int,
    val authNumberSupportingTextResId: Int,
    val idErrorMessageResId: Int,
    val pwErrorMessageResId: Int,
    val pwCheckErrorMessageResId: Int,
    val page: SignUpPage,
    val emailAuthTime: Long,
) : UiState {
    val topBarTitleResId: Int = R.string.screen_title_sign_up

    val timerText: String =
        "${(emailAuthTime / 60000)}:${(emailAuthTime % 60000 / 1000).toString().padStart(2, '0')}"

    val canSignUp: Boolean
        get() = PW_REGEX.matches(pw)
                && pw == pwCheck

    val isValidEmail: Boolean
        get() = EMAIL_REGEX.matches(email)

    val isValidId: Boolean
        get() = ID_REGEX.matches(id)

    val isValidName: Boolean
        get() = USER_NAME_REGEX.matches(name)

    val isSendEmailAuthNum: Boolean
        get() = emailAuthState == AuthenticationState.SEND_SUCCESS

    val isVerifiedEmailAuth: Boolean
        get() = emailAuthState == AuthenticationState.VERIFIED

    val isTimeExpiredEmailAuth: Boolean
        get() = emailAuthState == AuthenticationState.TIME_EXPIRED

    val isSendFailedEmailAuth: Boolean
        get() = emailAuthState == AuthenticationState.SEND_FAILED

    val enableAuthNumVerifyButton: Boolean
        get() = emailAuthNumber.length == AUTH_NUMBER_MAX_LENGTH

    val showAuthNumberLayout: Boolean
        get() = emailAuthState != AuthenticationState.NONE &&
                emailAuthState != AuthenticationState.SEND_FAILED

    enum class AuthenticationState {
        SEND_SUCCESS, SEND_FAILED, NONE, VERIFIED, REJECTED, TIME_EXPIRED;
    }

    enum class FocusState {
        NONE, EMAIL, AUTH_NUM, ID, NAME, PW, PW_CHECK;
    }

    enum class UserIdValidationState {
        NONE, AVAILABLE, NOT_AVAILABLE;
    }

    enum class SignUpPage(val index: Int) {
        AUTH(0),
        NAME(1),
        ID(2),
        PW(3);

        val next
            get() = entries[index + 1]
        val previous
            get() = entries[index - 1]
        val percent
            get() = (index + 1).toFloat() / entries.size.toFloat()
    }

    companion object {
        val Default = SignUpUiState(
            name = "",
            email = "",
            id = "",
            pw = "",
            pwCheck = "",
            emailAuthNumber = "",
            emailAuthState = AuthenticationState.NONE,
            idValidationState = UserIdValidationState.NONE,
            emailSupportingTextResId = R.string.empty,
            authNumberSupportingTextResId = R.string.empty,
            idErrorMessageResId = R.string.empty,
            pwErrorMessageResId = R.string.empty,
            nameErrorMessageResId = R.string.empty,
            pwCheckErrorMessageResId = R.string.empty,
            page = AUTH,
            emailAuthTime = AUTHENTICATION_LIMIT_MILS,
            focusState = FocusState.NONE,
        )
    }
}

sealed interface SignUpUiAction : UiAction {
    data class ChangeUserName(val name: String) : SignUpUiAction
    data class ChangeUserId(val id: String) : SignUpUiAction
    data class ChangeUserPw(val pw: String) : SignUpUiAction
    data class ChangeUserPwCheck(val pw: String) : SignUpUiAction
    data class ChangeEmail(val email: String) : SignUpUiAction
    data class ChangeEmailAuthNum(val authNum: String) : SignUpUiAction
    data class ChangeFocusState(val focusState: SignUpUiState.FocusState) : SignUpUiAction
    data object ClickPreviousPage : SignUpUiAction
    data object ClickNextPage : SignUpUiAction
    data object ClickEmailAuthNumSend : SignUpUiAction
    data object ClickIdDuplicateCheck : SignUpUiAction
    data object ClickSignUp : SignUpUiAction
    data object ClickVerifyAuthNum : SignUpUiAction
    data object StartEmailAuthTimer : SignUpUiAction
}

sealed interface SignUpUiEffect : UiEffect {
    data object SignUpUiComplete : SignUpUiEffect
    data object MoveToBack : SignUpUiEffect
    data class ShowSnackBer(
        @StringRes val defaultResId: Int,
        val message: String? = null
    ) : SignUpUiEffect
}
