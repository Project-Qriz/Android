package com.qriz.app.feature.sign.signUp.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.QrizTextFiled
import com.qriz.app.core.designsystem.component.SupportingText
import com.qriz.app.feature.sign.R

@Composable
fun SignUpNamePage(
    name: String,
    validName: Boolean,
    errorMessage: String,
    onNameChanged: (String) -> Unit,
    onNext: () -> Unit,
) {
    val supportingText: SupportingText? = if (errorMessage.isNotEmpty()) SupportingText(
        message = errorMessage,
        color = MaterialTheme.colorScheme.error
    ) else null

    SignUpContent(
        title = stringResource(R.string.sign_up_name_page_title),
        subTitle = stringResource(R.string.sign_up_name_page_sub_title),
        buttonEnabled = validName,
        buttonText = stringResource(R.string.sign_up_name_page_button_text),
        onButtonClick = onNext,
    ) {
        QrizTextFiled(
            value = name,
            supportingText = supportingText,
            onValueChange = onNameChanged,
            singleLine = true,
            hint = stringResource(R.string.sign_up_name_page_hint),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 14.dp,
            )
        )
    }
}
