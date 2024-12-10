package com.qriz.app.feature.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.qriz.app.core.navigation.route.Route
import com.qriz.app.feature.sign.navigation.navigateSignUp
import com.qriz.app.feature.sign.navigation.signNavGraph

@Composable
internal fun QrizApp(
    login: Boolean,
) {
    val navController = rememberNavController()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
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
                
            }
        )
    }
}
