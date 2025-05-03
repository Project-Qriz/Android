package com.qriz.app.feature.sign.signup

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.viewModelScope
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.ui.common.resource.CHECK_NETWORK_AND_TRY_AGAIN
import com.qriz.app.core.ui.common.resource.CONTACT_DEVELOPER_IF_PERSISTS
import com.qriz.app.core.ui.common.resource.NETWORK_IS_UNSTABLE
import com.qriz.app.core.ui.common.resource.UNKNOWN_ERROR
import com.qriz.app.feature.base.BaseViewModel
import com.qriz.app.feature.sign.R
import com.qriz.app.feature.sign.signup.SignUpUiState.AuthenticationState
import com.qriz.app.feature.sign.signup.SignUpUiState.SignUpPage
import com.quiz.app.core.data.user.user_api.model.EMAIL_REGEX
import com.quiz.app.core.data.user.user_api.model.ID_REGEX
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

    private var timerJob: Job? = null

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
            is SignUpUiAction.StartEmailAuthTimer -> startEmailAuthTimer()
            is SignUpUiAction.ClickVerifyAuthNum -> verifyEmailAuthNumber()
            is SignUpUiAction.ChangePasswordVisibility -> updateState { copy(isVisiblePassword = action.isVisible) }
            is SignUpUiAction.ChangePasswordCheckVisibility -> updateState { copy(isVisiblePasswordCheck = action.isVisible) }
            is SignUpUiAction.DismissFailureDialog -> updateState { copy(failureDialogState = null) }
        }
    }

    /*
    * ******************************************
    * EMAIL AUTHENTICATION
    * ******************************************
    */
    private fun onChangeEmail(email: String) {
        updateState {
            copy(
                email = email,
                emailSupportingTextResId = if (EMAIL_REGEX.matches(email) || email.isEmpty()) R.string.empty
                else R.string.email_is_invalid_format
            )
        }
    }

    private fun onChangeEmailAuthNum(authNum: String) {
        when (uiState.value.emailAuthState) {
            AuthenticationState.TIME_EXPIRED, AuthenticationState.VERIFIED -> return

            AuthenticationState.SEND_SUCCESS, AuthenticationState.SEND_FAILED, AuthenticationState.REJECTED -> updateState { copy(emailAuthNumber = authNum) }

            AuthenticationState.NONE -> {
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

        when (userRepository.requestEmailAuthNumber(uiState.value.email)) {
            is ApiResult.Success -> {
                sendEffect(SignUpUiEffect.ShowSnackBer(R.string.email_auth_sent))
                updateState { copy(emailAuthState = AuthenticationState.SEND_SUCCESS) }
                process(SignUpUiAction.StartEmailAuthTimer)
            }

            is ApiResult.NetworkError -> {
                updateState {
                    copy(
                        failureDialogState = SignUpUiState.FailureDialogState(
                            title = NETWORK_IS_UNSTABLE,
                            message = CHECK_NETWORK_AND_TRY_AGAIN,
                            retryAction = SignUpUiAction.ClickEmailAuthNumSend
                        )
                    )
                }
            }

            is ApiResult.Failure, is ApiResult.UnknownError -> {
                updateState {
                    copy(
                        emailAuthState = if (uiState.value.emailAuthState == AuthenticationState.NONE) AuthenticationState.SEND_FAILED
                        else uiState.value.emailAuthState,
                        authNumberSupportingTextResId = R.string.email_auth_sent_fail
                    )
                }
            }
        }
    }

    private fun verifyEmailAuthNumber() {
        if (uiState.value.isSendFailedEmailAuth) return

        val authenticationNumber = uiState.value.emailAuthNumber
        val email = uiState.value.email

        viewModelScope.launch {
            val result = userRepository.verifyEmailAuthNumber(
                email,
                authenticationNumber
            )
            when (result) {
                is ApiResult.Success -> {
                    updateState {
                        copy(
                            emailAuthState = AuthenticationState.VERIFIED,
                            authNumberSupportingTextResId = R.string.success_verify_auth_number,
                        )
                    }
                    cancelEmailAuthTimer()
                }

                is ApiResult.Failure -> {
                    updateState {
                        copy(
                            emailAuthState = AuthenticationState.REJECTED,
                            authNumberSupportingTextResId = R.string.email_auth_num_is_different
                        )
                    }
                }

                is ApiResult.NetworkError -> {
                    updateState {
                        copy(
                            failureDialogState = SignUpUiState.FailureDialogState(
                                title = NETWORK_IS_UNSTABLE,
                                message = CHECK_NETWORK_AND_TRY_AGAIN,
                                retryAction = SignUpUiAction.ClickVerifyAuthNum
                            )
                        )
                    }
                }

                is ApiResult.UnknownError -> {
                    updateState {
                        copy(
                            failureDialogState = SignUpUiState.FailureDialogState(
                                title = UNKNOWN_ERROR,
                                message = CONTACT_DEVELOPER_IF_PERSISTS,
                            )
                        )
                    }
                }
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
        else R.string.id_format_guide
        updateState {
            copy(
                id = id,
                idErrorMessageResId = idErrorMessageResId,
                idValidationState = SignUpUiState.UserIdValidationState.NONE
            )
        }
    }

    private fun checkDuplicateId() {
        if (uiState.value.idValidationState != SignUpUiState.UserIdValidationState.NONE) return

        if (uiState.value.id.isEmpty()) {
            updateState { copy(idErrorMessageResId = R.string.please_enter_id) }
            return
        }

        if (ID_REGEX.matches(uiState.value.id).not()) return

        viewModelScope.launch {
            when (val result = userRepository.isNotDuplicateId(uiState.value.id)) {
                is ApiResult.Success -> {
                    updateState {
                        copy(
                            idValidationState = SignUpUiState.UserIdValidationState.AVAILABLE,
                            idErrorMessageResId = R.string.empty,
                        )
                    }
                }

                is ApiResult.Failure -> {
                    updateState {
                        copy(
                            idValidationState = SignUpUiState.UserIdValidationState.NOT_AVAILABLE,
                            idErrorMessageResId = R.string.id_cannot_be_used
                        )
                    }
                }

                is ApiResult.NetworkError -> {
                    updateState {
                        copy(
                            failureDialogState = SignUpUiState.FailureDialogState(
                                title = NETWORK_IS_UNSTABLE,
                                message = CHECK_NETWORK_AND_TRY_AGAIN,
                                retryAction = SignUpUiAction.ClickIdDuplicateCheck
                            )
                        )
                    }
                }

                is ApiResult.UnknownError -> {
                    updateState {
                        copy(
                            failureDialogState = SignUpUiState.FailureDialogState(
                                title = result.throwable?.message ?: UNKNOWN_ERROR,
                                message = CONTACT_DEVELOPER_IF_PERSISTS,
                            )
                        )
                    }
                }
            }
        }
    }

    /*
    * ******************************************
    * PASSWORD
    * ******************************************
    */
    private fun onChangeUserPw(password: String) {
        val passwordCheck = uiState.value.pwCheck
        val errorMessage =
            if (password.isNotEmpty() && passwordCheck.isNotEmpty() && password != passwordCheck) R.string.password_is_incorrect
            else R.string.empty

        updateState {
            copy(
                pw = password,
                pwCheckErrorMessageResId = errorMessage,
            )
        }
    }

    private fun onChangeUserPwCheck(passwordCheck: String) {
        val password = uiState.value.pw
        val errorMessage =
            if (password.isNotEmpty() && passwordCheck.isNotEmpty() && password != passwordCheck) R.string.password_is_incorrect
            else R.string.empty

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
        updateState { copy(failureDialogState = null) }
        signUp()
    }

    private fun onClickIdDuplicateCheck() {
        checkDuplicateId()
    }

    private fun changeFocus(focusState: SignUpUiState.FocusState) {
        updateState { copy(focusState = focusState) }
    }

    private fun signUp() = viewModelScope.launch {
        val result = userRepository.signUp(
            loginId = uiState.value.id,
            email = uiState.value.email,
            password = uiState.value.pw,
            nickname = uiState.value.name,
        )

        when (result) {
            is ApiResult.Success -> {
                sendEffect(SignUpUiEffect.ShowSnackBer(R.string.sign_up_complete))
                sendEffect(SignUpUiEffect.SignUpUiComplete)
            }

            is ApiResult.Failure -> {
                updateState {
                    copy(
                        failureDialogState = SignUpUiState.FailureDialogState(
                            title = result.message,
                            message = "회원가입에 실패하였습니다."
                        )
                    )
                }
            }

            is ApiResult.NetworkError -> {
                updateState {
                    copy(
                        failureDialogState = SignUpUiState.FailureDialogState(
                            title = NETWORK_IS_UNSTABLE,
                            message = CHECK_NETWORK_AND_TRY_AGAIN,
                            retryAction = SignUpUiAction.ClickSignUp
                        )
                    )
                }
            }

            is ApiResult.UnknownError -> {
                updateState {
                    copy(
                        failureDialogState = SignUpUiState.FailureDialogState(
                            title = result.throwable?.message ?: UNKNOWN_ERROR,
                            message = CONTACT_DEVELOPER_IF_PERSISTS,
                        )
                    )
                }
            }
        }
    }

    /*
    * ******************************************
    * FOR TEST
    * ******************************************
    */

    @VisibleForTesting
    val isTimerJobNull
        get() = timerJob == null

    @VisibleForTesting
    val isTimerJobNotNull
        get() = timerJob != null

    companion object {
        val AUTHENTICATION_LIMIT_MILS = 3.minutes.inWholeMilliseconds
    }
}
