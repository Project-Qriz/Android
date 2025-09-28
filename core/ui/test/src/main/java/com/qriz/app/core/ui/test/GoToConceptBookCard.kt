package com.qriz.app.core.ui.test

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qriz.app.core.designsystem.component.QrizButton
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme

private val fontStyle = TextStyle(
    fontSize = 18.sp,
    lineHeight = 28.sp,
    fontWeight = FontWeight.Medium,
    color = Gray800,
)

@Composable
fun GoToConceptBookCard(
    modifier: Modifier = Modifier,
    userName: String,
    moveToConceptBook: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = buildAnnotatedString {
                append(stringResource(R.string.user_name, userName))
                withStyle(
                    style = fontStyle.copy(fontWeight = FontWeight.Bold).toSpanStyle()
                ) {
                    append(stringResource(R.string.recommend_concept_book))
                }
                append(stringResource(R.string.go_to_concept_book))
            },
            style = fontStyle
        )

        QrizButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.go_to_concept_book_button),
            onClick = moveToConceptBook
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GoToConceptBookCardPreview() {
    QrizTheme {
        GoToConceptBookCard(
            userName = "홍길동",
            moveToConceptBook = {}
        )
    }
}
