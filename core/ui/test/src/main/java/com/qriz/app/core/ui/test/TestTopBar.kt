package com.qriz.app.core.ui.test

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.NavigationType
import com.qriz.app.core.designsystem.component.QrizTopBar
import com.qriz.app.core.designsystem.theme.Blue600
import com.qriz.app.core.designsystem.theme.Gray200
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.Red800
import com.qriz.app.core.designsystem.theme.White

@Composable
fun TestTopBar(
    remainTimeText: String,
    progressPercent: Float,
    testTimeType: TestTimeType,
    onCancel: () -> Unit,
) {
    Column {
        QrizTopBar(
            navigationType = NavigationType.CANCEL,
            onNavigationClick = onCancel,
            background = White,
            actions = {

                val timeNoticeTitle =
                    when (testTimeType) {
                        TestTimeType.TOTAL -> stringResource(R.string.total_remaining_time)
                        TestTimeType.INDIVIDUAL -> stringResource(R.string.each_question_remaining_time)
                    }

                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            QrizTheme.typography.body2.toSpanStyle()
                                .copy(color = Gray800)
                        ) {
                            append(timeNoticeTitle)
                        }
                        withStyle(
                            QrizTheme.typography.subhead.toSpanStyle()
                                .copy(color = Red800)
                        ) {
                            append("  $remainTimeText")
                        }
                    },
                    modifier = Modifier.padding(end = 15.dp)
                )
            },
        )

        LinearProgressIndicator(
            progress = { progressPercent },
            trackColor = Gray200,
            color = Blue600,
            strokeCap = StrokeCap.Round,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

enum class TestTimeType {
    TOTAL,
    INDIVIDUAL
}

@Preview(showBackground = true)
@Composable
fun TestTopBarTotalTimePreview() {
    TestTopBar(
        remainTimeText = "10:45",
        progressPercent = 0.7f,
        testTimeType = TestTimeType.TOTAL,
        onCancel = {}
    )
}

@Preview(showBackground = true)
@Composable
fun TestTopBarIndividualTimePreview() {
    TestTopBar(
        remainTimeText = "10:45",
        progressPercent = 0.7f,
        testTimeType = TestTimeType.INDIVIDUAL,
        onCancel = {}
    )
}

