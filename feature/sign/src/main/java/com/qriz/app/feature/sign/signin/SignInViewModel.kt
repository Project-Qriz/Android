package com.qriz.app.feature.sign.signin

import androidx.lifecycle.viewModelScope
import com.qriz.app.core.model.ApiResult
import com.qriz.app.feature.base.BaseViewModel
import com.qriz.app.feature.sign.R
import com.quiz.app.core.data.user.user_api.model.SocialLoginType
import com.quiz.app.core.data.user.user_api.model.User
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.qriz.app.core.ui.common.R as UCR

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : BaseViewModel<SignInUiState, SignInUiEffect, SignInUiAction>(SignInUiState.Default) {

    override fun process(action: SignInUiAction): Job = viewModelScope.launch {
        when (action) {
            is SignInUiAction.ChangeUserId -> onChangeUserId(action.id)
            is SignInUiAction.ChangeUserPw -> onChangeUserPw(action.pw)
            is SignInUiAction.ClickPwVisibility -> onClickPwVisibility(action.isVisible)
            is SignInUiAction.ClickLogin -> onClickLogin()
            is SignInUiAction.ClickFindId -> onClickFindId()
            is SignInUiAction.ClickFindPw -> onClickFindPw()
            is SignInUiAction.ClickSignUp -> onClickSignUp()
            is SignInUiAction.ClickGoogleLogin -> sendEffect(SignInUiEffect.GoogleLogin)
            is SignInUiAction.ClickKakaoLogin -> sendEffect(SignInUiEffect.KakaoLogin)
            is SignInUiAction.ProcessGoogleLogin -> googleLogin(action.token)
            is SignInUiAction.ProcessKakaoLogin -> kakaoLogin(action.token)
            is SignInUiAction.ShowSnackbar -> sendEffect(
                SignInUiEffect.ShowSnackBar(
                    defaultResId = R.string.empty,
                    message = action.message
                )
            )
        }
    }

    private fun onChangeUserId(id: String) {
        updateState {
            copy(
                id = id,
                loginErrorMessageResId = R.string.empty
            )
        }
    }

    private fun onChangeUserPw(password: String) {
        updateState {
            copy(
                pw = password,
                loginErrorMessageResId = R.string.empty
            )
        }
    }

    private fun onClickPwVisibility(isVisible: Boolean) {
        updateState { copy(isVisiblePw = isVisible) }
    }

    private fun onClickLogin() {
        login()
    }

    private fun onClickFindId() {
        sendEffect(SignInUiEffect.MoveToFindId)
    }

    private fun onClickFindPw() {
        sendEffect(SignInUiEffect.MoveToFindPw)
    }

    private fun onClickSignUp() {
        sendEffect(SignInUiEffect.MoveToSignUp)
    }

    private suspend fun kakaoLogin(token: String) =
        processSocialLogin(SocialLoginType.KAKAO, token)

    private suspend fun googleLogin(token: String) =
        processSocialLogin(SocialLoginType.GOOGLE, token)

    private suspend fun processSocialLogin(socialLoginType: SocialLoginType, token: String) {
        if (uiState.value.isLoading) return
        updateState { copy(isLoading = true) }

        when (val result = userRepository.socialLogin(socialLoginType, token)) {
            is ApiResult.Success<User> -> {
                updateState { copy(isLoading = false) }
                if (result.data.isSurveyNeeded) {
                    sendEffect(SignInUiEffect.MoveToConceptCheckGuide)
                } else {
                    sendEffect(SignInUiEffect.MoveToHome)
                }
            }

            is ApiResult.Failure -> {
                updateState { copy(isLoading = false) }
                sendEffect(
                    SignInUiEffect.ShowSnackBar(
                        defaultResId = R.string.user_not_registered,
                    )
                )
            }

            is ApiResult.NetworkError -> {
                updateState { copy(isLoading = false) }
                sendEffect(
                    SignInUiEffect.ShowSnackBar(
                        defaultResId = UCR.string.check_network_and_try_again,
                    )
                )
            }

            is ApiResult.UnknownError -> {
                updateState { copy(isLoading = false) }
                sendEffect(
                    SignInUiEffect.ShowSnackBar(
                        defaultResId = UCR.string.unknown_error_occurs,
                    )
                )
            }
        }
    }

    private fun login() = viewModelScope.launch {
        if (uiState.value.isLoading) return@launch
        updateState { copy(isLoading = true) }

        when (userRepository.login(
            uiState.value.id,
            uiState.value.pw
        )) {
            is ApiResult.Success -> {
                updateState { copy(isLoading = false) }
                sendEffect(SignInUiEffect.MoveToHome)
            }

            is ApiResult.Failure -> {
                updateState {
                    copy(
                        loginErrorMessageResId = R.string.user_not_registered,
                        isLoading = false,
                    )
                }
            }

            is ApiResult.NetworkError -> {
                updateState {
                    copy(
                        loginErrorMessageResId = UCR.string.check_network_and_try_again,
                        isLoading = false,
                    )
                }
            }

            is ApiResult.UnknownError -> {
                updateState {
                    copy(
                        loginErrorMessageResId = UCR.string.unknown_error_occurs,
                        isLoading = false,
                    )
                }
            }
        }
    }
}
