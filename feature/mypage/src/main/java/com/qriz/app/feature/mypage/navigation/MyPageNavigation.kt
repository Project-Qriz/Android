package com.qriz.app.feature.mypage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.qriz.app.core.navigation.route.MainTabRoute
import com.qriz.app.feature.mypage.MyPageScreen

fun NavController.navigateToMyPage(navOptions: NavOptions) {
    navigate(MainTabRoute.MyPage, navOptions)
}

fun NavGraphBuilder.myPageNavGraph(
    onShowSnackbar: (String) -> Unit,
) {
    composable<MainTabRoute.MyPage> {
        MyPageScreen(
            onShowSnackBar = onShowSnackbar,
        )
    }
}
