package com.qriz.app.core.designsystem.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

//MaterialTheme.colorScheme.
//private val QrizColorScheme = lightColorScheme(
//    primary = Blue600,
//    onPrimary = White,
//    primaryContainer = Blue200,
//    onPrimaryContainer = Blue500,
//    secondary = Gray200,
//    onSecondary = Gray500,
//    secondaryContainer = Gray100,
//    tertiary = Mint600,
//    tertiaryContainer = Mint100,
//    surface = White,
//    onSurface = Gray800,
//    inverseOnSurface = Gray300,
//    surfaceVariant = Gray600,
//    onSurfaceVariant = Gray500,
//    outline = Blue600,
//    background = Blue100,
//    error = Red500,
//)

@Composable
fun QrizTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    if (LocalInspectionMode.current.not()) {
        val view = LocalView.current
        val activity = view.context as Activity
        val window = activity.window
        WindowCompat.getInsetsController(window, activity.window.decorView).apply {
            isAppearanceLightStatusBars = darkTheme.not()
            isAppearanceLightNavigationBars = darkTheme.not()
        }
        window.statusBarColor = colorScheme.Blue100.toArgb()
        window.navigationBarColor = colorScheme.Blue100.toArgb()
    }

    CompositionLocalProvider(
        LocalColorScheme provides colorScheme,
        LocalTypography provides Typography
    ) {
        MaterialTheme(
            content = content,
        )
    }
}

object QrizTheme {
    val typography: QrizTypography
        @Composable
        get() = LocalTypography.current

    val colorScheme: QrizColorScheme
        @Composable
        get() = LocalColorScheme.current
}
