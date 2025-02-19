package com.qriz.app.feature.main

import android.util.Log
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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.qriz.app.core.navigation.route.Route
import com.qriz.app.feature.onboard.navigation.navigateCheckGuide
import com.qriz.app.feature.onboard.navigation.onboardNavGraph
import com.qriz.app.feature.sign.navigation.navigateFindId
import com.qriz.app.feature.sign.navigation.navigateFindPasswordAuth
import com.qriz.app.feature.sign.navigation.navigateResetPassword
import com.qriz.app.feature.sign.navigation.navigateSignIn
import com.qriz.app.feature.sign.navigation.navigateSignUp
import com.qriz.app.feature.sign.navigation.signNavGraph
import kotlinx.coroutines.launch

@Composable
internal fun QrizApp(
    login: Boolean,
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
                login = login,
                navController = navController,
                onShowSnackbar = onShowSnackbar,
            )
        }
    }
}

@Composable
private fun QrizNavHost(
    login: Boolean,
    navController: NavHostController,
    onShowSnackbar: (String) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = Route.SignIn,
    ) {
        signNavGraph(
            onBack = navController::popBackStack,
            onShowSnackbar = onShowSnackbar,
            moveToSignUp = navController::navigateSignUp,
            moveToFindId = navController::navigateFindId,
            moveToFindPw = navController::navigateFindPasswordAuth,
            moveToHome = {},
            moveToResetPw = navController::navigateResetPassword,
            moveToSignIn = {
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
            onSignUpComplete = navController::navigateCheckGuide,
        )

        onboardNavGraph(
            onBack = navController::popBackStack,
            onNavigate = navController::navigate,
            onShowSnackbar = onShowSnackbar,
        )
    }
}
