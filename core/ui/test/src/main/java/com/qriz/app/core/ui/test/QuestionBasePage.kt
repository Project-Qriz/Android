package com.qriz.app.core.ui.test

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme

@Composable
fun QuestionBasePage(
    modifier: Modifier = Modifier,
    questionNum: Int,
    questionText: String,
    description: String? = null,
    actions: (@Composable () -> Unit)? = null,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    QrizTheme.typography.heading2.toSpanStyle()
                        .copy(color = Gray800)
                ) {
                    append(
                        "$questionNum"
                            .padStart(2, '0') + ".  "
                    )
                }

                withStyle(
                    QrizTheme.typography.headline2.toSpanStyle()
                        .copy(color = Gray800)
                ) { append(questionText) }
            },
            modifier = Modifier.padding(bottom = 10.dp)
        )

        if (description != null) {
            QuestionDescriptionCard(
                description = description,
                modifier = Modifier.padding(bottom = 10.dp),
            )
        }

        actions?.invoke()
    }
}
