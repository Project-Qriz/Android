package com.qriz.app.feature.sign.model

import android.util.Patterns
import androidx.compose.runtime.Immutable
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
) {
    val emailVerified: Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    val topBarTitle: String = when (page) {
        0 -> "이름 입력"
        1, 2 -> "이메일 인증"
        3, 4 -> "회원가입"
        else -> throw IllegalStateException("잘못된 페이지입니다.")
    }

    val timerText: String = "${(timer / 60000)}:${(timer % 60000 / 1000).toString().padStart(2, '0')}"

    val canSignUp: Boolean = Pattern.compile("^[a-zA-Z0-9]{8,10}$").matcher(password).matches()
            && password == passwordCheck

    val validName: Boolean = """[가-힣]{2,}""".toRegex().matches(name)
}

enum class AuthenticationState {
    None, Verified, Unverified;
}
