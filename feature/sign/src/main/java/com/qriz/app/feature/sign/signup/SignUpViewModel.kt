package com.qriz.app.feature.sign.signup

import androidx.annotation.VisibleForTesting
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
open class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel<SignUpUiState, SignUpUiEffect, SignUpUiAction>(SignUpUiState.Default) {

    final override fun process(action: SignUpUiAction): Job = viewModelScope.launch {
        when (action) {
            is SignUpUiAction.ChangeUserName -> onChangeUserName(action.name)
            is SignUpUiAction.ChangeEmail -> onChangeEmail(action.email)
            is SignUpUiAction.ChangeUserId -> onChangeUserId(action.id)
            is SignUpUiAction.ChangeUserPw -> onChangeUserPw(action.pw)
            is SignUpUiAction.ChangeUserPwCheck -> onChangeUserPwCheck(action.pw)
            is SignUpUiAction.ChangeEmailAuthNum -> onChangeEmailAuthNum(action.authNum)
            SignUpUiAction.ClickPreviousPage -> onClickPreviousPage()
            SignUpUiAction.ClickNextPage -> onClickNextPage()
            SignUpUiAction.ClickEmailAuthNumSend -> onClickEmailAuthNumSend()
            SignUpUiAction.ClickIdDuplicateCheck -> onClickIdDuplicateCheck()
            SignUpUiAction.ClickSignUp -> onClickSignUp()
            SignUpUiAction.RequestEmailAuthNumber -> requestEmailAuthNumber()
            SignUpUiAction.StartEmailAuthTimer -> startEmailAuthTimer()
        }
    }

    @VisibleForTesting
    var timerJob: Job? = null

    private fun onChangeUserName(name: String) {
        updateState { copy(name = name) }
    }

    private fun onChangeEmail(email: String) {
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

    private fun onChangeEmailAuthNum(authNum: String) {
        when (uiState.value.emailAuthState) {
            AuthenticationState.VERIFIED -> return

            AuthenticationState.TIME_EXPIRED,
            AuthenticationState.SEND_FAILED -> updateState { copy(emailAuthNumber = authNum) }

            AuthenticationState.NONE,
            AuthenticationState.REJECTED -> {
                updateState {
                    copy(
                        emailAuthNumber = authNum,
                        emailAuthState = AuthenticationState.NONE,
                        emailAuthNumberErrorMessageResId = R.string.empty
                    )
                }
                if (authNum.length == EMAIL_AUTH_NUMBER_LENGTH) verifyEmailAuthNumber()
            }
        }
    }

    private fun onChangeUserId(id: String) {
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

    private fun onChangeUserPw(pw: String) {
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

    private fun onChangeUserPwCheck(passwordCheck: String) {
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
        if (currentUiState.page == SignUpPage.EMAIL_AUTH) cancelEmailAuthTimer()
        updateState { copy(page = currentUiState.page.next) }
    }

    private fun onClickPreviousPage() {
        if (uiState.value.page == SignUpPage.NAME) {
            sendEffect(SignUpUiEffect.MoveToBack)
            return
        }
        updateState { copy(page = uiState.value.page.previous) }
    }

    private fun onClickEmailAuthNumSend() {
        requestEmailAuthNumber()
    }

    private fun onClickSignUp() {
        signUp()
    }

    private fun onClickIdDuplicateCheck() {
        checkDuplicateId()
    }

    private fun requestEmailAuthNumber() = viewModelScope.launch {
        if (uiState.value.isVerifiedEmailAuth) return@launch
        cancelEmailAuthTimer()
        updateState {
            copy(
                emailAuthNumber = "",
                emailAuthState = AuthenticationState.NONE,
                emailAuthNumberErrorMessageResId = R.string.empty
            )
        }
        runCatching { userRepository.requestEmailAuthNumber(uiState.value.email) }
            .onSuccess {
                sendEffect(SignUpUiEffect.ShowSnackBer(R.string.email_auth_sent))
                process(SignUpUiAction.StartEmailAuthTimer)
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
                //TODO : 재시도 가능하게 UI 구현되어야함 혹은 API 실패 유저에게 전달
                // "네트워크 오류 발생" 혹은 "서버 오류 발생" 모달 다이얼로그 노출
                // 수정 후 테스트 케이스 추가
            }
    }


    //TODO : 로딩 UI 추가되어야함
    private fun verifyEmailAuthNumber() {
        if (uiState.value.isSendFailedEmailAuth) return
        val authenticationNumber = uiState.value.emailAuthNumber

        viewModelScope.launch {
            runCatching { userRepository.verifyEmailAuthNumber(authenticationNumber) }
                .onSuccess { isVerified ->
                    if (isVerified) {
                        updateState { copy(emailAuthState = AuthenticationState.VERIFIED) }
                        cancelEmailAuthTimer()
                    } else {
                        updateState {
                            copy(
                                emailAuthState = AuthenticationState.REJECTED,
                                emailAuthNumberErrorMessageResId = R.string.email_auth_num_is_different
                            )
                        }
                    }
                }
                .onFailure {
                    //TODO : 재시도 가능하게 UI 구현되어야함 혹은 API 실패 유저에게 전달
                    // "네트워크 오류 발생" 혹은 "서버 오류 발생" 모달 다이얼로그 노출
                    // 수정 후 테스트 케이스 추가
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
                    //TODO : 재시도 가능하게 UI 구현되어야함 혹은 API 실패 유저에게 전달
                    // "네트워크 오류 발생" 혹은 "서버 오류 발생" 모달 다이얼로그 노출
                    // 수정 후 테스트 케이스 추가
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
                updateState { copy(emailAuthTime = currentTime) }
            }
        }
    }

    private fun cancelEmailAuthTimer() {
        timerJob?.cancel()
        timerJob = null
        updateState { copy(emailAuthTime = AUTHENTICATION_LIMIT_MILS) }
    }

    companion object {
        const val EMAIL_AUTH_NUMBER_LENGTH = 6
        val AUTHENTICATION_LIMIT_MILS = 3.minutes.inWholeMilliseconds
    }
}
