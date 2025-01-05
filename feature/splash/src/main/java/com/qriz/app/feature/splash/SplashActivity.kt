package com.qriz.app.feature.splash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.navigation.route.MainNavigator
import com.qriz.app.feature.base.extention.collectSideEffect
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : ComponentActivity() {
    private val viewModel: SplashViewModel by viewModels()

    @Inject
    lateinit var mainNavigator: MainNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            viewModel.collectSideEffect {
                when (it) {
                    is SplashUiEffect.MoveToMain -> moveToMain(it.isLoggedIn)
                }
            }

            QrizTheme {
                SplashScreen()
            }
        }
    }

    private fun moveToMain(isLoggedIn: Boolean) {
        mainNavigator.navigate(
            currentActivity = this,
            shouldFinish = true,
            isLogin = isLoggedIn
        )
    }
}
