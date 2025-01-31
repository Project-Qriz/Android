package com.qriz.app.feature.splash

import androidx.lifecycle.viewModelScope
import com.qriz.app.feature.base.BaseViewModel
import com.quiz.app.core.data.user.user_api.model.PreviewTestStatus
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : BaseViewModel<SplashUiState, SplashUiEffect, SplashUiAction>(SplashUiState) {

    override fun process(action: SplashUiAction): Job = viewModelScope.launch {
        when (action) {
            is SplashUiAction.LoadClientProfile -> loadClientProfile()
        }
    }

    private fun loadClientProfile() = viewModelScope.launch {
        delay(2000)
        val client = userRepository.getClient()
        when {
            client == null -> {
                sendEffect(SplashUiEffect.MoveToLogin)
                return@launch
            }

            client.previewTestStatus == PreviewTestStatus.NOT_STARTED -> {
                sendEffect(SplashUiEffect.MoveToSurvey)
                return@launch
            }

            else -> sendEffect(SplashUiEffect.MoveToMain())
        }
    }

}
