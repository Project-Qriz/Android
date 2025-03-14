package com.qriz.app.feature.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.QrizCard
import com.qriz.app.core.designsystem.theme.Blue100
import com.qriz.app.core.designsystem.theme.Blue400
import com.qriz.app.core.designsystem.theme.Gray500
import com.qriz.app.core.designsystem.theme.Gray700
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.feature.home.R

@Composable
fun WeeklyCustomConcept(
    modifier: Modifier = Modifier,
    isNeedPreviewTest: Boolean,
    horizontalPadding: Dp = 18.dp,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = 12.dp)
                .padding(horizontal = horizontalPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (isNeedPreviewTest) {
                Box(
                    modifier = Modifier
                        .size(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.lock_icon),
                        contentDescription = null,
                    )
                }
            }
            val titleText =
                if (isNeedPreviewTest) stringResource(R.string.weekly_recommend_concept)
                else stringResource(R.string.weekly_custom_concept)
            Text(
                text = titleText,
                style = QrizTheme.typography.headline1,
                color = Gray800,
                modifier = Modifier
                    .weight(1f)
            )
        }

        Column(
            modifier = Modifier
                .blur(if (isNeedPreviewTest) 4.dp else 0.dp)
                .padding(bottom = 32.dp)
        ) {
            WeeklyCustomConceptCard(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .padding(horizontal = horizontalPadding),
                onClick = if (isNeedPreviewTest) null else onClick
            )
            WeeklyCustomConceptCard(
                modifier = Modifier
                    .padding(horizontal = horizontalPadding),
                onClick = if (isNeedPreviewTest) null else onClick
            )
        }
    }
}

@Composable
fun WeeklyCustomConceptCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    QrizCard(
        modifier = modifier
            .clickable(
                enabled = onClick != null,
                onClick = { onClick?.invoke() }
            ),
    ) {
        Column(
            modifier = Modifier
                .padding(
                    vertical = 12.dp,
                    horizontal = 18.dp
                )
        ) {
            Text(
                "데이터 모델의 이해",
                style = QrizTheme.typography.headline2,
                color = Gray700,
                modifier = Modifier
                    .padding(bottom = 2.dp)
            )
            Row(
                modifier = Modifier
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    "1과목",
                    style = QrizTheme.typography.label2.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Gray500,
                    modifier = Modifier
                        .weight(1f)
                )
                IconButton(
                    modifier = Modifier
                        .size(20.dp),
                    enabled = onClick != null,
                    onClick = { onClick?.invoke() },
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.arrow_right_icon),
                        contentDescription = null,
                        tint = Gray500
                    )
                }
            }
            QrizCard(
                color = Blue100,
                cornerRadius = 4.dp,
                elevation = 0.dp,
                border = null
            ) {
                Box(
                    modifier = Modifier
                        .padding(
                            vertical = 4.dp,
                            horizontal = 8.dp
                        )
                ) {
                    Text(
                        text = "출제율 상",
                        style = QrizTheme.typography.label2,
                        color = Blue400
                    )
                }
            }


        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WeeklyCustomConceptPreview() {
    QrizTheme {
        WeeklyCustomConcept(
            isNeedPreviewTest = false,
            onClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WeeklyCustomConceptPreview2() {
    QrizTheme {
        WeeklyCustomConcept(
            isNeedPreviewTest = true,
            onClick = {},
        )
    }
}

