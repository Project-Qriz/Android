package com.qriz.app.feature.daily_study.result.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.ui.test.TestResultDonutChartCard
import com.qriz.app.core.ui.test.model.TestResultItem
import com.qriz.app.featrue.daily_study.R
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun DailyTestTotalScoreCard(
    modifier: Modifier = Modifier,
    day: Int,
    userName: String,
    passed: Boolean,
    totalScore: Int,
    testResultItems: ImmutableList<TestResultItem>,
    getTestResultColor: (Int) -> Color,
    onClickDetail: (() -> Unit)? = null,
) {
    Column {
        TestResultDonutChartCard(
            modifier = modifier,
            isLessScore = passed.not(),
            title = {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = QrizTheme.typography.heading2
                                .copy(fontWeight = FontWeight.Normal)
                                .toSpanStyle()
                        ) {
                            append(stringResource(R.string.daily_test_result_title_user_name, userName))
                        }
                        withStyle(
                            style = QrizTheme.typography.heading2.toSpanStyle()
                        ) {
                            append(stringResource(R.string.daily_test_result))
                        }
                        withStyle(
                            style = QrizTheme.typography.heading2
                                .copy(fontWeight = FontWeight.Normal)
                                .toSpanStyle()
                        ) {
                            append(stringResource(R.string.daily_test_result_title_tail))
                        }
                    },
                    color = Gray800,
                    style = QrizTheme.typography.headline2.copy(fontWeight = FontWeight.Medium)
                )
            },
            totalScore = totalScore,
            chartTitle = stringResource(R.string.day_number, day),
            testResultItems = testResultItems,
            getTestResultColor = getTestResultColor,
            onClickDetail = onClickDetail,
        )
    }
}
