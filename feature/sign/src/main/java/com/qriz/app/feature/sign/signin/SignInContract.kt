package com.qriz.app.feature.sign.signin

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState
import com.qriz.app.feature.sign.R

@Immutable
data class SignInUiState(
    val id: String,
    val pw: String,
    val isVisiblePw: Boolean,
    val loginErrorMessageResId: Int,
    val isLoading: Boolean,
) : UiState {
    val isAvailableLogin
        get() = id.isNotBlank()
                && pw.isNotBlank()
                && loginErrorMessageResId != R.string.user_not_registered

    companion object {
        val Default = SignInUiState(
            id = "",
            pw = "",
            isVisiblePw = false,
            loginErrorMessageResId = R.string.empty,
            isLoading = false
        )
    }
}

sealed interface SignInUiAction : UiAction {
    data class ShowSnackbar(val message: String) : SignInUiAction
    data class ChangeUserId(val id: String) : SignInUiAction
    data class ChangeUserPw(val pw: String) : SignInUiAction
    data class ClickPwVisibility(val isVisible: Boolean) : SignInUiAction
    data object ClickLogin : SignInUiAction
    data object ClickSignUp : SignInUiAction
    data object ClickFindId : SignInUiAction
    data object ClickFindPw : SignInUiAction
    data object ClickKakaoLogin : SignInUiAction
    data object ClickGoogleLogin : SignInUiAction
    data object ClearCredentialState: SignInUiAction
    data class ProcessKakaoLogin(val token: String): SignInUiAction
    data class ProcessGoogleLogin(val token: String): SignInUiAction
}

sealed interface SignInUiEffect : UiEffect {
    data object MoveToSignUp : SignInUiEffect
    data object MoveToFindId : SignInUiEffect
    data object MoveToFindPw : SignInUiEffect
    data object MoveToHome : SignInUiEffect
    data object MoveToConceptCheckGuide : SignInUiEffect
    data object ClearCredentialState : SignInUiEffect
    data class ShowSnackBar(
        @StringRes val defaultResId: Int,
        val message: String? = null
    ) : SignInUiEffect

    data object KakaoLogin : SignInUiEffect
    data object GoogleLogin : SignInUiEffect
}
