package com.qriz.app.core.ui.common.const

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.theme.Blue500
import com.qriz.app.core.designsystem.theme.Gray300
import com.qriz.app.core.designsystem.theme.White

@Composable
fun ExamCheckBox(
    size: Dp,
    isChecked: Boolean,
    modifier: Modifier = Modifier,
) {
    val sizeValue = size.value
    
    Box(
        modifier = modifier
            .size(size)
            .background(
                color = if (isChecked) Blue500 else Gray300,
                shape = CircleShape,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size((sizeValue * 0.4f).dp)
                .background(
                    color = White,
                    shape = CircleShape,
                )
        )
    }
}
