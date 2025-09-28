package com.qriz.app.feature.mock_test.sessions.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.QrizCard
import com.qriz.app.core.designsystem.theme.Gray100
import com.qriz.app.core.designsystem.theme.Gray500
import com.qriz.app.core.designsystem.theme.Gray600
import com.qriz.app.core.designsystem.theme.Gray700
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.feature.mock_test.R
import com.qriz.app.core.designsystem.R as DSR

@Composable
fun MockTestSessionCard(
    modifier: Modifier = Modifier,
    totalScore: Int,
    completed: Boolean,
    title: String,
    onCardClick: () -> Unit = {},
) {
    QrizCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        border = BorderStroke(
            width = 1.dp,
            color = Gray100
        ),
        elevation = 1.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(
                        if (completed) R.string.session_completed
                        else R.string.session_not_completed
                    ),
                    style = QrizTheme.typography.subhead,
                    color = Gray600
                )

                Text(
                    text = title,
                    style = QrizTheme.typography.headline1,
                    color = Gray700
                )
                Text(
                    text = stringResource(R.string.mock_test_session_total_score, totalScore),
                    style = QrizTheme.typography.body2,
                    color = Gray500
                )
            }

            Icon(
                imageVector = ImageVector.vectorResource(DSR.drawable.ic_keyboard_arrow_right),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Gray800,
            )
        }
    }
}

@Preview
@Composable
private fun MockTestSessionCardPreview() {
    QrizTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            MockTestSessionCard(
                completed = true,
                totalScore = 0,
                title = "1회차"
            )
        }
    }
}
