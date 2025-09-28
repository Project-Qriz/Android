package com.qriz.app.core.ui.test

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.QrizCard
import com.qriz.app.core.designsystem.theme.Blue100
import com.qriz.app.core.designsystem.theme.Blue400
import com.qriz.app.core.designsystem.theme.Gray600
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.Mint600
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.Red700
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun TestQuestionResultCard(
    modifier: Modifier = Modifier,
    number: Int,
    question: String,
    correct: Boolean,
    tags: ImmutableList<String>,
) {
    QrizCard(
        modifier = modifier,
        cornerRadius = 8.dp,
        border = BorderStroke(
            width = 1.dp,
            color = Blue100,
        ),
        elevation = 0.dp,
    ) {
        Column(
            modifier = Modifier.padding(
                vertical = 20.dp,
                horizontal = 12.dp,
            )
        ) {
            Row(
                modifier = Modifier.padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        if (correct) R.drawable.correct_icon
                        else R.drawable.incorrect_icon
                    ),
                    tint = if (correct) Mint600 else Red700,
                    contentDescription = null,
                )

                Text(
                    text = stringResource(
                        R.string.question_number,
                        number
                    ),
                    style = QrizTheme.typography.subhead.copy(
                        color = Gray800,
                        fontWeight = FontWeight.Bold,
                    )
                )
            }

            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = question,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = QrizTheme.typography.body2.copy(color = Gray600),
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tags.forEach { TagChip(tag = it) }
            }
        }
    }
}

@Composable
private fun TagChip(tag: String) {
    Box(
        modifier = Modifier
            .background(
                color = Blue100,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(
                vertical = 4.dp,
                horizontal = 8.dp,
            )
    ) {
        Text(
            text = tag,
            style = QrizTheme.typography.label2.copy(
                color = Blue400,
                fontWeight = FontWeight.Medium,
            )
        )
    }
}

@Preview
@Composable
private fun CorrectTestQuestionResultCardPreview() {
    TestQuestionResultCard(
        number = 1,
        question = "아래 테이블 T<S<R이 각각 다음과 같이 선언되었다. \n" + "다음 중 DELETE FROM T;를 수행한 후에 테이블 R에 남아있는 데이터로 가장 적절한 것은?",
        correct = true,
        tags = persistentListOf(
            "엔터티",
            "식별자"
        )
    )
}

@Preview
@Composable
private fun IncorrectTestQuestionResultCardPreview() {
    TestQuestionResultCard(
        number = 1,
        question = "아래 테이블 T<S<R이 각각 다음과 같이 선언되었다. \n" + "다음 중 DELETE FROM T;를 수행한 후에 테이블 R에 남아있는 데이터로 가장 적절한 것은?",
        correct = false,
        tags = persistentListOf(
            "엔터티",
            "식별자"
        )
    )
}
