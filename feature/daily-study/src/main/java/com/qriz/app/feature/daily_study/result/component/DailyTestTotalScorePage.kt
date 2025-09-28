package com.qriz.app.feature.daily_study.result.component

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
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.theme.Blue50
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.Typography
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.core.ui.test.GoToConceptBookCard
import com.qriz.app.core.ui.test.TestQuestionResultCard
import com.qriz.app.core.ui.test.model.TestResultItem
import com.qriz.app.featrue.daily_study.R
import com.qriz.app.feature.daily_study.result.model.DailyTestQuestionResultItem
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun DailyTestTotalScorePage(
    modifier: Modifier = Modifier,
    day: Int,
    userName: String,
    passed: Boolean,
    totalScore: Int,
    testResultItems: ImmutableList<TestResultItem>,
    questionResultItems: ImmutableList<DailyTestQuestionResultItem>,
    getTestResultColor: (Int) -> Color,
    onClickDetail: (() -> Unit)? = null,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        item {
            DailyTestTotalScoreCard(
                modifier = Modifier,
                day = day,
                userName = userName,
                passed = passed,
                totalScore = totalScore,
                testResultItems = testResultItems,
                getTestResultColor = getTestResultColor,
                onClickDetail = onClickDetail,
            )
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
                text = stringResource(R.string.question_results),
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
