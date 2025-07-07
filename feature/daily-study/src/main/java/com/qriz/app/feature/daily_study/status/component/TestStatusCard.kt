package com.qriz.app.feature.daily_study.status.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.QrizCard
import com.qriz.app.core.designsystem.theme.Blue500
import com.qriz.app.core.designsystem.theme.Gray100
import com.qriz.app.core.designsystem.theme.Gray200
import com.qriz.app.core.designsystem.theme.Gray500
import com.qriz.app.core.designsystem.theme.Gray700
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.Red700
import com.qriz.app.core.designsystem.theme.Red800
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.featrue.daily_study.R
import com.qriz.app.core.designsystem.R as DSR

@Composable
internal fun TestStatusCard(
    score: Double,
    canRetry: Boolean,
    statusTextResId: Int,
    statusTextColor: Color,
    backgroundColor: Color,
    iconColor: Color,
    onClick: (() -> Unit)? = null,
) {
    Row (
        modifier = Modifier
            .clickable { onClick?.invoke() }
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(
            vertical = 12.dp,
            horizontal = 18.dp
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(statusTextResId),
                style = QrizTheme.typography.body2,
                color = statusTextColor,
                modifier = Modifier.padding(bottom = 2.dp)
            )

            Text(
                text = stringResource(R.string.daily_test),
                style = QrizTheme.typography.headline1,
                color = Gray700,
            )

            Text(
                text = stringResource(R.string.total_scroe, score),
                style = QrizTheme.typography.body2,
                color = Gray500,
            )

            if (canRetry) {
                QrizCard(
                    color = Red800.copy(alpha = 0.14f),
                    cornerRadius = 4.dp,
                    elevation = 0.dp,
                    border = null
                ) {
                    Box(
                        modifier = Modifier.padding(
                            vertical = 4.dp,
                            horizontal = 8.dp
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.score_low),
                            style = QrizTheme.typography.label2,
                            color = Red800
                        )
                    }
                }
            }
        }

        IconButton(
            modifier = Modifier.size(20.dp),
            enabled = onClick != null,
            onClick = { onClick?.invoke() },
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = DSR.drawable.ic_keyboard_arrow_right),
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Preview
@Composable
private fun TestStatusCardPassedPreview() {
    QrizTheme {
        TestStatusCard(
            score = 85.0,
            canRetry = false,
            statusTextResId = R.string.study_complete,
            statusTextColor = Blue500,
            backgroundColor = White,
            iconColor = Gray800,
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun TestStatusCardBeforePreview() {
    QrizTheme {
        TestStatusCard(
            score = 85.0,
            canRetry = false,
            statusTextResId = R.string.study_not_available,
            statusTextColor = Red700,
            backgroundColor = Gray100,
            iconColor = Gray200,
            onClick = {},
        )
    }
}
