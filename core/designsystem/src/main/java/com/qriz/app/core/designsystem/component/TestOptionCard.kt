package com.qriz.app.core.designsystem.component

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.theme.Black
import com.qriz.app.core.designsystem.theme.Gray100
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White

@Composable
fun TestOptionCard(
    state: TestOptionState,
    number: Int,
    content: String,
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
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape,
            )
        }

        TestOptionState.SelectedAndIncorrect -> {
            Modifier.background(
                color = MaterialTheme.colorScheme.error,
                shape = CircleShape,
            )
        }
    }

    val backgroundColor = when(state) {
        TestOptionState.None -> MaterialTheme.colorScheme.surface
        TestOptionState.SelectedOrCorrect -> MaterialTheme.colorScheme.primaryContainer
        TestOptionState.SelectedAndIncorrect -> Color(0x33EF5D5D)
    }

    val textColor = when(state) {
        TestOptionState.None -> Black
        TestOptionState.SelectedOrCorrect,
        TestOptionState.SelectedAndIncorrect -> White
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = backgroundColor,
        shape = RoundedCornerShape(10.dp),
        border = if(state == TestOptionState.None) {
            BorderStroke(
                color = Gray100,
                width = 1.dp,
            )
        } else {
            null
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(
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
                    style = MaterialTheme.typography.titleMedium,
                    color = textColor,
                )
            }

            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFF8F9FD,
)
@Composable
private fun TestOptionCardPreview() {
    QrizTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TestOptionCard(
                state = TestOptionState.None,
                number = 1,
                content = "선택지 1번"
            )
            TestOptionCard(
                state = TestOptionState.SelectedOrCorrect,
                number = 2,
                content = "선택지 2번"
            )

            TestOptionCard(
                state = TestOptionState.SelectedAndIncorrect,
                number = 3,
                content = "선택지 3번"
            )
        }
    }
}

enum class TestOptionState {
    None, SelectedOrCorrect, SelectedAndIncorrect;
}
