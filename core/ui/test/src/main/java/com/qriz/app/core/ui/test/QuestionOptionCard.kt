package com.qriz.app.core.ui.test

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.theme.Black
import com.qriz.app.core.designsystem.theme.Blue100
import com.qriz.app.core.designsystem.theme.Blue500
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.Red700Opacity14
import com.qriz.app.core.designsystem.theme.Red700
import com.qriz.app.core.designsystem.theme.White

@Composable
fun QuestionOptionCard(
    state: TestOptionState,
    number: Int,
    optionDescription: String,
    modifier: Modifier = Modifier,
) {
    val numberBackground = when (state) {
        TestOptionState.None -> {
            Modifier.border(
                width = 1.dp,
                color = Black,
                shape = CircleShape,
            )
        }

        TestOptionState.SelectedOrCorrect -> {
            Modifier.background(
                color = Blue500,
                shape = CircleShape,
            )
        }

        TestOptionState.SelectedAndIncorrect -> {
            Modifier.background(
                color = Red700,
                shape = CircleShape,
            )
        }
    }

    val backgroundColor = when (state) {
        TestOptionState.None -> White
        TestOptionState.SelectedOrCorrect -> Blue100
        TestOptionState.SelectedAndIncorrect -> Red700Opacity14
    }

    val numTextColor = when (state) {
        TestOptionState.None -> Gray800
        TestOptionState.SelectedOrCorrect,
        TestOptionState.SelectedAndIncorrect -> White
    }

    val optionDescriptionColor = when (state) {
        TestOptionState.None -> Gray800
        TestOptionState.SelectedOrCorrect -> Blue500
        TestOptionState.SelectedAndIncorrect -> Gray800
    }
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = backgroundColor,
        shape = RoundedCornerShape(10.dp),
        border = if (state == TestOptionState.None) {
            BorderStroke(
                color = Blue100,
                width = 1.dp,
            )
        } else {
            null
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 18.dp,
                    vertical = 12.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .then(numberBackground),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = number.toString(),
                    style = QrizTheme.typography.headline2,
                    color = numTextColor,
                )
            }

            Text(
                text = optionDescription,
                style = QrizTheme.typography.body2,
                color = optionDescriptionColor,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TestOptionCardPreview() {
    QrizTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuestionOptionCard(
                state = TestOptionState.None,
                number = 1,
                optionDescription = "선택지 1번"
            )
            QuestionOptionCard(
                state = TestOptionState.SelectedOrCorrect,
                number = 2,
                optionDescription = "선택지 2번"
            )
            QuestionOptionCard(
                state = TestOptionState.SelectedAndIncorrect,
                number = 3,
                optionDescription = "선택지 3번"
            )
        }
    }
}

enum class TestOptionState {
    None,
    SelectedOrCorrect,
    SelectedAndIncorrect
}
