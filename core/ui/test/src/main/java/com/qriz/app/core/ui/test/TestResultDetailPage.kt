package com.qriz.app.core.ui.test

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.theme.Blue100
import com.qriz.app.core.designsystem.theme.Blue300
import com.qriz.app.core.designsystem.theme.Blue500
import com.qriz.app.core.designsystem.theme.Blue800
import com.qriz.app.core.designsystem.theme.Gray300
import com.qriz.app.core.designsystem.theme.Gray600
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.ui.test.model.ScoreDetailItem
import com.qriz.app.core.ui.test.model.ScoreDetailSubjectFilter
import com.qriz.app.core.ui.test.model.TestResultDetailItem
import com.qriz.app.core.ui.test.model.TestResultItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun TestResultDetailPage(
    modifier: Modifier = Modifier,
    selectedSubject: ScoreDetailSubjectFilter,
    totalScore: Int,
    enableFilter: Boolean = false,
    showFilterDropDown: Boolean = false,
    testResultItem: ImmutableList<TestResultItem>,
    resultDetailItems: ImmutableList<TestResultDetailItem>,
    getTestResultColor: (Int) -> Color,
    onClickFilter: () -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(
                horizontal = 18.dp,
                vertical = 24.dp,
            )
    ) {
        item {
            ScoreDetailChartHeader(
                totalScore = totalScore,
                subject = selectedSubject,
                showFilterDropDown = showFilterDropDown,
                enableFilter = enableFilter,
                testResultItem = testResultItem,
                getTestResultColor = getTestResultColor,
                onClickFilter = onClickFilter,
            )
        }

        itemsIndexed(
            items = resultDetailItems,
            key = { _, item -> item.hashCode() },
        ) { index, item ->
            ScoreDetailContainer(
                modifier = Modifier.padding(top = 32.dp),
                title = item.name,
                score = item.score,
                items = item.items,
                color = getTestResultColor(index),
            )
        }
    }
}

@Composable
private fun ScoreDetailChartHeader(
    subject: ScoreDetailSubjectFilter,
    totalScore: Int,
    enableFilter: Boolean,
    showFilterDropDown: Boolean,
    testResultItem: ImmutableList<TestResultItem>,
    getTestResultColor: (Int) -> Color,
    onClickFilter: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = stringResource(R.string.concept_score_analysis),
            style = QrizTheme.typography.heading2.copy(color = Gray800),
        )

        if (enableFilter) {
            SubjectFilter(
                modifier = Modifier,
                subject = subject,
                showDropDown = showFilterDropDown,
                onClickFilter = onClickFilter,
                onSelectSubjectFilter = {},
            )
        } else {
            Text(
                text = stringResource(R.string.total),
                style = QrizTheme.typography.headline3.copy(color = Gray600),
            )
        }

        AnimatedDonutChart(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            totalScore = totalScore,
            testResultItems = testResultItem,
            getTestResultColor = getTestResultColor,
        )
    }
}

private val TEST_RESULT_COLORS = listOf(
    Blue800,
    Blue500,
    Blue300,
    Blue100,
    Gray300
)

private fun getTestResultColor(index: Int): Color =
    if (index > TEST_RESULT_COLORS.lastIndex) TEST_RESULT_COLORS.last()
    else TEST_RESULT_COLORS[index]


@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
private fun TestResultDetailPagePreview() {
    QrizTheme {
        TestResultDetailPage(
            totalScore = 60,
            selectedSubject = ScoreDetailSubjectFilter.TOTAL,
            enableFilter = true,
            showFilterDropDown = true,
            testResultItem = persistentListOf(
                TestResultItem(
                    score = 20,
                    scoreName = "SQL에 대한 이해"
                )
            ),
            getTestResultColor = ::getTestResultColor,
            resultDetailItems = persistentListOf(
                TestResultDetailItem(
                    name = "데이터 모델과 SQL",
                    score = 12,
                    items = persistentListOf(
                        ScoreDetailItem(
                            name = "엔터티",
                            score = 10,
                        ),
                        ScoreDetailItem(
                            name = "속성",
                            score = 2,
                        )
                    )
                ),
                TestResultDetailItem(
                    name = "데이터 모델링의 이해",
                    score = 12,
                    items = persistentListOf(
                        ScoreDetailItem(
                            name = "TCL",
                            score = 10,
                        ),
                        ScoreDetailItem(
                            name = "DDL",
                            score = 2,
                        )
                    )
                )
            ),
            onClickFilter = {},
        )
    }
}


