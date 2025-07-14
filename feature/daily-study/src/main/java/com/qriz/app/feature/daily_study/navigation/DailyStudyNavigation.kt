package com.qriz.app.feature.daily_study.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.qriz.app.core.navigation.route.DailyStudyRoute
import com.qriz.app.core.navigation.route.MainTabRoute
import com.qriz.app.feature.daily_study.result.DailyTestResultScreen
import com.qriz.app.feature.daily_study.status.DailyStudyPlanStatusScreen
import com.qriz.app.feature.daily_study.study.DailyTestScreen

fun NavController.navigateToDailyStudyPlanStatus(
    dayNumber: Int,
    isReview: Boolean = false,
    isComprehensiveReview: Boolean = false,
    navOptions: NavOptions? = null,
) {
    navigate(
        DailyStudyRoute.DailyStudyPlanStatus(
            dayNumber = dayNumber,
            isReview = isReview,
            isComprehensiveReview = isComprehensiveReview
        ),
        navOptions = navOptions,
    )
}

fun NavController.navigateToDailyTest(
    dayNumber: Int,
    navOptions: NavOptions? = null,
) {
    navigate(
        DailyStudyRoute.DailyTest(dayNumber = dayNumber),
        navOptions = navOptions,
    )
}

fun NavController.navigateToDailyTestResult(
    dayNumber: Int,
    navOptions: NavOptions = navOptions {
        popUpTo<MainTabRoute.Home>()
    },
) {
    navigate(
        DailyStudyRoute.DailyTestResult(dayNumber = dayNumber),
        navOptions = navOptions,
    )
}

fun NavGraphBuilder.dailyStudyNavGraph(
    onBack: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    moveToTest: (Int) -> Unit,
    moveToDailyStudyResult: () -> Unit,
) {
    composable<DailyStudyRoute.DailyStudyPlanStatus> {
        DailyStudyPlanStatusScreen(
            moveToBack = onBack,
            moveToTest = moveToTest,
            onShowSnackbar = onShowSnackbar
        )
    }

    composable<DailyStudyRoute.DailyTest> {
        DailyTestScreen(
            moveToResult = moveToDailyStudyResult,
            onShowSnackBar = onShowSnackbar,
            moveToBack = onBack,
        )
    }

    composable<DailyStudyRoute.DailyTestResult> {
        DailyTestResultScreen(onClose = onBack)
    }
}
