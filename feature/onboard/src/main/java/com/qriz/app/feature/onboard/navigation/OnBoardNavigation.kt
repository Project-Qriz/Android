package com.qriz.app.feature.onboard.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.qriz.app.core.navigation.route.OnBoardRoute
import com.qriz.app.core.navigation.route.Route
import com.qriz.app.feature.onboard.guide.ConceptCheckGuideScreen
import com.qriz.app.feature.onboard.guide.PreviewGuideScreen
import com.qriz.app.feature.onboard.guide.WelcomeGuideScreen
import com.qriz.app.feature.onboard.preview.PreviewScreen
import com.qriz.app.feature.onboard.previewresult.PreviewResultScreen
import com.qriz.app.feature.onboard.survey.ConceptCheckScreen

fun NavHostController.navigateConceptCheckGuide() {
    navigate(OnBoardRoute.ConceptCheckGuide)
}

fun NavHostController.navigatePreviewGuide() {
    navigate(OnBoardRoute.PreviewGuide) {
        popUpTo(graph.id)
    }
}

fun NavHostController.navigatePreviewResult() {
    navigate(OnBoardRoute.PreviewResult) {
        popUpTo(graph.id)
    }
}

fun NavHostController.navigateWelcomeGuide(userName: String) {
    navigate(OnBoardRoute.WelcomeGuide(userName)) {
        popUpTo(graph.id)
    }
}

fun NavGraphBuilder.onboardNavGraph(
    onBack: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    onNavigate: (Route) -> Unit,
    moveToPreviewGuide: () -> Unit,
    moveToPreviewResult: () -> Unit,
    moveToWelcomeGuide: (String) -> Unit,
    moveToHome: () -> Unit,
) {
    composable<OnBoardRoute.ConceptCheckGuide> {
        ConceptCheckGuideScreen(
            onNext = { onNavigate(OnBoardRoute.ConceptCheck) }
        )
    }

    composable<OnBoardRoute.ConceptCheck> {
        ConceptCheckScreen(
            moveToPreviewGuide = moveToPreviewGuide,
            onShowSnackBar = onShowSnackbar,
        )
    }

    composable<OnBoardRoute.PreviewGuide> {
        PreviewGuideScreen(
            onNext = { onNavigate(OnBoardRoute.Preview) },
            moveToHome = moveToHome,
        )
    }

    composable<OnBoardRoute.Preview> {
        PreviewScreen(
            moveToPreviewResult = moveToPreviewResult,
            moveToHome = moveToHome,
            onShowSnackBar = onShowSnackbar,
        )
    }

    composable<OnBoardRoute.PreviewResult> {
        PreviewResultScreen(
            moveToWelcomeGuide = moveToWelcomeGuide,
            onShowSnackBar = onShowSnackbar,
        )
    }

    composable<OnBoardRoute.WelcomeGuide> { navBackStackEntry ->
        val userName = navBackStackEntry.toRoute<OnBoardRoute.WelcomeGuide>().userName
        WelcomeGuideScreen(
            userName = userName,
            moveToHome = moveToHome
        )
    }
}
