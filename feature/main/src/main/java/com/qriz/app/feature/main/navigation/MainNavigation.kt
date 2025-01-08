package com.qriz.app.feature.main.navigation

import android.app.Activity
import android.content.Intent
import com.qriz.app.core.navigation.route.MainNavigator
import com.qriz.app.core.navigation.route.MainNavigator.Companion.EXTRA_IS_LOGIN
import com.qriz.app.feature.main.MainActivity
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

internal class MainNavigatorImpl @Inject constructor() : MainNavigator {
    override fun navigate(
        currentActivity: Activity,
        intentAction: Intent.() -> Intent,
        shouldFinish: Boolean,
        isLogin: Boolean
    ) {
        currentActivity.startActivity(
            Intent(currentActivity, MainActivity::class.java)
                .putExtra(EXTRA_IS_LOGIN, isLogin)
                .intentAction()
        )
        if (shouldFinish) currentActivity.finish()
    }
}

@Module
@InstallIn(SingletonComponent::class)
internal interface MainNavigatorModule {
    @Binds
    @Singleton
    fun bindMainNavigator(
        mainNavigator: MainNavigatorImpl,
    ): MainNavigator
}
