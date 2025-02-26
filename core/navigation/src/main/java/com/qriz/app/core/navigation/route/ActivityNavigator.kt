package com.qriz.app.core.navigation.route

import android.app.Activity
import android.content.Intent

interface BaseActivityNavigator {
    fun navigate(
        currentActivity: Activity,
        intentAction: Intent.() -> Intent = { this },
        shouldFinish: Boolean = false,
    )
}

interface MainActivityNavigator : BaseActivityNavigator
