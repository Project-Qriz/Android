package com.qriz.app.feature.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.feature.main.MainActivity
import com.qriz.app.feature.splash.model.SplashEffect
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : ComponentActivity() {
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val window = this.window
            window.navigationBarColor = Color.White.toArgb()

            LaunchedEffect(Unit) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is SplashEffect.CheckLogin -> {
                            val intent =
                                Intent(this@SplashActivity, MainActivity::class.java)
                                    .putExtra("login", effect.login)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }

            QrizTheme {
                SplashScreen()
            }
        }
    }
}