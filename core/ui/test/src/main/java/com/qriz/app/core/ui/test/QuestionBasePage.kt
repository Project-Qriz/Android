package com.qriz.app.core.ui.test

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.theme.Black
import com.qriz.app.core.designsystem.theme.Blue50
import com.qriz.app.core.designsystem.theme.Blue500
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
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Blue50
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
            ) {
                Text(
                    text = questionNum.toString().padStart(2, '0') + ".",
                    style = QrizTheme.typography.headline4,
                    color = Blue500
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = questionText,
                    style = QrizTheme.typography.headline1,
                    color = Gray800
                )

                if (description != null) {

                    Spacer(modifier = Modifier.height(16.dp))

                    QuestionDescriptionCard(
                        description = description,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        actions?.invoke()
    }
}
