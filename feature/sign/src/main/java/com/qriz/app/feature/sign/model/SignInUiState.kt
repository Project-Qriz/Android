package com.qriz.app.feature.sign.model

data class SignInUiState(
    val id: String,
    val password: String,
    val showPassword: Boolean,
    val idErrorMessage: String,
    val passwordErrorMessage: String,
)
