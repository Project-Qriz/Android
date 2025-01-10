package com.qriz.app.feature.sign.signUp

import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.qriz.app.feature.base.BaseViewModel
import com.qriz.app.feature.sign.R
import com.qriz.app.feature.sign.signUp.SignUpUiState.AuthenticationState
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel<SignUpUiState, SignUpUiEffect, SignUpUiAction>(SignUpUiState.Default) {

    override fun process(action: SignUpUiAction): Job {
        TODO("Not yet implemented")
    }

    private val timer: Flow<Long>
        get() = flow {
            var current = 0L
            while (true) {
                emit(AUTHENTICATION_LIMIT_MILS - current)
                delay(1000)
                current += 1000
                if (current >= AUTHENTICATION_LIMIT_MILS) {
                    break
                }
            }
        }

    private var timerJob: Job? = null

    private val passwordPattern = Pattern.compile("^[a-zA-Z0-9]{8,10}$")

    fun updateName(name: String) {
        val nameErrorMessage = if (name.length > 2) "" else uiState.value.nameErrorMessage

        updateState {
            copy(
                name = name,
                nameErrorMessage = nameErrorMessage,
            )
        }
    }

    fun updateEmail(email: String) {
        val emailErrorMessage = if (verifyEmail(email)) "" else uiState.value.emailErrorMessage

        updateState {
            copy(
                email = email,
                emailErrorMessage = emailErrorMessage,
            )
        }
    }

    fun updateAuthenticationNumber(authenticationNumber: String) {
        updateState { copy(authenticationNumber = authenticationNumber) }

        if (authenticationNumber.length == AUTHENTICATION_NUMBER_LENGTH) {
            verifyAuthenticationNumber()
        }
    }

    fun updateId(id: String) {
        updateState {
            copy(
                id = id,
                isAvailableId = false,
            )
        }
    }

    fun updatePassword(password: String) {
        val errorMessage = if (passwordPattern.matcher(password).matches()) {
            ""
        } else {
            "사용할 수 없는 비밀번호 입니다."
        }

        val passwordCheck = uiState.value.passwordCheck
        val passwordCheckErrorMessage = if (password == passwordCheck) {
            ""
        } else {
            "비밀번호가 맞지 않아요"
        }

        updateState {
            copy(
                password = password,
                passwordErrorMessage = errorMessage,
                passwordCheckErrorMessage = passwordCheckErrorMessage,
            )
        }
    }

    fun updatePasswordCheck(passwordCheck: String) {
        val password = uiState.value.password
        val errorMessage = if (password == passwordCheck) {
            ""
        } else {
            "비밀번호가 맞지 않아요"
        }

        updateState {
            copy(
                passwordCheck = passwordCheck,
                passwordCheckErrorMessage = errorMessage,
            )
        }
    }

    fun nextPage() {
        val currentUiState = uiState.value
        if (currentUiState.name.length < 2) {
            updateState { copy(nameErrorMessage = "이름은 두글자 이상입니다") }
            return
        }

        if (currentUiState.page == EMAIL_PAGE) {
            sendAuthenticationNumberEmail()
        }

        if (currentUiState.page == AUTHENTICATION_PAGE) {
            cancelAuthTimer()
        }

        updateState { copy(page = currentUiState.page + 1) }
    }

    fun previousPage() {
        val currentUiState = uiState.value
        if (currentUiState.page == NAME_PAGE) return

        updateState { copy(page = uiState.value.page - 1) }
    }

    fun sendAuthenticationNumberEmail() {
        val email = uiState.value.email

        viewModelScope.launch {
            cancelAuthTimer()
            userRepository.sendAuthenticationNumber(email)
            sendEffect(SignUpUiEffect.ShowSnackBer(R.string.email_auth_sent))
            startAuthTimer()
        }
    }

    fun signUp() {
        //TODO: 추후 실제 연결
//        val uiState = state.value
//        viewModelScope.launch {
//            userRepository.signUp(
//                loginId = state.id,
//                password = state.password,
//                nickname = state.name,
//                email = state.email,
//            )
//        }

        sendEffect(SignUpUiEffect.SignUpUiComplete)
    }

    private fun verifyEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun verifyAuthenticationNumber() {
        val authenticationNumber = uiState.value.authenticationNumber

        viewModelScope.launch {
            runCatching {
                userRepository.verifyAuthenticationNumber(authenticationNumber)
            }.onSuccess {
                updateState {
                    copy(
                        authenticationState = AuthenticationState.Verified
                    )
                }
            }.onFailure {
                updateState {
                    copy(
                        authenticationState = AuthenticationState.Unverified,
                        authenticationNumberErrorMessage = "인증번호가 다르게 입력되었어요"
                    )
                }
            }
        }
    }

    fun checkDuplicateId() {
        val id = uiState.value.id

        if (id.isEmpty()) {
            updateState { copy(idErrorMessage = "아이디를 입력해주세요") }
            return
        }

        viewModelScope.launch {
            runCatching {
                userRepository.checkDuplicateId(uiState.value.id)
            }.onSuccess { isAvailable ->
                updateState {
                    copy(
                        isAvailableId = isAvailable,
                        idErrorMessage = ""
                    )
                }
            }.onFailure { t ->
                updateState {
                    copy(
                        idErrorMessage = t.message ?: "알 수 없는 오류가 발생했습니다."
                    )
                }
            }
        }
    }

    private fun startAuthTimer() {
        timerJob = viewModelScope.launch {
            timer.collect { seconds ->
                updateState { copy(timer = seconds) }
            }
        }
    }

    private fun cancelAuthTimer() {
        timerJob?.cancel()
        timerJob = null
        updateState {
            copy(timer = AUTHENTICATION_LIMIT_MILS)
        }
    }

    companion object {
        const val NAME_PAGE = 0
        const val EMAIL_PAGE = 1
        const val AUTHENTICATION_PAGE = 2
        const val ID_PAGE = 3
        const val PASSWORD_PAGE = 4

        const val AUTHENTICATION_NUMBER_LENGTH = 6

        const val AUTHENTICATION_LIMIT_MILS = 180000L
    }
}
