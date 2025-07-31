package com.qriz.app.feature.mock_test.guide.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SubjectColumn(
    modifier: Modifier = Modifier,
    subjectName: String,
    questionCountText: String,
    subjectScore: Int,
    questionCountBackgroundColor: Color,
    questionCountTextColor: Color,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        QuestionCountBadge(
            text = questionCountText,
            backgroundColor = questionCountBackgroundColor,
            textColor = questionCountTextColor,
        )

        SubjectRow(
            title = subjectName,
            totalScore = subjectScore,
        )
    }
}
