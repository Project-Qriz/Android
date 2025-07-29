package com.qriz.app.feature.mock_test.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.qriz.app.core.navigation.route.MockTestRoute
import com.qriz.app.feature.mock_test.sessions.MockTestSessionsScreen
import com.qriz.app.feature.mock_test.test.MockTestScreen

fun NavController.navigateToMockTestSessions(
    navOptions: NavOptions? = null,
) {
    navigate(
        MockTestRoute.MockTestSessions,
        navOptions = navOptions,
    )
}

fun NavController.navigateToMockTest(
    id: Long,
    navOptions: NavOptions? = null,
) {
    navigate(
        MockTestRoute.MockTest(id),
        navOptions = navOptions,
    )
}

fun NavGraphBuilder.mockTestNavGraph(
    onBack: () -> Unit,
    moveToMockTest: (Long) -> Unit,
    onShowSnackbar: (String) -> Unit,
) {
    composable<MockTestRoute.MockTestSessions> {
        MockTestSessionsScreen(
            onShowSnackbar = onShowSnackbar,
            moveToMockTest = moveToMockTest,
            onBack = onBack,
        )
    }

    composable<MockTestRoute.MockTest> {
        MockTestScreen(
            moveToResult = {},
            onShowSnackBar = onShowSnackbar,
            onBack = onBack,
        )
    }
}
