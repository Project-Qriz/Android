package com.qriz.app.feature.sign.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.qriz.app.core.navigation.route.Route
import com.qriz.app.feature.sign.screen.SignInScreen
import com.qriz.app.feature.sign.screen.SignUpScreen

fun NavHostController.navigateSignIn() {
    navigate(Route.SignIn)
}

fun NavHostController.navigateSignUp() {
    navigate(Route.SignUp)
}

fun NavGraphBuilder.signNavGraph(
    onClickSignUp: () -> Unit,
    onSignUpComplete: () -> Unit,
    onShowSnackbar: (String) -> Unit,
) {
    composable<Route.SignIn> {
        SignInScreen(onClickSignUp = onClickSignUp)
    }

    composable<Route.SignUp> {
        SignUpScreen(
            onSignUp = onSignUpComplete,
            onShowSnackbar = onShowSnackbar,
        )
    }
}
