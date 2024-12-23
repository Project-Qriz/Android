package com.qriz.app.feature.main

import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController

class QrizNavigator(
    private val navController: NavHostController
) {
    val currentDestination: NavDestination?
        get() = navController.currentDestination
}
