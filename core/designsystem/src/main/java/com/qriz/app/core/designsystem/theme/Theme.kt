package com.qriz.app.core.designsystem.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
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
    background = White,
    error = Red700,
)

@Composable
fun QrizTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    if (LocalInspectionMode.current.not()) {
        val view = LocalView.current
        val activity = view.context as Activity
        val window = activity.window
        SideEffect {
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = true
                isAppearanceLightNavigationBars = true
            }
        }
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
