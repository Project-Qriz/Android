package com.qriz.app.feature.mypage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.qriz.app.core.navigation.route.MainTabRoute
import com.qriz.app.core.navigation.route.MyPageRoute
import com.qriz.app.feature.mypage.MyPageScreen
import com.qriz.app.feature.mypage.setting.SettingScreen
import com.qriz.app.feature.mypage.withdraw.WithdrawScreen

fun NavController.navigateToMyPage(navOptions: NavOptions) {
    navigate(MainTabRoute.MyPage, navOptions)
}

fun NavController.navigateSetting(navOptions: NavOptions? = null) {
    navigate(MyPageRoute.Setting, navOptions)
}

fun NavController.navigateWithdraw(navOptions: NavOptions? = null) {
    navigate(MyPageRoute.Withdraw, navOptions)
}

fun NavGraphBuilder.myPageNavGraph(
    onBack: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    moveToSetting: () -> Unit,
    moveToLogin: () -> Unit,
    moveToResetPassword: () -> Unit,
    moveToWithDraw: () -> Unit
) {
    composable<MainTabRoute.MyPage> {
        MyPageScreen(
            onShowSnackBar = onShowSnackbar,
            moveToSetting = moveToSetting,
        )
    }

    composable<MyPageRoute.Setting> {
        SettingScreen(
            onBack = onBack,
            onShowSnackBar = onShowSnackbar,
            moveToLogin = moveToLogin,
            moveToResetPassword = moveToResetPassword,
            moveToWithDraw = moveToWithDraw,
        )
    }

    composable<MyPageRoute.Withdraw> {
        WithdrawScreen(
            onBack = onBack,
            onShowSnackBar = onShowSnackbar,
            moveToLogin = moveToLogin,
        )
    }
}
