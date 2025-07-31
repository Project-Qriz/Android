package com.qriz.app.feature.mock_test.guide

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.NavigationType
import com.qriz.app.core.designsystem.component.QrizButton
import com.qriz.app.core.designsystem.component.QrizTopBar
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.feature.mock_test.R
import com.qriz.app.feature.mock_test.guide.component.TestQuestionGuideCard

@Composable
fun MockTestGuideScreen(
    onBack: () -> Unit,
    moveToMockTest: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        QrizTopBar(
            background = Color.Transparent,
            navigationType = NavigationType.BACK,
            onNavigationClick = onBack,
        )

        Text(
            modifier = Modifier.padding(top = 32.dp, start = 24.dp),
            text = stringResource(R.string.mock_test_guide_title),
            style = QrizTheme.typography.heading1.copy(color = Gray800)
        )

        TestQuestionGuideCard()

        Spacer(modifier = Modifier.weight(1f))

        QrizButton(
            modifier = Modifier
                .padding(horizontal = 18.dp, vertical = 16.dp)
                .fillMaxWidth(),
            text = stringResource(R.string.test_start),
            onClick = { moveToMockTest() },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MockTestGuideScreenPreview() {
    QrizTheme {
        MockTestGuideScreen(
            onBack = {},
            moveToMockTest = {}
        )
    }
}
