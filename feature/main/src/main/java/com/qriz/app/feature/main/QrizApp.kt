package com.qriz.app.feature.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.qriz.app.core.navigation.route.MainNavigator
import com.qriz.app.core.navigation.route.Route
import com.qriz.app.core.navigation.route.SplashRoute
import com.qriz.app.feature.concept_book.navigation.conceptBookNavGraph
import com.qriz.app.feature.home.navigation.homeNavGraph
import com.qriz.app.feature.incorrect_answers_note.navigation.incorrectAnswersNoteNavGraph
import com.qriz.app.feature.main.component.MainBottomBar
import com.qriz.app.feature.main.navigation.rememberMainNavigator
import com.qriz.app.feature.mypage.navigation.myPageNavGraph
import com.qriz.app.feature.onboard.navigation.navigateConceptCheckGuide
import com.qriz.app.feature.onboard.navigation.navigatePreviewGuide
import com.qriz.app.feature.onboard.navigation.navigatePreviewResult
import com.qriz.app.feature.onboard.navigation.navigateWelcomeGuide
import com.qriz.app.feature.onboard.navigation.onboardNavGraph
import com.qriz.app.feature.sign.navigation.navigateFindId
import com.qriz.app.feature.sign.navigation.navigateFindPasswordAuth
import com.qriz.app.feature.sign.navigation.navigateResetPassword
import com.qriz.app.feature.sign.navigation.navigateSignIn
import com.qriz.app.feature.sign.navigation.navigateSignUp
import com.qriz.app.feature.sign.navigation.signNavGraph
import com.qriz.app.feature.splash.navigation.splashNavGraph
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@Composable
internal fun QrizApp(
    mainNavigator: MainNavigator = rememberMainNavigator(),
) {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val onShowSnackbar: (String) -> Unit = { message ->
        coroutineScope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        bottomBar = {
            MainBottomBar(
                modifier = Modifier
                    .navigationBarsPadding(),
                isVisible = mainNavigator.shouldShowBottomBar(),
                tabs = MainTab.entries.toImmutableList(),
                currentTab = mainNavigator.currentTab,
                onClickTab = mainNavigator::navigate,
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .imePadding()
                    .navigationBarsPadding(),
            )
        },
        contentWindowInsets = WindowInsets(0.dp),
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .navigationBarsPadding()
                .systemBarsPadding(),
        ) {
            QrizNavHost(
                mainNavigator = mainNavigator,
                onShowSnackbar = onShowSnackbar,
            )
        }
    }
}

@Composable
private fun QrizNavHost(
    startDestination: Route = SplashRoute,
    mainNavigator: MainNavigator,
    onShowSnackbar: (String) -> Unit,
) {
    val navController = mainNavigator.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        splashNavGraph(
            moveToMain = { mainNavigator.navigateMainTabClearingStack(it) },
            moveToSurvey = navController::navigateConceptCheckGuide,
            moveToLogin = navController::navigateSignIn
        )

        signNavGraph(
            onBack = navController::popBackStack,
            onShowSnackbar = onShowSnackbar,
            moveToSignUp = navController::navigateSignUp,
            moveToFindId = navController::navigateFindId,
            moveToFindPw = navController::navigateFindPasswordAuth,
            moveToHome = { mainNavigator.navigateMainTabClearingStack(MainTab.HOME) },
            moveToConceptCheckGuide = navController::navigateConceptCheckGuide,
            moveToResetPw = navController::navigateResetPassword,
            moveToSignIn = {
                //TODO : 코드 정리 필요
                navController.navigateSignIn(
                    navOptions = navOptions {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                            saveState = true
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                )
            },
        )

        onboardNavGraph(
            onBack = navController::popBackStack,
            onNavigate = navController::navigate,
            onShowSnackbar = onShowSnackbar,
            moveToPreviewGuide = navController::navigatePreviewGuide,
            moveToPreviewResult = navController::navigatePreviewResult,
            moveToWelcomeGuide = navController::navigateWelcomeGuide,
        )

        homeNavGraph(
            onShowSnackbar = onShowSnackbar,
        )
        conceptBookNavGraph(
            onShowSnackbar = onShowSnackbar,
        )
        incorrectAnswersNoteNavGraph(
            onShowSnackbar = onShowSnackbar,
        )
        myPageNavGraph(
            onShowSnackbar = onShowSnackbar,
        )
    }
}
