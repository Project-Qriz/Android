package com.qriz.app.feature.sign.signup

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.viewModelScope
import com.qriz.app.feature.base.BaseViewModel
import com.qriz.app.feature.sign.R
import com.qriz.app.feature.sign.signup.SignUpUiState.AuthenticationState
import com.qriz.app.feature.sign.signup.SignUpUiState.SignUpPage
import com.quiz.app.core.data.user.user_api.model.AUTH_NUMBER_MAX_LENGTH
import com.quiz.app.core.data.user.user_api.model.EMAIL_REGEX
import com.quiz.app.core.data.user.user_api.model.ID_REGEX
import com.quiz.app.core.data.user.user_api.model.PW_REGEX
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

    @VisibleForTesting
    var timerJob: Job? = null

    final override fun process(action: SignUpUiAction): Job = viewModelScope.launch {
        when (action) {
            is SignUpUiAction.ChangeUserName -> onChangeUserName(action.name)
            is SignUpUiAction.ChangeEmail -> onChangeEmail(action.email)
            is SignUpUiAction.ChangeUserId -> onChangeUserId(action.id)
            is SignUpUiAction.ChangeUserPw -> onChangeUserPw(action.pw)
            is SignUpUiAction.ChangeUserPwCheck -> onChangeUserPwCheck(action.pw)
            is SignUpUiAction.ChangeEmailAuthNum -> onChangeEmailAuthNum(action.authNum)
            is SignUpUiAction.ChangeFocusState -> changeFocus(action.focusState)
            is SignUpUiAction.ClickPreviousPage -> onClickPreviousPage()
            is SignUpUiAction.ClickNextPage -> onClickNextPage()
            is SignUpUiAction.ClickEmailAuthNumSend -> onClickEmailAuthNumSend()
            is SignUpUiAction.ClickIdDuplicateCheck -> onClickIdDuplicateCheck()
            is SignUpUiAction.ClickSignUp -> onClickSignUp()
            is SignUpUiAction.RequestEmailAuthNumber -> requestEmailAuthNumber()
            is SignUpUiAction.StartEmailAuthTimer -> startEmailAuthTimer()
            is SignUpUiAction.ClickVerifyAuthNum -> verifyEmailAuthNumber()
        }
    }

    /*
    * ******************************************
    * EMAIL AUTHENTICATION
    * ******************************************
    */
    private fun onChangeEmail(email: String) {
        updateState {
            copy(email = email)
        }
    }

    private fun onChangeEmailAuthNum(authNum: String) {
        when (uiState.value.emailAuthState) {
            AuthenticationState.TIME_EXPIRED, AuthenticationState.VERIFIED -> return

            AuthenticationState.SEND_SUCCESS, AuthenticationState.SEND_FAILED -> updateState { copy(emailAuthNumber = authNum) }

            AuthenticationState.NONE, AuthenticationState.REJECTED -> {
                updateState {
                    copy(
                        emailAuthNumber = authNum,
                        emailAuthState = AuthenticationState.NONE,
                        authNumberSupportingTextResId = R.string.empty
                    )
                }
            }
        }
    }

    private fun onClickEmailAuthNumSend() {
        val email = uiState.value.email

        if (email.isBlank()) {
            updateState {
                copy(
                    emailSupportingTextResId = R.string.email_is_empty
                )
            }
            return
        }

        if (EMAIL_REGEX.matches(email).not()) {
            updateState {
                copy(
                    emailSupportingTextResId = R.string.email_is_invalid_format
                )
            }
            return
        }

        requestEmailAuthNumber()
    }

    private fun requestEmailAuthNumber() = viewModelScope.launch {
        if (uiState.value.isVerifiedEmailAuth) return@launch
        cancelEmailAuthTimer()
        updateState {
            copy(
                emailAuthNumber = "",
                emailAuthState = AuthenticationState.NONE,
                authNumberSupportingTextResId = R.string.empty
            )
        }
        runCatching { userRepository.requestEmailAuthNumber(uiState.value.email) }.onSuccess {
            sendEffect(SignUpUiEffect.ShowSnackBer(R.string.email_auth_sent))
            updateState { copy(emailAuthState = AuthenticationState.SEND_SUCCESS) }
            process(SignUpUiAction.StartEmailAuthTimer)
        }.onFailure {
            updateState {
                copy(
                    emailAuthState = AuthenticationState.SEND_FAILED,
                    authNumberSupportingTextResId = R.string.email_auth_sent_fail
                )
            }
        }
    }

    //TODO : 로딩 UI 추가되어야함
    private fun verifyEmailAuthNumber() {
        if (uiState.value.isSendFailedEmailAuth) return

        val authenticationNumber = uiState.value.emailAuthNumber

        viewModelScope.launch {
            runCatching { userRepository.verifyEmailAuthNumber(authenticationNumber) }.onSuccess { isVerified ->
                if (isVerified) {
                    updateState {
                        copy(
                            emailAuthState = AuthenticationState.VERIFIED,
                            authNumberSupportingTextResId = R.string.success_verify_auth_number,
                        )
                    }
                    cancelEmailAuthTimer()
                } else {
                    updateState {
                        copy(
                            emailAuthState = AuthenticationState.REJECTED,
                            authNumberSupportingTextResId = R.string.email_auth_num_is_different
                        )
                    }
                }
            }.onFailure {
                //TODO : 재시도 가능하게 UI 구현되어야함 혹은 API 실패 유저에게 전달
                // "네트워크 오류 발생" 혹은 "서버 오류 발생" 모달 다이얼로그 노출
                // 수정 후 테스트 케이스 추가
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
                            authNumberSupportingTextResId = R.string.email_auth_time_has_expired
                        )
                    }
                    break
                }
                delay(interval)
                currentTime -= interval
                updateState { copy(emailAuthTime = currentTime) }
            }

            updateState {
                copy(
                    emailAuthState = AuthenticationState.TIME_EXPIRED,
                    authNumberSupportingTextResId = R.string.email_auth_time_has_expired
                )
            }
        }
    }

    private fun cancelEmailAuthTimer() {
        timerJob?.cancel()
        timerJob = null
        updateState { copy(emailAuthTime = AUTHENTICATION_LIMIT_MILS) }
    }

    /*
    * ******************************************
    * NAME
    * ******************************************
    */
    private fun onChangeUserName(name: String) {
        updateState { copy(name = name) }
    }

    /*
    * ******************************************
    * ID
    * ******************************************
    */
    private fun onChangeUserId(id: String) {
        //TODO : 아이디 생성 조건 노출 필요 (디자인 수정 대기중)
        val idErrorMessageResId = if (ID_REGEX.matches(id)) R.string.empty
        else R.string.id_cannot_be_used
        updateState {
            copy(
                id = id,
                isNotDuplicatedId = false,
                idErrorMessageResId = idErrorMessageResId
            )
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
            runCatching { userRepository.isNotDuplicateId(uiState.value.id) }.onSuccess { isNotDuplicated ->
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

    /*
    * ******************************************
    * PASSWORD
    * ******************************************
    */
    private fun onChangeUserPw(pw: String) {
        val pwErrorMessageResId = if (PW_REGEX.matches(pw)) R.string.empty
        //TODO : 경고 문구 UI 수정 대기 중
        else R.string.pw_warning

        val pwCheckErrorMessageResId = if (pw == uiState.value.pwCheck) R.string.empty
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

    /*
    * ******************************************
    * COMMON
    * ******************************************
    */
    private fun onClickNextPage() {
        val currentUiState = uiState.value
        if (currentUiState.page == SignUpPage.AUTH) cancelEmailAuthTimer()
        updateState { copy(page = currentUiState.page.next) }
    }

    private fun onClickPreviousPage() {
        if (uiState.value.page == SignUpPage.AUTH) {
            sendEffect(SignUpUiEffect.MoveToBack)
            return
        }
        updateState { copy(page = uiState.value.page.previous) }
    }

    private fun onClickSignUp() {
        signUp()
    }

    private fun onClickIdDuplicateCheck() {
        checkDuplicateId()
    }

    private fun changeFocus(focusState: SignUpUiState.FocusState) {
        updateState { copy(focusState = focusState) }
    }

    private fun signUp() = viewModelScope.launch {
        runCatching {
//            userRepository.signUp(
//                loginId = uiState.value.id,
//                password = uiState.value.pw,
//                nickname = uiState.value.name,
//                email = uiState.value.email,
//            )
        }.onSuccess { sendEffect(SignUpUiEffect.SignUpUiComplete) }.onFailure {
            //TODO : 재시도 가능하게 UI 구현되어야함 혹은 API 실패 유저에게 전달
            // "네트워크 오류 발생" 혹은 "서버 오류 발생" 모달 다이얼로그 노출
            // 수정 후 테스트 케이스 추가
        }
    }

    companion object {
        val AUTHENTICATION_LIMIT_MILS = 3.minutes.inWholeMilliseconds
    }
}
