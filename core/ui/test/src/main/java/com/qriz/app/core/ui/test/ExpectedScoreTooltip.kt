package com.qriz.app.core.ui.test

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.qriz.app.core.designsystem.theme.Gray200
import com.qriz.app.core.designsystem.theme.Gray500
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White

@Composable
internal fun ExpectedScoreTooltip(
    modifier: Modifier = Modifier,
    targetOffset: IntOffset,
    targetSize: IntSize,
    onDismissRequest: () -> Unit,
) {
    var popupSize by remember { mutableStateOf(IntSize.Zero) }

    Popup(
        offset = targetOffset.copy(
            x = (popupSize.width - targetSize.width) / 2,
            y = targetOffset.y - popupSize.height - 10,
        ),
        onDismissRequest = onDismissRequest,
    ) {
        ExpectedScoreTooltipContent(
            modifier = modifier.onGloballyPositioned { popupSize = it.size },
            onDismissRequest = onDismissRequest,
        )
    }
}

@Composable
private fun ExpectedScoreTooltipContent(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
) {
    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = Gray200,
                shape = RoundedCornerShape(8.dp)
            )
            .background(color = White)
            .padding(16.dp),
    ) {
        IconButton(
            modifier = Modifier
                .size(16.dp)
                .align(Alignment.TopEnd),
            onClick = onDismissRequest,
        ) {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.ic_close_outline),
                contentDescription = null,
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.expected_score_tooltip_title),
                style = QrizTheme.typography.body2.copy(
                    color = Gray800,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 24.sp
                )
            )

            Text(
                text = stringResource(R.string.expected_score_tooltip_content),
                style = QrizTheme.typography.caption.copy(color = Gray500)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExpectedScoreTooltipPreview() {
    ExpectedScoreTooltipContent(
        modifier = Modifier,
        onDismissRequest = { },
    )
}
