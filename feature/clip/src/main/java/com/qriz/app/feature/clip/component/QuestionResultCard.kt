package com.qriz.app.feature.clip.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.theme.Blue100
import com.qriz.app.core.designsystem.theme.Blue400
import com.qriz.app.core.designsystem.theme.Gray500
import com.qriz.app.core.designsystem.theme.Mint600
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.Red700
import com.qriz.app.core.designsystem.R as DSR

@Composable
internal fun QuestionResultCard(
    isCorrect: Boolean,
    number: Int,
    question: String,
    tag: List<String>,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        shadowElevation = 1.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(
                vertical = 20.dp,
                horizontal = 16.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top,
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        if (isCorrect) DSR.drawable.ic_correct
                        else DSR.drawable.ic_incorrect
                    ),
                    contentDescription = null,
                    tint = if (isCorrect) Mint600 else Red700,
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "문제 $number",
                    style = QrizTheme.typography.headline2,
                )
            }

            Text(
                text = question,
                style = QrizTheme.typography.body2,
                color = Gray500
            )

            Row {
                tag.forEach { tag ->
                    QuestionResultTag(
                        tag = tag,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun QuestionResultTag(
    tag: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = tag,
        style = QrizTheme.typography.body2,
        color = Blue400,
        modifier = modifier
            .background(
                color = Blue100,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(
                vertical = 4.dp,
                horizontal = 8.dp,
            )
    )
}

@Preview
@Composable
private fun QuestionResultCardPreview() {
    QrizTheme {
        QuestionResultCard(
            isCorrect = true,
            number = 1,
            question = "문제",
            tag = listOf(
                "tag1",
                "tag2"
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}
