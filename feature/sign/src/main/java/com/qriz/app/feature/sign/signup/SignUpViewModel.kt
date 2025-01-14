package com.qriz.app.feature.sign.signup

import androidx.lifecycle.viewModelScope
import com.qriz.app.feature.base.BaseViewModel
import com.qriz.app.feature.sign.R
import com.qriz.app.feature.sign.signup.SignUpUiState.AuthenticationState
import com.qriz.app.feature.sign.signup.SignUpUiState.Companion.EMAIL_REGEX
import com.qriz.app.feature.sign.signup.SignUpUiState.Companion.ID_REGEX
import com.qriz.app.feature.sign.signup.SignUpUiState.Companion.PW_REGEX
import com.qriz.app.feature.sign.signup.SignUpUiState.SignUpPage
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

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

    private var timerJob: Job? = null

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

    private fun onEmailChanged(email: String) {
        val emailErrorMessageResId = when {
            email.isBlank() || EMAIL_REGEX.matches(email) -> R.string.empty
            else -> R.string.please_check_email_again
        }
        updateState {
            copy(
                email = email,
                emailErrorMessageResId = emailErrorMessageResId,
            )
        }
    }

    private fun onEmailAuthNumChanged(authNum: String) {
        when (uiState.value.emailAuthState) {
            AuthenticationState.VERIFIED -> return
            AuthenticationState.TIME_EXPIRED -> {
                updateState {
                    copy(
                        emailAuthNumber = authNum,
                        emailAuthState = AuthenticationState.TIME_EXPIRED,
                        emailAuthNumberErrorMessageResId = R.string.email_auth_time_has_expired
                    )
                }
            }

            AuthenticationState.SEND_FAILED -> {
                updateState {
                    copy(
                        emailAuthNumber = authNum,
                        emailAuthState = AuthenticationState.SEND_FAILED,
                        emailAuthNumberErrorMessageResId = R.string.email_auth_sent_fail
                    )
                }
            }

            AuthenticationState.NONE,
            AuthenticationState.UNVERIFIED -> {
                updateState {
                    copy(
                        emailAuthNumber = authNum,
                        emailAuthState = AuthenticationState.NONE,
                        emailAuthNumberErrorMessageResId = R.string.empty
                    )
                }
                if (authNum.length == EMAIL_AUTH_NUMBER_LENGTH) verifyAuthenticationNumber()
            }
        }
    }

    private fun onUserIdChanged(id: String) {
        //TODO : 아이디 생성 조건 노출 필요 (디자인 수정 대기중)
        val idErrorMessageResId =
            if (ID_REGEX.matches(id)) R.string.empty
            else R.string.id_cannot_be_used
        updateState {
            copy(
                id = id,
                isNotDuplicatedId = false,
                idErrorMessageResId = idErrorMessageResId
            )
        }
    }

    private fun onUserPwChanged(pw: String) {
        val pwErrorMessageResId =
            if (PW_REGEX.matches(pw)) R.string.empty
            //TODO : 경고 문구 UI 수정 대기 중
            else R.string.pw_warning

        val pwCheckErrorMessageResId =
            if (pw == uiState.value.pwCheck) R.string.empty
            else R.string.password_is_incorrect

        updateState {
            copy(
                pw = pw,
                pwErrorMessageResId = pwErrorMessageResId,
                pwCheckErrorMessageResId = pwCheckErrorMessageResId,
            )
        }
    }

    private fun onUserPwCheckChanged(passwordCheck: String) {
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

    private fun onClickNextPage() {
        val currentUiState = uiState.value
        when {
            currentUiState.name.length < 2 -> {
                updateState { copy(nameErrorMessageResId = R.string.user_name_is_short) }
            }

            currentUiState.page == SignUpPage.EMAIL -> {
                if (timerJob == null || timerJob?.isCompleted == true) sendEmailAuthNumber()
            }

            currentUiState.page == SignUpPage.ID -> cancelEmailAuthTimer()
        }
        updateState { copy(page = currentUiState.page.next) }
    }


    private fun onClickPreviousPage() {
        if (uiState.value.page == SignUpPage.NAME) return
        updateState { copy(page = uiState.value.page.previous) }
    }

    private fun onClickEmailAuthNumSend() {
        sendEmailAuthNumber()
    }

    private fun onClickSignUp() {
        signUp()
    }

    private fun onClickIdDuplicateCheck() {
        checkDuplicateId()
    }

    private fun sendEmailAuthNumber() = viewModelScope.launch {
        if (uiState.value.isVerifiedEmailAuth) return@launch
        cancelEmailAuthTimer()
        updateState {
            copy(
                emailAuthNumber = "",
                emailAuthState = AuthenticationState.NONE,
                emailAuthNumberErrorMessageResId = R.string.empty
            )
        }
        runCatching { userRepository.sendAuthenticationNumber(uiState.value.email) }
            .onSuccess {
                sendEffect(SignUpUiEffect.ShowSnackBer(R.string.email_auth_sent))
                startEmailAuthTimer()
            }
            .onFailure {
                updateState {
                    copy(
                        emailAuthState = AuthenticationState.SEND_FAILED,
                        emailAuthNumberErrorMessageResId = R.string.email_auth_sent_fail
                    )
                }
            }
    }

    private fun signUp() = viewModelScope.launch {
        runCatching {
//            userRepository.signUp(
//                loginId = uiState.value.id,
//                password = uiState.value.pw,
//                nickname = uiState.value.name,
//                email = uiState.value.email,
//            )
        }
            .onSuccess { sendEffect(SignUpUiEffect.SignUpUiComplete) }
            .onFailure {
                //TODO : "네트워크 오류 발생" 혹은 "서버 오류 발생" 모달 다이얼로그 노출
            }
    }


    //TODO : 로딩 UI 추가되어야함
    private fun verifyAuthenticationNumber() {
        if (uiState.value.isSendFailedEmailAuth) return
        val authenticationNumber = uiState.value.emailAuthNumber

        viewModelScope.launch {
            runCatching { userRepository.verifyAuthenticationNumber(authenticationNumber) }
                .onSuccess { isVerified ->
                    if (isVerified) {
                        updateState { copy(emailAuthState = AuthenticationState.VERIFIED) }
                        cancelEmailAuthTimer()
                    } else {
                        updateState {
                            copy(
                                emailAuthState = AuthenticationState.UNVERIFIED,
                                emailAuthNumberErrorMessageResId = R.string.email_auth_num_is_different
                            )
                        }
                    }
                }
                .onFailure {
                    //TODO : 재시도 가능하게 UI 구현되어야함
                    // API 실패 유저에게 전달
                    // "네트워크 오류 발생" 혹은 "서버 오류 발생" 모달 다이얼로그 노출
                }
        }
    }

    private fun checkDuplicateId() {
        if (uiState.value.isNotDuplicatedId) return

        if (uiState.value.id.isEmpty()) {
            updateState { copy(idErrorMessageResId = R.string.please_enter_id) }
            return
        }

        if (ID_REGEX.matches(uiState.value.id).not()) return

        viewModelScope.launch {
            runCatching { userRepository.isNotDuplicateId(uiState.value.id) }
                .onSuccess { isNotDuplicated ->
                    val idErrorMessageResId =
                        if (isNotDuplicated) R.string.empty else R.string.id_cannot_be_used
                    updateState {
                        copy(
                            isNotDuplicatedId = isNotDuplicated,
                            idErrorMessageResId = idErrorMessageResId
                        )
                    }
                }.onFailure { t ->
                    //TODO : "네트워크 오류 발생" 혹은 "서버 오류 발생" 모달 다이얼로그 노출
                    // SignUpUiEffect.ShowErrorDialog(재시도 해야하는 action 전달)
                    // 다이얼로그 확인 누르면 재시도 혹은 확인 버튼 = 재시도 버튼
                    sendEffect(
                        SignUpUiEffect.ShowSnackBer(
                            defaultResId = R.string.id_duplication_check_failed,
                            message = t.message
                        )
                    )
                }
        }
    }

    private fun startEmailAuthTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var currentTime = AUTHENTICATION_LIMIT_MILS
            val interval = 1.seconds.inWholeMilliseconds
            while (true) {
                if (currentTime <= 0L) {
                    updateState {
                        copy(
                            emailAuthState = AuthenticationState.TIME_EXPIRED,
                            emailAuthNumberErrorMessageResId = R.string.email_auth_time_has_expired
                        )
                    }
                    break
                }
                delay(interval)
                currentTime -= interval
                updateState { copy(timer = currentTime) }
            }
        }
    }

    private fun cancelEmailAuthTimer() {
        timerJob?.cancel()
        timerJob = null
        updateState { copy(timer = AUTHENTICATION_LIMIT_MILS) }
    }

    companion object {
        const val EMAIL_AUTH_NUMBER_LENGTH = 6
        val AUTHENTICATION_LIMIT_MILS = 3.minutes.inWholeMilliseconds
    }
}
