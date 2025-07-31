package com.qriz.app.feature.mock_test.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.qriz.app.core.navigation.route.MockTestRoute
import com.qriz.app.feature.mock_test.guide.MockTestGuideScreen
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

fun NavController.navigateToMockTestGuide(
    id: Long,
    navOptions: NavOptions? = null,
) {
    navigate(
        MockTestRoute.MockTestGuide(id),
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
    moveToMockTestGuide: (Long) -> Unit,
    onShowSnackbar: (String) -> Unit,
) {
    composable<MockTestRoute.MockTestSessions> {
        MockTestSessionsScreen(
            onShowSnackbar = onShowSnackbar,
            moveToMockTest = moveToMockTestGuide,
            onBack = onBack,
        )
    }

    composable<MockTestRoute.MockTestGuide> {
        val id = it.toRoute<MockTestRoute.MockTestGuide>().id

        MockTestGuideScreen(
            onBack = onBack,
            moveToMockTest = { moveToMockTest(id) },
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
