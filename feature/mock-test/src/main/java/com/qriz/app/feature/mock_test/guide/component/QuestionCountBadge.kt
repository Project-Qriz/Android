package com.qriz.app.feature.mock_test.guide.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.QrizCard
import com.qriz.app.core.designsystem.theme.Blue100
import com.qriz.app.core.designsystem.theme.Blue400
import com.qriz.app.core.designsystem.theme.QrizTheme

@Composable
fun QuestionCountBadge(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color,
    backgroundColor: Color,
) {
    QrizCard(
        modifier = modifier,
        elevation = 0.dp,
        cornerRadius = 4.dp,
        border = null,
        color = backgroundColor,
    ) {
        Text(
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
            text = text,
            style = QrizTheme.typography.label2.copy(color = textColor),
        )
    }
}

@Preview
@Composable
private fun QuestionCountCardPreview() {
    QrizTheme {
        QuestionCountBadge(
            text = "10 문항",
            textColor = Blue400,
            backgroundColor = Blue100
        )
    }
}
