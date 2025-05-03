package com.qriz.app.feature.splash

import androidx.lifecycle.viewModelScope
import com.qriz.app.core.model.ApiResult
import com.qriz.app.feature.base.BaseViewModel
import com.qriz.core.data.token.token_api.TokenRepository
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val userRepository: UserRepository,
) : BaseViewModel<SplashUiState, SplashUiEffect, SplashUiAction>(SplashUiState) {

    override fun process(action: SplashUiAction): Job = viewModelScope.launch {
        when (action) {
            is SplashUiAction.StartLogin -> startLogin()
        }
    }

    private fun startLogin() = viewModelScope.launch {
        delay(SPLASH_DURATION.seconds)
        if (isTokenExist()) loadClientProfile()
        else sendEffect(SplashUiEffect.MoveToLogin)
    }

    private fun loadClientProfile() = viewModelScope.launch {
        when(val result = userRepository.getUser()) {
            is ApiResult.Success -> {
                if (result.data.isSurveyNeeded) {
                    sendEffect(SplashUiEffect.MoveToSurvey)
                    return@launch
                }
                sendEffect(SplashUiEffect.MoveToMain())
            }
            is ApiResult.Failure,
            is ApiResult.NetworkError,
            is ApiResult.UnknownError -> {
                sendEffect(SplashUiEffect.MoveToLogin)
            }
        }
    }

    private suspend fun isTokenExist(): Boolean {
        return tokenRepository.isTokenExist()
    }

    companion object {
        private const val SPLASH_DURATION = 2
    }

}
