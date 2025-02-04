package com.qriz.app.core.ui.test

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.qriz.app.core.designsystem.theme.Gray700
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme

@Composable
fun TestPageBottomNavigator(
    currentIndex: Int,
    lastIndex: Int,
    canTurnNextPage: Boolean,
    onClickNextPage: () -> Unit,
    onClickSubmit: () -> Unit,
    onClickPreviousPage: () -> Unit,
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
        if (currentIndex > 0) {
            QrizButton(
                text = stringResource(R.string.previous),
                modifier = Modifier.width(90.dp),
                onClick = onClickPreviousPage,
                containerColor = Gray700
            )
        } else {
            Spacer(Modifier.width(90.dp))
        }

        Text(
            text = buildAnnotatedString {
                withStyle(
                    QrizTheme.typography.headline2.toSpanStyle()
                        .copy(color = Gray800)
                ) {
                    append(
                        "${currentIndex + 1}"
                            .padStart(2, '0')
                    )
                }

                withStyle(
                    QrizTheme.typography.body1.toSpanStyle()
                        .copy(color = Gray400)
                ) { append(" ${stringResource(R.string.slash)} ") }

                withStyle(
                    QrizTheme.typography.body1.toSpanStyle()
                        .copy(color = Gray400)
                ) {
                    append(
                        "${lastIndex + 1}"
                            .padStart(2, '0')
                    )
                }
            },
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )

        if (canTurnNextPage) {
            val isLastPage = currentIndex >= lastIndex
            val nextButtonText =
                if (isLastPage) stringResource(R.string.submit)
                else stringResource(R.string.next)

            QrizButton(
                text = nextButtonText,
                modifier = Modifier.width(90.dp),
                onClick = if (isLastPage) onClickSubmit else onClickNextPage,
                containerColor = Gray700
            )
        } else {
            Spacer(Modifier.width(90.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TestPageBottomNavigatorFirstPagePreview() {
    QrizTheme {
        TestPageBottomNavigator(
            currentIndex = 0,
            lastIndex = 19,
            canTurnNextPage = false,
            onClickNextPage = {},
            onClickPreviousPage = {},
            onClickSubmit = {}
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun TestPageBottomNavigatorSelectedFirstPagePreview() {
    QrizTheme {
        TestPageBottomNavigator(
            currentIndex = 0,
            lastIndex = 19,
            canTurnNextPage = true,
            onClickNextPage = {},
            onClickPreviousPage = {},
            onClickSubmit = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TestPageBottomNavigatorSelectedMiddlePagePreview() {
    QrizTheme {
        TestPageBottomNavigator(
            currentIndex = 5,
            lastIndex = 19,
            canTurnNextPage = true,
            onClickNextPage = {},
            onClickPreviousPage = {},
            onClickSubmit = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TestPageBottomNavigatorSelectedLastPagePreview() {
    QrizTheme {
        TestPageBottomNavigator(
            currentIndex = 19,
            lastIndex = 19,
            canTurnNextPage = true,
            onClickNextPage = {},
            onClickPreviousPage = {},
            onClickSubmit = {}
        )
    }
}
