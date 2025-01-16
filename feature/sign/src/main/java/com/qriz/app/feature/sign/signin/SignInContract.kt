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
                && loginErrorMessageResId == R.string.empty

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
    data class ChangeUserId(val id: String) : SignInUiAction
    data class ChangeUserPw(val pw: String) : SignInUiAction
    data class ClickPwVisibility(val isVisible: Boolean) : SignInUiAction
    data object ClickLogin : SignInUiAction
    data object ClickSignUp : SignInUiAction
    data object ClickFindId : SignInUiAction
    data object ClickFindPw : SignInUiAction
}

sealed interface SignInUiEffect : UiEffect {
    data object MoveToSignUp : SignInUiEffect
    data object MoveToFindId : SignInUiEffect
    data object MoveToFindPw : SignInUiEffect
    data object MoveToHome : SignInUiEffect
    data class ShowSnackBar(
        @StringRes val defaultResId: Int,
        val message: String? = null
    ) : SignInUiEffect
}
