package com.qriz.app.core.ui.test

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.QrizButton
import com.qriz.app.core.designsystem.theme.Gray400
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme

@Composable
fun TestPageBottomNavigator(
    currentPage: Int,
    totalQuestionsCount: Int,
    canTurnNextPage: Boolean,
    onNextPage: () -> Unit,
    onPreviousPage: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 18.dp,
                vertical = 8.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        //TODO : 버튼 색상 수정 필요
        QrizButton(
            enable = currentPage > 0,
            text = stringResource(R.string.previous),
            modifier = Modifier.width(90.dp),
            onClick = onPreviousPage,
        )
        Text(
            text = buildAnnotatedString {
                withStyle(
                    QrizTheme.typography.headline2.toSpanStyle()
                        .copy(color = Gray800)
                ) {
                    append("$currentPage")
                }

                withStyle(
                    QrizTheme.typography.body1.toSpanStyle()
                        .copy(color = Gray400)
                ) { append(" ${stringResource(R.string.slash)} ") }

                withStyle(
                    QrizTheme.typography.body1.toSpanStyle()
                        .copy(color = Gray400)
                ) { append("$totalQuestionsCount") }
            },
            textAlign = TextAlign.Center,
            style = QrizTheme.typography.body1,
            modifier = Modifier.weight(1f),
        )
        QrizButton(
            enable = canTurnNextPage,
            text = stringResource(R.string.next),
            modifier = Modifier.width(90.dp),
            onClick = onNextPage,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TestPageBottomNavigatorPreview() {
    TestPageBottomNavigator(
        currentPage = 1,
        totalQuestionsCount = 10,
        canTurnNextPage = true,
        onNextPage = {},
        onPreviousPage = {}
    )
}
