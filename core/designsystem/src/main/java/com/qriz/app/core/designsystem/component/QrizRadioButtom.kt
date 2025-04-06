package com.qriz.app.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.theme.Blue500
import com.qriz.app.core.designsystem.theme.Gray300
import com.qriz.app.core.designsystem.theme.White

@Composable
fun QrizRadioButton(
    modifier: Modifier = Modifier,
    selected: Boolean,
    enabled: Boolean = true,
    onClick: (() -> Unit)?,
    enabledColor: Color = Gray300,
    selectedColor: Color = Blue500,
) {
    val radioColor = if (enabled && selected) selectedColor else enabledColor


    //큰원 r : 작은원 r = 12 : 9.6

    //지름 12.dp
    Box(
        modifier = modifier
            .clickable(
                enabled = enabled,
                onClick = onClick ?: {},
            )
            .background(color = radioColor)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {

        //지름 9.6dp
        Box(
            modifier = Modifier
//                .size((9.6 / 12) * modifier.size)
                .background(color = White)
                .clip(CircleShape)
        )
    }
}

private val RadioButtonDotSize = 9.6.dp
private val RadioStrokeWidth = 7.2.dp
private val RadioButtonPadding = 2.dp
private const val RadioAnimationDuration = 100
val StateLayerSize = 40.0.dp
val IconSize = 20.0.dp

@Preview(showBackground = true)
@Composable
fun QrizRadioButtonPreview() {
    QrizRadioButton(
        selected = true,
        onClick = { }
    )
}
