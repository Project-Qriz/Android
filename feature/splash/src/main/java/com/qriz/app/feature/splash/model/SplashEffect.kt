package com.qriz.app.feature.splash.model

sealed interface SplashEffect {
    data class CheckLogin(
        val login: Boolean
    ) : SplashEffect
}
