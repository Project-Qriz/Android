package com.qriz.app.feature.home.component

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
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
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.weekly_custom_concept),
            style = QrizTheme.typography.headline1,
            color = Gray800,
            modifier = Modifier
                .padding(bottom = 12.dp)
        )
        WeeklyCustomConceptCard(
            modifier = Modifier
                .padding(bottom = 8.dp),
            onClick = {}
        )
        WeeklyCustomConceptCard(
            onClick = {}
        )
    }
}

@Composable
fun WeeklyCustomConceptCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    QrizCard(
        modifier = modifier
            .clickable { onClick() }
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
                    onClick = onClick,
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
        WeeklyCustomConcept()
    }
}
