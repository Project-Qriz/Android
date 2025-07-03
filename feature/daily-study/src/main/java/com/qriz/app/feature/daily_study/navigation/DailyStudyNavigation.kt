package com.qriz.app.feature.daily_study.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.qriz.app.core.navigation.route.DailyStudyRoute
import com.qriz.app.feature.daily_study.status.DailyStudyPlanStatusScreen

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

fun NavGraphBuilder.dailyStudyNavGraph(
    onBack: () -> Unit,
    onShowSnackbar: (String) -> Unit,
) {
    composable<DailyStudyRoute.DailyStudyPlanStatus> {
        DailyStudyPlanStatusScreen(
            moveToBack = onBack,
            onShowSnackbar = onShowSnackbar
        )
    }
}
