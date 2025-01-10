package com.qriz.app.feature.sign.signin

import androidx.compose.runtime.Immutable
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState

@Immutable
data class SignInUiState(
    val id: String,
    val password: String,
    val showPassword: Boolean,
    val idErrorMessage: String,
    val passwordErrorMessage: String,
) : UiState {
    companion object {
        val Default = SignInUiState(
            id = "",
            password = "",
            showPassword = false,
            idErrorMessage = "",
            passwordErrorMessage = ""
        )
    }
}

sealed interface SignInUiAction : UiAction
sealed interface SignInUiEffect : UiEffect
