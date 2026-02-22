package com.qriz.app.feature.clip.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.qriz.app.core.navigation.route.ClipRoute
import com.qriz.app.core.navigation.route.MainTabRoute
import com.qriz.app.feature.clip.ClipScreen
import com.qriz.app.feature.clip.detail.ClipDetailScreen

fun NavController.navigateToClip(navOptions: NavOptions) {
    navigate(MainTabRoute.Clip, navOptions)
}

fun NavController.navigateToClipDetail(clipId: Long, navOptions: NavOptions? = null) {
    navigate(ClipRoute.ClipDetail(clipId), navOptions = navOptions)
}

fun NavGraphBuilder.clipNavGraph(
    onBack: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    moveToDailyStudy: (Int, Boolean, Boolean) -> Unit,
    moveToMockTestSessions: () -> Unit,
    moveToClipDetail: (Long) -> Unit,
    moveToConceptBookDetail: (Long) -> Unit,
    moveToConceptBook: () -> Unit,
) {
    composable<MainTabRoute.Clip> {
        ClipScreen(
            onShowSnackBar = onShowSnackbar,
            moveToDailyStudy = moveToDailyStudy,
            moveToMockTestSessions = moveToMockTestSessions,
            moveToClipDetail = moveToClipDetail,
        )
    }

    composable<ClipRoute.ClipDetail> {
        ClipDetailScreen(
            onBack = onBack,
            onShowSnackBar = onShowSnackbar,
            onMoveToConceptBookDetail = moveToConceptBookDetail,
            onMoveToConceptBook = moveToConceptBook,
        )
    }
}
