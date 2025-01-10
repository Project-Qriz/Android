package com.qriz.app.feature.sign.signUp

import android.util.Patterns
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState
import com.qriz.app.feature.sign.R
import com.qriz.app.feature.sign.signUp.SignUpUiState.SignUpPage.EMAIL
import com.qriz.app.feature.sign.signUp.SignUpUiState.SignUpPage.EMAIL_AUTH
import com.qriz.app.feature.sign.signUp.SignUpUiState.SignUpPage.ID
import com.qriz.app.feature.sign.signUp.SignUpUiState.SignUpPage.NAME
import com.qriz.app.feature.sign.signUp.SignUpUiState.SignUpPage.PW
import com.qriz.app.feature.sign.signUp.SignUpViewModel.Companion.AUTHENTICATION_LIMIT_MILS
import java.util.regex.Pattern

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
    val isAvailableId: Boolean,
    val page: SignUpPage,
    val timer: Long,
) : UiState {
    val emailVerified: Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    val topBarTitleResId: Int = when (page) {
        NAME -> R.string.screen_title_enter_name
        EMAIL, EMAIL_AUTH -> R.string.screen_title_email_auth
        ID, PW -> R.string.screen_title_sign_up
    }

    val timerText: String =
        "${(timer / 60000)}:${(timer % 60000 / 1000).toString().padStart(2, '0')}"

    val canSignUp: Boolean = Pattern.compile("^[a-zA-Z0-9]{8,10}$").matcher(pw).matches()
            && pw == pwCheck

    val validName: Boolean = """[가-힣]{2,}""".toRegex().matches(name)

    enum class AuthenticationState {
        None, Verified, Unverified;
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
            emailAuthState = AuthenticationState.None,
            emailErrorMessageResId = R.string.empty,
            emailAuthNumberErrorMessageResId = R.string.empty,
            idErrorMessageResId = R.string.empty,
            pwErrorMessageResId = R.string.empty,
            nameErrorMessageResId = R.string.empty,
            pwCheckErrorMessageResId = R.string.empty,
            isAvailableId = false,
            page = NAME,
            timer = AUTHENTICATION_LIMIT_MILS,
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
