package com.qriz.app.feature.onboard.previewresult.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.ui.test.TestResultDonutChartCard
import com.qriz.app.core.ui.test.model.TestResultItem
import com.qriz.app.feature.onboard.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun PreviewResultDonutChartCard(
    modifier: Modifier = Modifier,
    userName: String,
    totalScore: Int,
    estimatedScore: Float,
    testResultItems: ImmutableList<TestResultItem>
) {
    TestResultDonutChartCard(
        modifier = modifier,
        title = {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        QrizTheme.typography.heading2.toSpanStyle()
                            .copy(fontWeight = FontWeight.Normal)
                    ) {
                        append(stringResource(R.string.test_result_title_user_name, userName))
                    }
                    withStyle(
                        QrizTheme.typography.heading2.toSpanStyle()
                    ) {
                        append(stringResource(R.string.test_result_title_tail))
                    }
                },
                color = Gray800
            )
        },
        subTitle = stringResource(R.string.test_result_expected_score),
        totalScore = totalScore,
        estimatedScore = estimatedScore,
        testResultItems = testResultItems,
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewResultDonutChartCardPreview() {
    QrizTheme {
        PreviewResultDonutChartCard(
            userName = "Qriz",
            totalScore = 100,
            estimatedScore = 50.0F,
            testResultItems = persistentListOf(
                TestResultItem(
                    scoreName = "1과목",
                    score = 50,
                ),
                TestResultItem(
                    scoreName = "2과목",
                    score = 50,
                )
            ),
        )
    }
}
