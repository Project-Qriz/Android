package com.qriz.app.feature.sign.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.qriz.app.core.navigation.route.SignRoute
import com.qriz.app.feature.sign.findId.FindIdScreen
import com.qriz.app.feature.sign.findPassword.auth.FindPasswordAuthScreen
import com.qriz.app.feature.sign.findPassword.reset.ResetPasswordScreen
import com.qriz.app.feature.sign.signin.SignInScreen
import com.qriz.app.feature.sign.signup.SignUpScreen

fun NavHostController.navigateSignIn(navOptions: NavOptions? = null) {
    navigate(
        route = SignRoute.SignIn,
        navOptions = navOptions,
    )
}

fun NavHostController.navigateSignUp() {
    navigate(SignRoute.SignUp)
}

fun NavHostController.navigateFindId() {
    navigate(SignRoute.FindId)
}

fun NavHostController.navigateFindPasswordAuth() {
    navigate(SignRoute.FindPasswordAuth)
}

fun NavHostController.navigateResetPassword() {
    navigate(SignRoute.ResetPassword)
}

fun NavGraphBuilder.signNavGraph(
    onBack: () -> Unit,
    moveToSignUp: () -> Unit,
    moveToFindId: () -> Unit,
    moveToFindPw: () -> Unit,
    moveToHome: () -> Unit,
    moveToConceptCheckGuide: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    moveToResetPw: () -> Unit,
    moveToSignIn: () -> Unit,
) {
    composable<SignRoute.SignIn> {
        SignInScreen(
            onShowSnackbar = onShowSnackbar,
            moveToSignUp = moveToSignUp,
            moveToFindId = moveToFindId,
            moveToFindPw = moveToFindPw,
            moveToHome = moveToHome
        )
    }

    composable<SignRoute.SignUp> {
        SignUpScreen(
            onBack = onBack,
            moveToConceptCheckGuide = moveToConceptCheckGuide,
            onShowSnackbar = onShowSnackbar,
        )
    }

    composable<SignRoute.FindId> {
        FindIdScreen(onBack = onBack)
    }

    composable<SignRoute.FindPasswordAuth> {
        FindPasswordAuthScreen(
            onBack = onBack,
            onNavigateReset = moveToResetPw
        )
    }

    composable<SignRoute.ResetPassword> {
        ResetPasswordScreen(
            onBack = onBack,
            moveToSignIn = moveToSignIn
        )
    }
}
