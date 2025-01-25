package com.qriz.app.feature.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.qriz.app.core.navigation.route.MainTabRoute
import com.qriz.app.feature.home.HomeScreen

fun NavController.navigateToHome(navOptions: NavOptions) {
    navigate(MainTabRoute.Home, navOptions)
}

fun NavGraphBuilder.homeNavGraph(
    padding: PaddingValues,
    popBackStack: () -> Unit,
    onShowSnackBar: (String) -> Unit,
) {
    composable<MainTabRoute.Home> {
        HomeScreen(
//            padding = padding,
//            popBackStack = popBackStack,
            onShowSnackBar = onShowSnackBar,
        )
    }
}
