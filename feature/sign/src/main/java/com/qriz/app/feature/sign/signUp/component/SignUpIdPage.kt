package com.qriz.app.feature.sign.signUp.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.QrizTextFiled
import com.qriz.app.core.designsystem.component.SupportingText
import com.qriz.app.core.designsystem.theme.Black
import com.qriz.app.feature.sign.R

@Composable
fun SignUpIdPage(
    id: String,
    isAvailableId: Boolean,
    errorMessage: String,
    onIdChanged: (String) -> Unit,
    onCheckDuplicate: () -> Unit,
    onNext: () -> Unit,
) {
    val supportingText = if (errorMessage.isNotEmpty()) {
        SupportingText(
            message = errorMessage,
            color = MaterialTheme.colorScheme.error
        )
    } else {
        SupportingText(
            message = "${id.length}/8",
            color = Black
        )
    }

    SignUpContent(
        title = stringResource(R.string.sign_up_id_page_title),
        subTitle = stringResource(R.string.sign_up_id_page_sub_title),
        buttonText = stringResource(R.string.sign_up_id_page_button_text),
        buttonEnabled = isAvailableId,
        onButtonClick = onNext,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            QrizTextFiled(
                value = id,
                onValueChange = onIdChanged,
                supportingText = supportingText,
                singleLine = true,
                hint = stringResource(R.string.sign_up_id_page_hint),
                maxLength = 8,
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 14.dp,
                )
            )
            OutlinedButton(
                onClick = onCheckDuplicate,
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 14.dp
                ),
                modifier = Modifier.padding(start = 10.dp)
            ) {
                Text(
                    stringResource(R.string.sign_up_id_page_check_duplicate),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}
