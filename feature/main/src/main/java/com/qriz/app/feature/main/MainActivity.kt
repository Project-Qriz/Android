package com.qriz.app.feature.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.navigation.route.MainNavigator.Companion.EXTRA_IS_LOGIN
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val login = intent.getBooleanExtra(EXTRA_IS_LOGIN, false)
            QrizTheme {
                QrizApp(
                    login = login
                )
            }
        }
    }
}
