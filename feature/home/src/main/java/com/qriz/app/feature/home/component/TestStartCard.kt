package com.qriz.app.feature.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.QrizCard
import com.qriz.app.core.designsystem.theme.Gray500
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.feature.home.R

@Composable
fun TestStartCard(
    modifier: Modifier = Modifier,
    isNeedPreviewTest: Boolean,
    onClickMockTest: () -> Unit,
    onClickPreviewTest: () -> Unit
) {
    QrizCard(
        modifier = modifier
            .clickable {
                if (isNeedPreviewTest) onClickPreviewTest()
                else onClickMockTest()
            }
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                if (isNeedPreviewTest) {
                    Text(
                        text = stringResource(R.string.preview_exam),
                        style = QrizTheme.typography.headline2,
                        color = Gray800,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                    )
                    Text(
                        text = stringResource(R.string.test_skill_check),
                        style = QrizTheme.typography.body2,
                        color = Gray500
                    )
                } else {
                    Text(
                        text = stringResource(R.string.take_mock_exam),
                        style = QrizTheme.typography.headline2,
                        color = Gray800,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                    )
                    Text(
                        text = stringResource(R.string.prepare_like_real_exam),
                        style = QrizTheme.typography.body2,
                        color = Gray500
                    )
                }
            }

            val imageRes =
                if (isNeedPreviewTest) painterResource(R.drawable.blue_paper_exam_icon)
                else painterResource(R.drawable.green_pen_exam_icon)

            Image(
                painter = imageRes,
                contentDescription = null,
                modifier = Modifier
                    .width(42.dp)
                    .height(42.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TestStartCardPreview() {
    QrizTheme {
        TestStartCard(
            isNeedPreviewTest = true,
            onClickMockTest = {},
            onClickPreviewTest = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TestStartCardPreview2() {
    QrizTheme {
        TestStartCard(
            isNeedPreviewTest = false,
            onClickMockTest = {},
            onClickPreviewTest = {}
        )
    }
}
