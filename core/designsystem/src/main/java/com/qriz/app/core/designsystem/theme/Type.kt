package com.qriz.app.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val BoldStyle = TextStyle(
    fontWeight = FontWeight.Bold,
)

private val NormalStyle = TextStyle(
    fontWeight = FontWeight.Normal,
)

val Typography = QrizTypography(
    /** Title */
    display1 = BoldStyle.copy(
        fontSize = 36.sp,
        lineHeight = 46.sp,
    ),
    display2 = BoldStyle.copy(
        fontSize = 32.sp,
        lineHeight = 42.sp,
    ),

    title1 = BoldStyle.copy(
        fontSize = 28.sp,
        lineHeight = 38.sp,
    ),
    title2 = BoldStyle.copy(
        fontSize = 24.sp,
        lineHeight = 34.sp,
    ),

    heading1 = BoldStyle.copy(
        fontSize = 22.sp,
        lineHeight = 32.sp,
    ),
    heading2 = BoldStyle.copy(
        fontSize = 20.sp,
        lineHeight = 28.sp,
    ),

    headline1 = BoldStyle.copy(
        fontSize = 18.sp,
        lineHeight = 24.sp,
    ),
    headline2 = BoldStyle.copy(
        fontSize = 16.sp,
        lineHeight = 23.sp,
    ),

    subhead = BoldStyle.copy(
        fontSize = 14.sp,
        lineHeight = 22.sp,
    ),
    subheadLong = BoldStyle.copy(
        fontSize = 14.sp,
        lineHeight = 28.sp,
    ),

    /** Body */
    body1 = NormalStyle.copy(
        fontSize = 16.sp,
        lineHeight = 20.sp,
    ),
    body1Long = NormalStyle.copy(
        fontSize = 16.sp,
        lineHeight = 22.sp,
    ),

    body2 = NormalStyle.copy(
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    body2Long = NormalStyle.copy(
        fontSize = 14.sp,
        lineHeight = 22.sp,
    ),

    label = NormalStyle.copy(
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    label2 = NormalStyle.copy(
        fontSize = 12.sp,
        lineHeight = 20.sp,
    ),

    caption = NormalStyle.copy(
        fontSize = 12.sp,
        lineHeight = 18.sp,
    ),

    /** Splash*/
    splash = NormalStyle.copy(
        fontSize = 24.sp,
        lineHeight = 34.sp,
    )
)

@Immutable
data class QrizTypography(
    /** Title */
    val display1: TextStyle,
    val display2: TextStyle,

    val title1: TextStyle,
    val title2: TextStyle,

    val heading1: TextStyle,
    val heading2: TextStyle,

    val headline1: TextStyle,
    val headline2: TextStyle,

    val subhead: TextStyle,
    val subheadLong: TextStyle,

    /** Body */
    val body1: TextStyle,
    val body1Long: TextStyle,

    val body2: TextStyle,
    val body2Long: TextStyle,

    val label: TextStyle,
    val label2: TextStyle,

    val caption: TextStyle,

    val splash: TextStyle,
) {
    companion object {
        val Default = QrizTypography(
            display1 = BoldStyle,
            display2 = BoldStyle,
            title1 = BoldStyle,
            title2 = BoldStyle,
            heading1 = BoldStyle,
            heading2 = BoldStyle,
            headline1 = BoldStyle,
            headline2 = BoldStyle,
            subhead = BoldStyle,
            subheadLong = BoldStyle,
            body1 = NormalStyle,
            body1Long = NormalStyle,
            body2 = NormalStyle,
            body2Long = NormalStyle,
            label = NormalStyle,
            label2 = NormalStyle,
            caption = NormalStyle,
            splash = NormalStyle,
        )
    }
}

//https://developer.android.com/develop/ui/compose/designsystems/custom#replacing-systems
val LocalTypography = staticCompositionLocalOf {
    QrizTypography.Default
}
