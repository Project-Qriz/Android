package com.qriz.app.feature.mock_test.result.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.core.ui.test.TestResultDetailPage
import com.qriz.app.core.ui.test.model.ScoreDetailSubjectFilter
import com.qriz.app.core.ui.test.model.TestResultDetailItem
import com.qriz.app.core.ui.test.model.TestResultItem
import kotlinx.collections.immutable.ImmutableList

@Composable
fun MockTestDetailScorePage(
    selectedSubjectFilter: ScoreDetailSubjectFilter,
    totalScore: Int,
    showFilterDropDown: Boolean,
    testResultItem: ImmutableList<TestResultItem>,
    resultDetailItems: ImmutableList<TestResultDetailItem>,
    getTestResultColor: (String) -> Color,
    onSelectFilter: (ScoreDetailSubjectFilter) -> Unit,
    onClickFilter: () -> Unit,
) {
    TestResultDetailPage(
        modifier = Modifier.background(color = White).fillMaxSize(),
        selectedSubject = selectedSubjectFilter,
        totalScore = totalScore,
        enableFilter = true,
        showFilterDropDown = showFilterDropDown,
        testResultItem = testResultItem,
        resultDetailItems = resultDetailItems,
        getTestResultColor = { getTestResultColor(testResultItem[it].scoreName) },
        onSelectFilter = onSelectFilter,
        onClickFilter = onClickFilter,
    )
}
