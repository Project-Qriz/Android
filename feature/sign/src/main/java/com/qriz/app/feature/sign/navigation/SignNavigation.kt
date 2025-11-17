package com.qriz.app.feature.sign.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.toRoute
import com.qriz.app.core.navigation.route.SignRoute
import com.qriz.app.feature.sign.findId.FindIdScreen
import com.qriz.app.feature.sign.findPassword.auth.FindPasswordAuthScreen
import com.qriz.app.feature.sign.findPassword.reset.ResetPasswordScreen
import com.qriz.app.feature.sign.signin.SignInScreen
import com.qriz.app.feature.sign.signup.SignUpScreen
import com.qriz.app.feature.sign.termswebview.TermsWebViewScreen

fun NavHostController.navigateTermsWebView(url: String) {
    navigate(SignRoute.TermsWebView(url))
}

fun NavHostController.navigateSignIn(navOptions: NavOptions? = null) {
    navigate(
        route = SignRoute.SignIn,
        navOptions = navOptions {
            popUpTo(graph.id) {
                inclusive = true
                saveState = true
            }

            launchSingleTop = true
            restoreState = true
        },
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

fun NavHostController.navigateResetPassword(resetToken: String) {
    navigate(SignRoute.ResetPassword(resetToken))
}

fun NavGraphBuilder.signNavGraph(
    onBack: () -> Unit,
    moveToSignUp: () -> Unit,
    moveToFindId: () -> Unit,
    moveToFindPw: () -> Unit,
    moveToHome: () -> Unit,
    moveToConceptCheckGuide: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    moveToResetPw: (String) -> Unit,
    moveToSignIn: () -> Unit,
    moveToTermsWebView: (String) -> Unit,
) {
    composable<SignRoute.SignIn> {
        SignInScreen(
            onShowSnackbar = onShowSnackbar,
            moveToSignUp = moveToSignUp,
            moveToFindId = moveToFindId,
            moveToFindPw = moveToFindPw,
            moveToHome = moveToHome,
            moveToConceptCheckGuide = moveToConceptCheckGuide,
        )
    }

    composable<SignRoute.SignUp> {
        SignUpScreen(
            onBack = onBack,
            moveToConceptCheckGuide = moveToConceptCheckGuide,
            onShowSnackbar = onShowSnackbar,
            moveToTermsWebView = moveToTermsWebView,
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
        it.savedStateHandle["resetToken"] = it.toRoute<SignRoute.ResetPassword>().resetToken
        ResetPasswordScreen(
            onBack = onBack,
            onShowSnackbar = onShowSnackbar,
            moveToSignIn = moveToSignIn
        )
    }

    composable<SignRoute.TermsWebView> {
        val url = it.toRoute<SignRoute.TermsWebView>().url
        TermsWebViewScreen(
            url = url,
            onBack = onBack
        )
    }
}
