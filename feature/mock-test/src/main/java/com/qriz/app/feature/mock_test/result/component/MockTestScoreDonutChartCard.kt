package com.qriz.app.feature.mock_test.result.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.qriz.app.core.ui.test.TestResultDonutChartCard
import com.qriz.app.core.ui.test.model.TestResultItem
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun MockTestScoreCard(
    modifier: Modifier = Modifier,
    totalScore: Int,
    testResultItems: ImmutableList<TestResultItem>,
    title: @Composable () -> Unit,
    getTestResultColor: (String) -> Color,
    onClickDetail: (() -> Unit)? = null,
) {
    TestResultDonutChartCard(
        modifier = modifier,
        title = title,
        totalScore = totalScore,
        testResultItems = testResultItems,
        getTestResultColor = { getTestResultColor(testResultItems[it].scoreName) },
        onClickDetail = onClickDetail,
    )
}
