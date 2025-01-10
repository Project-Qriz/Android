package com.qriz.app.feature.sign.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.qriz.app.core.navigation.route.Route
import com.qriz.app.feature.sign.signIn.SignInScreen
import com.qriz.app.feature.sign.signUp.SignUpScreen

fun NavHostController.navigateSignIn() {
    navigate(Route.SignIn)
}

fun NavHostController.navigateSignUp() {
    navigate(Route.SignUp)
}

fun NavGraphBuilder.signNavGraph(
    onBack: () -> Unit,
    onClickSignUp: () -> Unit,
    onSignUpComplete: () -> Unit,
    onShowSnackbar: (String) -> Unit,
) {
    composable<Route.SignIn> {
        SignInScreen(onClickSignUp = onClickSignUp)
    }

    composable<Route.SignUp> {
        SignUpScreen(
            onBack = onBack,
            onSignUp = onSignUpComplete,
            onShowSnackbar = onShowSnackbar,
        )
    }
}
