package com.qriz.app.feature.splash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.navigation.route.MainNavigator
import com.qriz.app.feature.splash.model.SplashEffect
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
            LaunchedEffect(Unit) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is SplashEffect.CheckLogin -> moveToMain(effect.login)
                    }
                }
            }

            QrizTheme {
                SplashScreen()
            }
        }
    }

    private fun moveToMain(isLogin: Boolean) {
//        mainNavigator.navigate(
//            currentActivity = this,
//            shouldFinish = true,
//            isLogin = isLogin
//        )
    }
}