package com.qriz.app.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.qriz.app.core.navigation.route.MainTabRoute
import com.qriz.app.feature.home.HomeScreen

fun NavController.navigateToHome(navOptions: NavOptions) {
    navigate(MainTabRoute.Home, navOptions)
}

fun NavGraphBuilder.homeNavGraph(
    moveToPreviewTest: () -> Unit,
    moveToDailyStudy: (Int, Boolean, Boolean) -> Unit,
    moveToMockTestSessions: () -> Unit,
    onShowSnackbar: (String) -> Unit,
) {
    composable<MainTabRoute.Home> {
        HomeScreen(
            moveToPreviewTest = moveToPreviewTest,
            moveToDailyStudy = moveToDailyStudy,
            moveToMockTestSessions = moveToMockTestSessions,
            onShowSnackBar = onShowSnackbar,
        )
    }
}
