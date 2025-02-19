package com.qriz.app.feature.sign.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.qriz.app.core.navigation.route.Route
import com.qriz.app.feature.sign.findId.FindIdScreen
import com.qriz.app.feature.sign.findPassword.auth.FindPasswordAuthScreen
import com.qriz.app.feature.sign.findPassword.reset.ResetPasswordScreen
import com.qriz.app.feature.sign.signin.SignInScreen
import com.qriz.app.feature.sign.signup.SignUpScreen

fun NavHostController.navigateSignIn(navOptions: NavOptions? = null) {
    navigate(
        route = Route.SignIn,
        navOptions = navOptions,
    )
}

fun NavHostController.navigateSignUp() {
    navigate(Route.SignUp)
}

fun NavHostController.navigateFindId() {
    navigate(Route.FindId)
}

fun NavHostController.navigateFindPasswordAuth() {
    navigate(Route.FindPasswordAuth)
}

fun NavHostController.navigateResetPassword() {
    navigate(Route.ResetPassword)
}

fun NavGraphBuilder.signNavGraph(
    onBack: () -> Unit,
    moveToSignUp: () -> Unit,
    moveToFindId: () -> Unit,
    moveToFindPw: () -> Unit,
    moveToHome: () -> Unit,
    onSignUpComplete: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    moveToResetPw: () -> Unit,
    moveToSignIn: () -> Unit,
) {
    composable<Route.SignIn> {
        SignInScreen(
            onShowSnackbar = onShowSnackbar,
            moveToSignUp = moveToSignUp,
            moveToFindId = moveToFindId,
            moveToFindPw = moveToFindPw,
            moveToHome = moveToHome
        )
    }

    composable<Route.SignUp> {
        SignUpScreen(
            onBack = onBack,
            onSignUpComplete = onSignUpComplete,
            onShowSnackbar = onShowSnackbar,
        )
    }

    composable<Route.FindId> {
        FindIdScreen(onBack = onBack)
    }

    composable<Route.FindPasswordAuth> {
        FindPasswordAuthScreen(
            onBack = onBack,
            onNavigateReset = moveToResetPw
        )
    }

    composable<Route.ResetPassword> {
        ResetPasswordScreen(
            onBack = onBack,
            moveToSignIn = moveToSignIn
        )
    }
}
