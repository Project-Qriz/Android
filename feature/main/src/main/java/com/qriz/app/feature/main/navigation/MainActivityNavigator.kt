package com.qriz.app.feature.main.navigation

import android.app.Activity
import android.content.Intent
import com.qriz.app.core.navigation.route.MainActivityNavigator
import com.qriz.app.feature.main.MainActivity
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

internal class MainActivityNavigatorImpl @Inject constructor() : MainActivityNavigator {
    override fun navigate(
        currentActivity: Activity,
        intentAction: Intent.() -> Intent,
        shouldFinish: Boolean,
    ) {
        currentActivity.startActivity(
            Intent(currentActivity, MainActivity::class.java)
                .intentAction()
        )
        if (shouldFinish) currentActivity.finish()
    }
}

@Module
@InstallIn(SingletonComponent::class)
internal interface MainActivityNavigatorModule {
    @Binds
    @Singleton
    fun bindMainActivityNavigator(
        mainActivityNavigator: MainActivityNavigatorImpl,
    ): MainActivityNavigator
}
