package com.qriz.app.feature.splash

import androidx.compose.runtime.Immutable
import com.qriz.app.core.navigation.route.MainTabRoute
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState

@Immutable
data object SplashUiState : UiState

sealed interface SplashUiAction : UiAction {
    data object LoadUserProfile : SplashUiAction
}

sealed interface SplashUiEffect : UiEffect {
    data class MoveToMain(val startDestination: MainTabRoute) : SplashUiEffect
    data object MoveToSurvey : SplashUiEffect
    data object MoveToLogin : SplashUiEffect
}
