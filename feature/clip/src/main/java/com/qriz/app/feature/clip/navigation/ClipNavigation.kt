package com.qriz.app.feature.clip.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.qriz.app.core.navigation.route.MainTabRoute
import com.qriz.app.feature.clip.ClipScreen

fun NavController.navigateToClip(navOptions: NavOptions) {
    navigate(MainTabRoute.IncorrectAnswersNote, navOptions)
}

fun NavGraphBuilder.clipNavGraph(
    onShowSnackbar: (String) -> Unit,
    moveToDailyStudy: (Int, Boolean, Boolean) -> Unit,
    moveToMockTestSessions: () -> Unit,
) {
    composable<MainTabRoute.IncorrectAnswersNote> {
        ClipScreen(
            onShowSnackBar = onShowSnackbar,
            moveToDailyStudy = moveToDailyStudy,
            moveToMockTestSessions = moveToMockTestSessions,
        )
    }
}
