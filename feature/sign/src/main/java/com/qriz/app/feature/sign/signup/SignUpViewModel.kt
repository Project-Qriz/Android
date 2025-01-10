package com.qriz.app.feature.sign.signup

import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.qriz.app.feature.base.BaseViewModel
import com.qriz.app.feature.sign.R
import com.qriz.app.feature.sign.signup.SignUpUiState.AuthenticationState
import com.qriz.app.feature.sign.signup.SignUpUiState.SignUpPage
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel<SignUpUiState, SignUpUiEffect, SignUpUiAction>(SignUpUiState.Default) {

    override fun process(action: SignUpUiAction): Job = viewModelScope.launch {
        when (action) {
            is SignUpUiAction.ChangeUserName -> onUserNameChanged(action.name)
            is SignUpUiAction.ChangeUserId -> onUserIdChanged(action.id)
            is SignUpUiAction.ChangeUserPw -> onUserPwChanged(action.pw)
            is SignUpUiAction.ChangeUserPwCheck -> onUserPwCheckChanged(action.pw)
            is SignUpUiAction.ChangeEmail -> onEmailChanged(action.email)
            is SignUpUiAction.ChangeEmailAuthNum -> onEmailAuthNumChanged(action.authNum)
            SignUpUiAction.ClickPreviousPage -> onClickPreviousPage()
            SignUpUiAction.ClickNextPage -> onClickNextPage()
            SignUpUiAction.ClickEmailAuthNumSend -> onClickEmailAuthNumSend()
            SignUpUiAction.ClickIdDuplicateCheck -> onClickIdDuplicateCheck()
            SignUpUiAction.ClickSignUp -> onClickSignUp()
        }
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

    private fun onUserNameChanged(name: String) { //TODO : 테스트 필요
        val nameErrorMessage =
            if (name.length > 2) R.string.empty else uiState.value.nameErrorMessageResId

        updateState {
            copy(
                name = name,
                nameErrorMessageResId = nameErrorMessage,
            )
        }
    }

    private fun onEmailChanged(email: String) { //TODO : 테스트 필요
//        val emailErrorMessage = if (verifyEmail(email)) R.string.empty
//        else uiState.value.emailErrorMessage

        updateState {
            copy(
                email = email,
                emailErrorMessageResId = R.string.empty,
            )
        }
    }

    private fun onEmailAuthNumChanged(authNum: String) { //TODO : 테스트 필요
        updateState { copy(emailAuthNumber = authNum) }

        if (authNum.length == AUTHENTICATION_NUMBER_LENGTH) {
            verifyAuthenticationNumber()
        }
    }

    private fun onUserIdChanged(id: String) {  //TODO : 테스트 필요
        updateState {
            copy(
                id = id,
                isAvailableId = false,
            )
        }
    }

    private fun onUserPwChanged(password: String) {  //TODO : 테스트 필요
        val errorMessage = if (passwordPattern.matcher(password).matches()) R.string.empty
        else R.string.password_cannot_be_used

        val passwordCheck = uiState.value.pwCheck
        val passwordCheckErrorMessage = if (password == passwordCheck) R.string.empty
        else R.string.password_is_incorrect


        updateState {
            copy(
                pw = password,
                pwErrorMessageResId = errorMessage,
                pwCheckErrorMessageResId = passwordCheckErrorMessage,
            )
        }
    }

    private fun onUserPwCheckChanged(passwordCheck: String) {   //TODO : 테스트 필요
        val password = uiState.value.pw
        val errorMessage = if (password == passwordCheck) R.string.empty
        else R.string.password_is_incorrect


        updateState {
            copy(
                pwCheck = passwordCheck,
                pwCheckErrorMessageResId = errorMessage,
            )
        }
    }

    private fun onClickNextPage() { //TODO : 테스트 필요
        val currentUiState = uiState.value
        if (currentUiState.name.length < 2) {
            updateState { copy(nameErrorMessageResId = R.string.user_name_is_short) }
            return
        }
        if (currentUiState.page == SignUpPage.EMAIL) sendAuthenticationNumberEmail()
        if (currentUiState.page == SignUpPage.EMAIL_AUTH) cancelAuthTimer()

        updateState { copy(page = currentUiState.page.next) }
    }


    private fun onClickPreviousPage() { //TODO : 테스트 필요
        if (uiState.value.page == SignUpPage.NAME) return
        updateState { copy(page = uiState.value.page.previous) }
    }

    private fun onClickEmailAuthNumSend() { //TODO : 테스트 필요
        sendAuthenticationNumberEmail()
    }

    private fun onClickSignUp() {
        signUp()
    }

    private fun onClickIdDuplicateCheck() { //TODO : 테스트 필요
        checkDuplicateId()
    }

    private fun sendAuthenticationNumberEmail() { //TODO : 테스트 필요
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
        val authenticationNumber = uiState.value.emailAuthNumber

        viewModelScope.launch {
            runCatching { userRepository.verifyAuthenticationNumber(authenticationNumber) }
                .onSuccess { updateState { copy(emailAuthState = AuthenticationState.Verified) } }
                .onFailure {
                    updateState {
                        copy(
                            emailAuthState = AuthenticationState.Unverified,
                            emailAuthNumberErrorMessageResId = R.string.email_auth_num_is_different
                        )
                    }
                }
        }
    }

    private fun checkDuplicateId() {
        val id = uiState.value.id

        if (id.isEmpty()) {
            updateState { copy(idErrorMessageResId = R.string.please_enter_id) }
            return
        }

        viewModelScope.launch {
            runCatching { userRepository.checkDuplicateId(uiState.value.id) }
                .onSuccess { isAvailable ->
                    updateState {
                        copy(
                            isAvailableId = isAvailable,
                            idErrorMessageResId = R.string.empty
                        )
                    }
                }.onFailure { t ->
                    updateState { copy(idErrorMessageResId = R.string.id_duplication_check_failed) }
                    sendEffect(
                        SignUpUiEffect.ShowSnackBer(
                            defaultResId = R.string.id_duplication_check_failed,
                            message = t.message
                        )
                    )
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
        updateState { copy(timer = AUTHENTICATION_LIMIT_MILS) }
    }

    companion object {
        const val AUTHENTICATION_NUMBER_LENGTH = 6
        val AUTHENTICATION_LIMIT_MILS = 3.minutes.inWholeMilliseconds
    }
}
