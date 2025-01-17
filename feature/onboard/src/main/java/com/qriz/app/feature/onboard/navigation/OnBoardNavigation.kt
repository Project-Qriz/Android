package com.qriz.app.feature.onboard.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.qriz.app.core.navigation.route.OnBoardRoute
import com.qriz.app.core.navigation.route.Route
import com.qriz.app.core.navigation.route.SignRoute
import com.qriz.app.feature.onboard.guide.ConceptCheckGuideScreen
import com.qriz.app.feature.onboard.guide.PreviewGuideScreen
import com.qriz.app.feature.onboard.guide.WelcomeGuideScreen
import com.qriz.app.feature.onboard.preview.PreviewScreen
import com.qriz.app.feature.onboard.survey.ConceptCheckScreen

fun NavHostController.navigateConceptCheckGuide() {
    navigate(OnBoardRoute.ConceptCheckGuide) {
        popUpTo<SignRoute.SignIn>()
    }
}

fun NavGraphBuilder.onboardNavGraph(
    onBack: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    onNavigate: (Route) -> Unit,
) {
    composable<OnBoardRoute.ConceptCheckGuide> {
        ConceptCheckGuideScreen(
            onNext = { onNavigate(OnBoardRoute.ConceptCheck) }
        )
    }

    composable<OnBoardRoute.ConceptCheck> {
        ConceptCheckScreen(
            moveToGuide = { onNavigate(OnBoardRoute.PreviewGuide) },
            moveToBack = onBack,
            onShowSnackBar = onShowSnackbar,
        )
    }

    composable<OnBoardRoute.PreviewGuide> {
        PreviewGuideScreen(
            onNext = { onNavigate(OnBoardRoute.Preview) }
        )
    }

    composable<OnBoardRoute.Preview> {
        PreviewScreen(
            moveToBack = onBack,
            moveToGuide = { onNavigate(OnBoardRoute.PreviewResult) },
            onShowSnackBar = onShowSnackbar,
        )
    }

    composable<OnBoardRoute.PreviewResult> {

    }

    composable<OnBoardRoute.WelcomeGuide> {
        WelcomeGuideScreen(
            userName = "asfa",
            onNext = { }
        )
    }
}
