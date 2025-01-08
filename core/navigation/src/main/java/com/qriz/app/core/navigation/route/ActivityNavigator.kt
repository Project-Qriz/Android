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

interface MainNavigator {
    fun navigate(
        currentActivity: Activity,
        intentAction: Intent.() -> Intent = { this },
        shouldFinish: Boolean = false,
        isLogin: Boolean,
    )

    companion object {
        const val EXTRA_IS_LOGIN = "EXTRA_IS_LOGIN"
    }
}