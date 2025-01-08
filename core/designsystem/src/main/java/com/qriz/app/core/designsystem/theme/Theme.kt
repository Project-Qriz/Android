package com.qriz.app.core.designsystem.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val QrizColorScheme = lightColorScheme(
    primary = Blue600,
    onPrimary = White,
    primaryContainer = Blue200,
    onPrimaryContainer = Blue500,
    secondary = Gray200,
    onSecondary = Gray500,
    secondaryContainer = Gray100,
    tertiary = Mint600,
    tertiaryContainer = Mint100,
    surface = White,
    onSurface = Gray800,
    inverseOnSurface = Gray300,
    surfaceVariant = Gray600,
    onSurfaceVariant = Gray500,
    outline = Blue600,
    background = Blue100,
    error = Red500,
)

@Composable
fun QrizTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = QrizColorScheme

    if (!LocalInspectionMode.current) {
        val view = LocalView.current
        val activity = view.context as Activity
        val window = activity.window
        WindowCompat.getInsetsController(window, activity.window.decorView).apply {
            isAppearanceLightStatusBars = darkTheme.not()
            isAppearanceLightNavigationBars = darkTheme.not()
        }
        window.statusBarColor = colorScheme.background.toArgb()
        window.navigationBarColor = colorScheme.background.toArgb()
    }

    CompositionLocalProvider(
        LocalTypography provides Typography
    ) {
        MaterialTheme(
            colorScheme = QrizColorScheme,
            content = content,
        )
    }
}

object QrizTheme {
    val typography: QrizTypography
        @Composable
        get() = LocalTypography.current
}