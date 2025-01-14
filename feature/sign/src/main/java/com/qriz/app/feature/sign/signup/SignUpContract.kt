package com.qriz.app.feature.sign.signup

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState
import com.qriz.app.feature.sign.R
import com.qriz.app.feature.sign.signup.SignUpUiState.SignUpPage.EMAIL
import com.qriz.app.feature.sign.signup.SignUpUiState.SignUpPage.EMAIL_AUTH
import com.qriz.app.feature.sign.signup.SignUpUiState.SignUpPage.ID
import com.qriz.app.feature.sign.signup.SignUpUiState.SignUpPage.NAME
import com.qriz.app.feature.sign.signup.SignUpUiState.SignUpPage.PW
import com.qriz.app.feature.sign.signup.SignUpViewModel.Companion.AUTHENTICATION_LIMIT_MILS

@Immutable
data class SignUpUiState(
    val name: String,
    val email: String,
    val id: String,
    val pw: String,
    val pwCheck: String,
    val emailAuthNumber: String,
    val emailAuthState: AuthenticationState,
    val nameErrorMessageResId: Int,
    val emailErrorMessageResId: Int,
    val emailAuthNumberErrorMessageResId: Int,
    val idErrorMessageResId: Int,
    val pwErrorMessageResId: Int,
    val pwCheckErrorMessageResId: Int,
    val page: SignUpPage,
    val isNotDuplicatedId: Boolean,
    val timer: Long,
) : UiState {
    val topBarTitleResId: Int = when (page) {
        NAME -> R.string.screen_title_enter_name
        EMAIL, EMAIL_AUTH -> R.string.screen_title_email_auth
        ID, PW -> R.string.screen_title_sign_up
    }

    val timerText: String =
        "${(timer / 60000)}:${(timer % 60000 / 1000).toString().padStart(2, '0')}"

    val canSignUp: Boolean
        get() = PW_REGEX.matches(pw)
                && pw == pwCheck

    val isValidEmail: Boolean
        get() = EMAIL_REGEX.matches(email)

    val isValidId: Boolean
        get() = ID_REGEX.matches(id)

    val isValidName: Boolean
        get() = USER_NAME_REGEX.matches(name)

    val isVerifiedEmailAuth: Boolean
        get() = emailAuthState == AuthenticationState.VERIFIED

    val isTimeExpiredEmailAuth: Boolean
        get() = emailAuthState == AuthenticationState.TIME_EXPIRED

    val isSendFailedEmailAuth: Boolean
        get() = emailAuthState == AuthenticationState.SEND_FAILED

    enum class AuthenticationState {
        SEND_FAILED, NONE, VERIFIED, UNVERIFIED, TIME_EXPIRED;
    }

    enum class SignUpPage(val index: Int) {
        NAME(0),
        EMAIL(1),
        EMAIL_AUTH(2),
        ID(3),
        PW(4);

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
            emailErrorMessageResId = R.string.empty,
            emailAuthNumberErrorMessageResId = R.string.empty,
            idErrorMessageResId = R.string.empty,
            pwErrorMessageResId = R.string.empty,
            nameErrorMessageResId = R.string.empty,
            pwCheckErrorMessageResId = R.string.empty,
            page = NAME,
            isNotDuplicatedId = false,
            timer = AUTHENTICATION_LIMIT_MILS,
        )

        //TODO : 아이디부분 10자 짧은 것같아 서버 수정 대기중
        /**
         * 1. 이메일 아이디 부분 : 영문 대소문자와 숫자만 허용, 최소 2자 최대 10자까지 가능
         * 2. 도메인 이름 부분 : 영문 대소문자와 숫자만 허용, 최소 2자 최대 6자까지 가능
         * 3. 최상위 도메인 부분 : 영문 대소문자만 허용, 최소 2자 최대 3자까지 가능 (예: com, net, org)
         */
        val EMAIL_REGEX =
            "^[a-zA-Z0-9]{2,10}@[a-zA-Z0-9]{2,6}\\\\.[a-zA-Z]{2,3}\$".toRegex()

        /**
         * 1. 길이 6자 이상 20자 이하
         * 2. 영문과 숫자를 반드시 둘 다 포함
         * 3. 공백 불포함
         * 4. 특수문자 불포함
         */
        const val ID_MAX_LENGTH = 20
        val ID_REGEX = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{6,$ID_MAX_LENGTH}\$".toRegex()

        /**
         * 1. 길이 : 최소 8 ~ 16 자
         * 2. 대문자 포함 : 최소 한 개의 대문자를 포함
         * 3. 소문자 포함 : 최소 한 개의 소문자를 포함
         * 4. 숫자 포함 : 하나 이상의 숫자 포함
         * 5. 특수 문자 포함 : 하나 이상의 특수 문자 포함
         */
        const val PW_MAX_LENGTH = 16
        val PW_REGEX =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=*!])(?=\\S+$).{8,$PW_MAX_LENGTH}$".toRegex()

        /** 한글/영문 1~20자 이내 */
        const val USER_NAME_MAX_LENGTH = 20
        val USER_NAME_REGEX = "^[a-zA-Z가-힣]{1,$USER_NAME_MAX_LENGTH}\$".toRegex()

    }
}

sealed interface SignUpUiAction : UiAction {
    data class ChangeUserName(val name: String) : SignUpUiAction
    data class ChangeUserId(val id: String) : SignUpUiAction
    data class ChangeUserPw(val pw: String) : SignUpUiAction
    data class ChangeUserPwCheck(val pw: String) : SignUpUiAction
    data class ChangeEmail(val email: String) : SignUpUiAction
    data class ChangeEmailAuthNum(val authNum: String) : SignUpUiAction
    data object ClickPreviousPage : SignUpUiAction
    data object ClickNextPage : SignUpUiAction
    data object ClickEmailAuthNumSend : SignUpUiAction
    data object ClickIdDuplicateCheck : SignUpUiAction
    data object ClickSignUp : SignUpUiAction
}

sealed interface SignUpUiEffect : UiEffect {
    data object SignUpUiComplete : SignUpUiEffect
    data class ShowSnackBer(
        @StringRes val defaultResId: Int,
        val message: String? = null
    ) : SignUpUiEffect
}
