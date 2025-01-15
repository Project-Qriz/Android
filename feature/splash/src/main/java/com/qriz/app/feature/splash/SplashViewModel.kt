package com.qriz.app.feature.splash

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.qriz.app.feature.base.BaseViewModel
import com.qriz.core.data.token.token_api.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val tokenRepository: TokenRepository,
) : BaseViewModel<SplashUiState, SplashUiEffect, SplashUiAction>(SplashUiState) {
    private val isTest = savedStateHandle.get<Boolean>(IS_TEST_FLAG) ?: false

    init {
        if (isTest.not()) process(SplashUiAction.CheckLoginState)
    }

    override fun process(action: SplashUiAction): Job = viewModelScope.launch {
        when (action) {
            SplashUiAction.CheckLoginState -> checkLoginState()
        }
    }

    private fun checkLoginState() = viewModelScope.launch {
        delay(2000)
        val isLoggedIn = tokenRepository.flowTokenExist.first()
        sendEffect(SplashUiEffect.MoveToMain(isLoggedIn))
    }

    companion object {
        internal const val IS_TEST_FLAG = "IS_TEST_FLAG"
    }
}
