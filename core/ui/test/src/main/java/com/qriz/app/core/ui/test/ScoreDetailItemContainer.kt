package com.qriz.app.core.ui.test

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.theme.Black
import com.qriz.app.core.designsystem.theme.Blue500
import com.qriz.app.core.designsystem.theme.Gray100
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.ui.test.model.ScoreDetailItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ScoreDetailContainer(
    modifier: Modifier = Modifier,
    score: Int,
    title: String,
    color: Color,
    items: ImmutableList<ScoreDetailItem>
) {
    Column(modifier = modifier) {
        BottomScoreRowItem(
            modifier = Modifier.padding(bottom = 16.dp),
            score = score,
            scoreName = title,
            scoreColor = color,
        )

        items.forEachIndexed { index, item ->
            ScoreDetailItemRow(
                name = item.name,
                score = item.score,
            )

            if (index < items.size - 1) {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Gray100,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun ScoreDetailItemRow(
    name: String,
    score: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = name,
            style = QrizTheme.typography.body2.copy(color = Black),
        )

        Text(
            text = "${score}점",
            style = QrizTheme.typography.body2.copy(
                color = Gray800,
                fontWeight = FontWeight.Bold,
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScoreDetailItemPreview() {
    QrizTheme {
        ScoreDetailContainer(
            modifier = Modifier.fillMaxWidth(),
            score = 40,
            title = "데이터 모델과 SQL",
            color = Blue500,
            items = persistentListOf(
                ScoreDetailItem(
                    score = 20,
                    name = "엔터티"
                ),
                ScoreDetailItem(
                    score = 20,
                    name = "속성",
                )
            )
        )
    }
}
