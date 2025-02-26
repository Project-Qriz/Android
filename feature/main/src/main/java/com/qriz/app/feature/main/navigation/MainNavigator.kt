package com.qriz.app.feature.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.qriz.app.core.navigation.route.MainTabRoute
import com.qriz.app.core.navigation.route.Route
import com.qriz.app.feature.concept_book.navigation.navigateToConceptBook
import com.qriz.app.feature.home.navigation.navigateToHome
import com.qriz.app.feature.incorrect_answers_note.navigation.navigateToIncorrectAnswersNote
import com.qriz.app.feature.main.MainTab
import com.qriz.app.feature.mypage.navigation.navigateToMyPage

//TODO : ForcedLogoutManger만들어서 강제 로그아웃 Flag Flow 수신하면 navigateToSignIn이동 혹은 다이얼로그 띄우고 이동
//          Intercepter에서 401 수신 혹은 로컬에 토큰이 존재하지 않으면 ForcedLogoutManger에서 Flag변경되게 하면 될듯
//          (모든 화면에 로그아웃 코드 작성하지 않기 위해)
class MainNavigator(
    val navController: NavHostController,
    val mainStartDestination: MainTabRoute = MainTab.HOME.route,
) {
    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTab: MainTab?
        @Composable get() = MainTab.find { tab ->
            currentDestination?.hasRoute(tab::class) == true
        }

    fun navigate(tab: MainTab, navOptions: NavOptions? = null) {
        val selectedNavOptions = navOptions ?: navOptions {
            popUpTo(mainStartDestination) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (tab) {
            MainTab.HOME -> navController.navigateToHome(selectedNavOptions)
            MainTab.CONCEPT_BOOK -> navController.navigateToConceptBook(selectedNavOptions)
            MainTab.INCORRECT_ANSWERS_NOTE -> navController.navigateToIncorrectAnswersNote(
                selectedNavOptions
            )

            MainTab.MY_PAGE -> navController.navigateToMyPage(selectedNavOptions)
        }
    }

    fun navigateMainTabClearingStack(tab: MainTab) {
        navigate(tab, navOptions {
            popUpTo(navController.graph.id)
        })
    }

    fun navigateMainTabClearingStack(mainRoute: MainTabRoute) {
        MainTab.entries
            .find { tab -> tab.route == mainRoute }
            ?.let { navigateMainTabClearingStack(it) }
    }

    private fun popBackStack() {
        navController.popBackStack()
    }

    // 홈이 아닌 다른 화면에서 뒤로가기 버튼 사용시 사용
    // but 중복 클릭 시 앱이 종료되는 현상 방지를 위해 MainTabRoute.Home인지 사전 판단
    fun popBackStackIfNotHome() {
        if (isSameCurrentDestination<MainTabRoute.Home>().not()) popBackStack()
    }

    private inline fun <reified T : Route> isSameCurrentDestination(): Boolean {
        return navController.currentDestination?.hasRoute<T>() == true
    }

    @Composable
    fun shouldShowBottomBar() = MainTab.contains {
        currentDestination?.hasRoute(it::class) == true
    }
}

@Composable
internal fun rememberMainNavigator(
    navController: NavHostController = rememberNavController(),
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}
