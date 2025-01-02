package com.qriz.app.feature.main.navigation

import androidx.navigation.NavDestination
import androidx.navigation.NavHostController

class QrizNavigator(
    private val navController: NavHostController
) {
    val currentDestination: NavDestination?
        get() = navController.currentDestination
}
