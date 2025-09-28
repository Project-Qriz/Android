package com.qriz.app.feature.mock_test.result.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.theme.Blue50
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.Typography
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.core.ui.test.GoToConceptBookCard
import com.qriz.app.core.ui.test.TestQuestionResultCard
import com.qriz.app.core.ui.test.model.TestResultItem
import com.qriz.app.feature.mock_test.R
import com.qriz.app.feature.mock_test.result.MockTestResultUiState
import com.qriz.app.feature.mock_test.result.model.HistoricalScoreChartItem
import com.qriz.app.feature.mock_test.result.model.MockTestQuestionResultItem
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun MockTestTotalScorePage(
    modifier: Modifier = Modifier,
    userName: String,
    totalScore: Int,
    showFilterDropDown: Boolean,
    historicalScoreFilter: MockTestResultUiState.HistoricalScoreFilter,
    testResultItems: ImmutableList<TestResultItem>,
    questionResultItems: ImmutableList<MockTestQuestionResultItem>,
    historicalResultItem: ImmutableList<ImmutableList<HistoricalScoreChartItem>>,
    getTestResultColor: (String) -> Color,
    onClickHistoryFilter: () -> Unit,
    onChangeHistoryFilter: (MockTestResultUiState.HistoricalScoreFilter) -> Unit,
    onClickDetail: (() -> Unit)? = null,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        item {
            MockTestScoreCard(
                modifier = Modifier,
                totalScore = totalScore,
                testResultItems = testResultItems,
                getTestResultColor = getTestResultColor,
                onClickDetail = onClickDetail,
                title = {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = QrizTheme.typography.heading2
                                    .copy(fontWeight = FontWeight.Normal)
                                    .toSpanStyle()
                            ) {
                                append(stringResource(R.string.mock_test_result_title_user_name, userName))
                            }
                            withStyle(
                                style = QrizTheme.typography.heading2.toSpanStyle()
                            ) {
                                append(stringResource(R.string.mock_test_result_title))
                            }
                            withStyle(
                                style = QrizTheme.typography.heading2
                                    .copy(fontWeight = FontWeight.Normal)
                                    .toSpanStyle()
                            ) {
                                append(stringResource(R.string.mock_test_result_title_tail))
                            }
                        },
                        color = Gray800,
                        style = QrizTheme.typography.headline2.copy(fontWeight = FontWeight.Medium)
                    )
                },
            )
        }

        if (historicalResultItem.first().size >= 2) {
            item {
                HorizontalDivider(
                    thickness = 16.dp,
                    color = Blue50,
                )
            }

            item {
                MockTestChart(
                    filter = historicalScoreFilter,
                    showFilterDropDown = showFilterDropDown,
                    historicalScores = historicalResultItem,
                    onSubjectFilterChange = onChangeHistoryFilter,
                    onSubjectFilterClick = onClickHistoryFilter,
                )
            }
        }

        item {
            HorizontalDivider(
                thickness = 16.dp,
                color = Blue50,
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = White)
                    .padding(
                        vertical = 24.dp,
                        horizontal = 16.dp,
                    ),
                text = stringResource(R.string.mock_test_question_result),
                style = Typography.heading2.copy(color = Gray800),
            )
        }

        itemsIndexed(
            items = questionResultItems,
            key = { _, item -> item.id },
        ) { index, item ->
            TestQuestionResultCard(
                modifier = Modifier
                    .padding(horizontal = 18.dp)
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                correct = item.correct,
                number = index + 1,
                question = item.question,
                tags = item.tags,
            )
        }

        item {
            GoToConceptBookCard(
                modifier = Modifier.padding(
                    vertical = 24.dp,
                    horizontal = 18.dp,
                ),
                userName = userName,
                moveToConceptBook = {

                },
            )
        }
    }
}

@Preview
@Composable
private fun MockTestTotalScorePagePreview() {
    QrizTheme {

    }
}
