package com.qriz.app.feature.splash.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.qriz.app.core.navigation.route.MainTabRoute
import com.qriz.app.core.navigation.route.SplashRoute
import com.qriz.app.feature.splash.SplashScreen

fun NavHostController.navigateSplash() {
    navigate(SplashRoute)
}

fun NavGraphBuilder.splashNavGraph(
    moveToMain: (MainTabRoute) -> Unit,
    moveToSurvey: () -> Unit,
    moveToLogin: () -> Unit,
) {
    composable<SplashRoute> {
        SplashScreen(
            moveToMain = moveToMain,
            moveToSurvey = moveToSurvey,
            moveToLogin = moveToLogin
        )
    }
}
