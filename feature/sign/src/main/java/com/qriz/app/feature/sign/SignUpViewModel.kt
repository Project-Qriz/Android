package com.qriz.app.feature.sign

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qriz.app.core.data.repository.UserRepository
import com.qriz.app.feature.sign.model.AuthenticationState
import com.qriz.app.feature.sign.model.SignUpEffect
import com.qriz.app.feature.sign.model.SignUpUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _state: MutableStateFlow<SignUpUiState> = MutableStateFlow(
        SignUpUiState(
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
    )

    val state: StateFlow<SignUpUiState> = _state.asStateFlow()

    private val _effect: MutableSharedFlow<SignUpEffect> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val effect: SharedFlow<SignUpEffect> = _effect.asSharedFlow()

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
        val nameErrorMessage = if (name.length > 2) "" else state.value.nameErrorMessage

        _state.update {
            it.copy(
                name = name,
                nameErrorMessage = nameErrorMessage,
            )
        }
    }

    fun updateEmail(email: String) {
        val emailErrorMessage = if (verifyEmail(email)) "" else state.value.emailErrorMessage

        _state.update {
            it.copy(
                email = email,
                emailErrorMessage = emailErrorMessage,
            )
        }
    }

    fun updateAuthenticationNumber(authenticationNumber: String) {
        _state.update {
            it.copy(
                authenticationNumber = authenticationNumber,
            )
        }

        if (authenticationNumber.length == AUTHENTICATION_NUMBER_LENGTH) {
            verifyAuthenticationNumber()
        }
    }

    fun updateId(id: String) {
        _state.update {
            it.copy(
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

        val passwordCheck = state.value.passwordCheck
        val passwordCheckErrorMessage = if (password == passwordCheck) {
            ""
        } else {
            "비밀번호가 맞지 않아요"
        }

        _state.update {
            it.copy(
                password = password,
                passwordErrorMessage = errorMessage,
                passwordCheckErrorMessage = passwordCheckErrorMessage,
            )
        }
    }

    fun updatePasswordCheck(passwordCheck: String) {
        val password = state.value.password
        val errorMessage = if (password == passwordCheck) {
            ""
        } else {
            "비밀번호가 맞지 않아요"
        }

        _state.update {
            it.copy(
                passwordCheck = passwordCheck,
                passwordCheckErrorMessage = errorMessage,
            )
        }
    }

    fun nextPage() {
        val currentState = state.value
        if (currentState.name.length < 2) {
            _state.update { it.copy(nameErrorMessage = "이름은 두글자 이상입니다") }
            return
        }

        if (currentState.page == EMAIL_PAGE) {
            sendAuthenticationNumberEmail()
        }

        if (currentState.page == AUTHENTICATION_PAGE) {
            cancelAuthTimer()
        }

        _state.update { it.copy(page = it.page + 1) }
    }

    fun previousPage() {
        val currentState = state.value
        if (currentState.page == NAME_PAGE) return

        _state.update { it.copy(page = it.page - 1) }
    }

    fun sendAuthenticationNumberEmail() {
        val email = state.value.email

        viewModelScope.launch {
            cancelAuthTimer()
            userRepository.sendAuthenticationNumber(email)
            startAuthTimer()
        }
    }

    fun signUp() {
        //TODO: 추후 실제 연결
//        val state = state.value
//        viewModelScope.launch {
//            userRepository.signUp(
//                loginId = state.id,
//                password = state.password,
//                nickname = state.name,
//                email = state.email,
//            )
//        }

        _effect.tryEmit(SignUpEffect.SignUpComplete)
    }

    private fun verifyEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun verifyAuthenticationNumber() {
        val authenticationNumber = state.value.authenticationNumber

        viewModelScope.launch {
            runCatching {
                userRepository.verifyAuthenticationNumber(authenticationNumber)
            }.onSuccess {
                _state.update {
                    it.copy(
                        authenticationState = AuthenticationState.Verified
                    )
                }
            }.onFailure {
                _state.update {
                    it.copy(
                        authenticationState = AuthenticationState.Unverified,
                        authenticationNumberErrorMessage = "인증번호가 다르게 입력되었어요"
                    )
                }
            }
        }
    }

    fun checkDuplicateId() {
        val id = state.value.id

        if (id.isEmpty()) {
            _state.update {
                it.copy(
                    idErrorMessage = "아이디를 입력해주세요"
                )
            }
            return
        }

        viewModelScope.launch {
            runCatching {
                userRepository.checkDuplicateId(state.value.id)
            }.onSuccess { isAvailable ->
                _state.update {
                    it.copy(
                        isAvailableId = isAvailable,
                        idErrorMessage = ""
                    )
                }
            }.onFailure { t ->
                _state.update {
                    it.copy(
                        idErrorMessage = t.message ?: "알 수 없는 오류가 발생했습니다."
                    )
                }
            }
        }
    }

    private fun startAuthTimer() {
        timerJob = viewModelScope.launch {
            timer.collect { seconds ->
                _state.update { it.copy(timer = seconds) }
            }
        }
    }

    private fun cancelAuthTimer() {
        timerJob?.cancel()
        timerJob = null
        _state.update {
            it.copy(timer = AUTHENTICATION_LIMIT_MILS)
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
