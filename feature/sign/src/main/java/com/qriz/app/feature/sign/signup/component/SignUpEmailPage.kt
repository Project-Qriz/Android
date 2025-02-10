package com.qriz.app.feature.sign.signup.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.QrizTextFiled
import com.qriz.app.core.designsystem.component.SupportingText
import com.qriz.app.core.designsystem.theme.Red700
import com.qriz.app.feature.sign.R

@Composable
fun SignUpEmailPage(
    email: String,
    errorMessage: String,
    isValidEmail: Boolean,
    onChangeEmail: (String) -> Unit,
    onClickNextPage: () -> Unit,
) {
    val supportingText: SupportingText? = if (errorMessage.isNotBlank()) SupportingText(
        message = errorMessage,
        color = Red700
    ) else null

    SignUpBasePage(
        title = stringResource(R.string.sign_up_email_page_title),
        subTitle = stringResource(R.string.sign_up_email_page_sub_title),
        buttonEnabled = isValidEmail,
        buttonText = stringResource(R.string.sign_up_email_page_button_text),
        onButtonClick = onClickNextPage,
    ) {
        QrizTextFiled(
            value = email,
            supportingText = supportingText,
            onValueChange = onChangeEmail,
            singleLine = true,
            hint = stringResource(R.string.sign_up_email_page_hint),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 14.dp,
            )
        )
    }
}
