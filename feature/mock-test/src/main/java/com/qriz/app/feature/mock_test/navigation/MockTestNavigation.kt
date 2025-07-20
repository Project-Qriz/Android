package com.qriz.app.feature.mock_test.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.qriz.app.core.navigation.route.MockTestRoute
import com.qriz.app.feature.mock_test.sessions.MockTestSessionsScreen

fun NavController.navigateToMockTestSessions(
    navOptions: NavOptions? = null,
) {
    navigate(
        MockTestRoute.MockTestSessions,
        navOptions = navOptions,
    )
}

fun NavGraphBuilder.mockTestNavGraph(
    onBack: () -> Unit,
    onShowSnackbar: (String) -> Unit,
) {
    composable<MockTestRoute.MockTestSessions> {
        MockTestSessionsScreen(
            onShowSnackbar = onShowSnackbar,
            onBack = onBack,
        )
    }
}
