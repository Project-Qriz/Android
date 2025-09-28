package com.qriz.app.feature.mock_test.guide.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.QrizCard
import com.qriz.app.core.designsystem.theme.Blue100
import com.qriz.app.core.designsystem.theme.Blue400
import com.qriz.app.core.designsystem.theme.Gray400
import com.qriz.app.core.designsystem.theme.Mint50
import com.qriz.app.core.designsystem.theme.Mint800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.feature.mock_test.R

@Composable
fun TestQuestionGuideCard(
    modifier: Modifier = Modifier,
) {
    QrizCard(
        modifier = modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 32.dp),
        cornerRadius = 16.dp,
        elevation = 1.dp,
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SubjectColumn(
                modifier = Modifier.padding(bottom = 8.dp),
                subjectName = stringResource(R.string.data_modeling_understanding),
                questionCountText = stringResource(R.string.subject_question_count, 10),
                questionCountBackgroundColor = Blue100,
                questionCountTextColor = Blue400,
                subjectScore = 20,
            )

            SubjectColumn(
                modifier = Modifier.padding(bottom = 32.dp),
                subjectName = stringResource(R.string.sql_basic_and_application),
                questionCountText = stringResource(R.string.subject_question_count, 40),
                questionCountBackgroundColor = Blue100,
                questionCountTextColor = Blue400,
                subjectScore = 80,
            )

            SubjectColumn(
                modifier = Modifier.padding(bottom = 16.dp),
                subjectName = stringResource(R.string.question_total),
                questionCountText = stringResource(R.string.total_question_count, 50),
                questionCountBackgroundColor = Mint50,
                questionCountTextColor = Mint800,
                subjectScore = 100,
            )

            Text(
                text = stringResource(R.string.mock_test_limit_time_guide),
                style = QrizTheme.typography.caption.copy(color = Gray400)
            )
        }
    }
}

@Preview
@Composable
private fun TestQuestionGuideCardPreview() {
    QrizTheme {
        TestQuestionGuideCard()
    }
}
