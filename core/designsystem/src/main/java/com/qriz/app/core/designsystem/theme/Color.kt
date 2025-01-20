package com.qriz.app.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LightColorScheme = QrizColorScheme(
    Gray100 = Color(0xFFF0F4F7),
    Gray200 = Color(0xFFD8DFE9),
    Gray300 = Color(0xFFAAB6C9),
    Gray400 = Color(0xFF8F9AAA),
    Gray500 = Color(0xFF747D8B),
    Gray600 = Color(0xFF5A616B),
    Gray700 = Color(0xFF3F444C),
    Gray800 = Color(0xFF24282D),

    Blue50 = Color(0xFFF8F9FD),
    Blue100 = Color(0xFFF8F9FD),
    Blue200 = Color(0xFFEEF0F8),
    Blue300 = Color(0xFFD5DFFF),
    Blue400 = Color(0xFF93AEFE),
    Blue500 = Color(0xFF668EFE),
    Blue600 = Color(0xFF3A6EFE),
    Blue700 = Color(0xFF265FFF),
    Blue800 = Color(0xFF265FFF),

    Mint50 = Color(0xFFD2FFEC), // TODO : 임시(피그마 수정 대기중)
    Mint100 = Color(0xFFD2FFEC),
    Mint200 = Color(0xFFB0F2DB),
    Mint300 = Color(0xFF99E8D0),
    Mint400 = Color(0xFF81DEC4),
    Mint500 = Color(0xFF6AD4B9),
    Mint600 = Color(0xFF52CAAE),
    Mint700 = Color(0xFF42BA9E),
    Mint800 = Color(0xFF37AF93),

    Red15Per = Color(0xFFF6DADD), // TODO : 임시(피그마 수정 대기중)
    Red20Per = Color(0xFFFDE7E7), // TODO : 임시(피그마 수정 대기중)
    Red500 = Color(0xFFEF5D5D),

    White = Color(0xFFFFFFFF),
    Black = Color(0xFF000000),
)

//TODO: 다크모드 적용시 해당 색상에 대한 반전색 기입
val DarkColorScheme = QrizColorScheme(
    Gray100 = Color(0xFFF0F4F7),
    Gray200 = Color(0xFFD8DFE9),
    Gray300 = Color(0xFFAAB6C9),
    Gray400 = Color(0xFF8F9AAA),
    Gray500 = Color(0xFF747D8B),
    Gray600 = Color(0xFF5A616B),
    Gray700 = Color(0xFF3F444C),
    Gray800 = Color(0xFF24282D),

    Blue50 = Color(0xFFF8F9FD),
    Blue100 = Color(0xFFF8F9FD),
    Blue200 = Color(0xFFEEF0F8),
    Blue300 = Color(0xFFD5DFFF),
    Blue400 = Color(0xFF93AEFE),
    Blue500 = Color(0xFF668EFE),
    Blue600 = Color(0xFF3A6EFE),
    Blue700 = Color(0xFF265FFF),
    Blue800 = Color(0xFF265FFF),

    Mint50 = Color(0xFFD2FFEC), // TODO : 임시(피그마 수정 대기중)
    Mint100 = Color(0xFFD2FFEC),
    Mint200 = Color(0xFFB0F2DB),
    Mint300 = Color(0xFF99E8D0),
    Mint400 = Color(0xFF81DEC4),
    Mint500 = Color(0xFF6AD4B9),
    Mint600 = Color(0xFF52CAAE),
    Mint700 = Color(0xFF42BA9E),
    Mint800 = Color(0xFF37AF93),

    Red15Per = Color(0xFFF6DADD), // TODO : 임시(피그마 수정 대기중)
    Red20Per = Color(0xFFFDE7E7), // TODO : 임시(피그마 수정 대기중)
    Red500 = Color(0xFFEF5D5D),

    White = Color(0xFFFFFFFF),
    Black = Color(0xFF000000),
)

@Immutable
data class QrizColorScheme(
    val Gray100: Color,
    val Gray200: Color,
    val Gray300: Color,
    val Gray400: Color,
    val Gray500: Color,
    val Gray600: Color,
    val Gray700: Color,
    val Gray800: Color,

    val Blue50: Color,
    val Blue100: Color,
    val Blue200: Color,
    val Blue300: Color,
    val Blue400: Color,
    val Blue500: Color,
    val Blue600: Color,
    val Blue700: Color,
    val Blue800: Color,

    val Mint50: Color,
    val Mint100: Color,
    val Mint200: Color,
    val Mint300: Color,
    val Mint400: Color,
    val Mint500: Color,
    val Mint600: Color,
    val Mint700: Color,
    val Mint800: Color,

    val Red15Per: Color,
    val Red20Per: Color,
    val Red500: Color,

    val White: Color,
    val Black: Color,
) {
    companion object {
        val Default = QrizColorScheme(
            Gray100 = Color.Unspecified,
            Gray200 = Color.Unspecified,
            Gray300 = Color.Unspecified,
            Gray400 = Color.Unspecified,
            Gray500 = Color.Unspecified,
            Gray600 = Color.Unspecified,
            Gray700 = Color.Unspecified,
            Gray800 = Color.Unspecified,

            Blue50 = Color.Unspecified,
            Blue100 = Color.Unspecified,
            Blue200 = Color.Unspecified,
            Blue300 = Color.Unspecified,
            Blue400 = Color.Unspecified,
            Blue500 = Color.Unspecified,
            Blue600 = Color.Unspecified,
            Blue700 = Color.Unspecified,
            Blue800 = Color.Unspecified,

            Mint50 = Color.Unspecified,
            Mint100 = Color.Unspecified,
            Mint200 = Color.Unspecified,
            Mint300 = Color.Unspecified,
            Mint400 = Color.Unspecified,
            Mint500 = Color.Unspecified,
            Mint600 = Color.Unspecified,
            Mint700 = Color.Unspecified,
            Mint800 = Color.Unspecified,

            Red15Per = Color.Unspecified,
            Red20Per = Color.Unspecified,
            Red500 = Color.Unspecified,

            White = Color.Unspecified,
            Black = Color.Unspecified
        )
    }
}

val LocalColorScheme = staticCompositionLocalOf {
    QrizColorScheme.Default
}
