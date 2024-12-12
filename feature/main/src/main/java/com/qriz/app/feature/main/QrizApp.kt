package com.qriz.app.feature.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.qriz.app.core.navigation.route.Route
import com.qriz.app.feature.onboard.navigation.navigateCheckGuide
import com.qriz.app.feature.onboard.navigation.onboardNavGraph
import com.qriz.app.feature.sign.navigation.navigateSignUp
import com.qriz.app.feature.sign.navigation.signNavGraph

@Composable
internal fun QrizApp(
    login: Boolean,
) {
    val navController = rememberNavController()

    Scaffold(
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
            )
        }
    }
}

@Composable
private fun QrizNavHost(
    login: Boolean,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Route.SignIn,
    ) {
        signNavGraph(
            onClickSignUp = {
                navController.navigateSignUp()
            },
            onSignUpComplete = {
                navController.navigateCheckGuide()
            },
        )

        onboardNavGraph(
            onNavigateFromGuide = { route ->
                navController.navigate(route)
            }
        )
    }
}
