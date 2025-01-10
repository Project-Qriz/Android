package com.qriz.app.feature.sign.signUp.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.QrizTextFiled
import com.qriz.app.core.designsystem.component.SupportingText
import com.qriz.app.feature.sign.R

@Composable
fun SignUpPasswordPage(
    password: String,
    passwordCheck: String,
    passwordErrorMessage: String,
    passwordCheckErrorMessage: String,
    canSignUp: Boolean,
    onChangePassword: (String) -> Unit,
    onChangePasswordCheck: (String) -> Unit,
    onSignUp: () -> Unit,
) {
    val passwordSupportingText = if (passwordErrorMessage.isNotEmpty()) {
        SupportingText(
            message = passwordErrorMessage,
            color = MaterialTheme.colorScheme.error,
        )
    } else {
        null
    }

    val passwordCheckSupportingText = if (passwordCheckErrorMessage.isNotEmpty()) {
        SupportingText(
            message = passwordCheckErrorMessage,
            color = MaterialTheme.colorScheme.error,
        )
    } else {
        null
    }

    SignUpContent(
        title = stringResource(R.string.sign_up_password_page_title),
        subTitle = stringResource(R.string.sign_up_password_page_sub_title),
        buttonText = stringResource(R.string.sign_up_password_page_button_text),
        buttonEnabled = canSignUp,
        onButtonClick = onSignUp,
    ) {
        QrizTextFiled(
            value = password,
            onValueChange = onChangePassword,
            supportingText = passwordSupportingText,
            singleLine = true,
            hint = stringResource(R.string.sign_up_password_page_hint),
            maxLength = 10,
            modifier = Modifier.padding(bottom = 12.dp),
            visualTransformation = PasswordVisualTransformation(),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 14.dp,
            )
        )

        QrizTextFiled(
            value = passwordCheck,
            onValueChange = onChangePasswordCheck,
            supportingText = passwordCheckSupportingText,
            singleLine = true,
            hint = stringResource(R.string.sign_up_password_page_hint_check),
            maxLength = 10,
            visualTransformation = PasswordVisualTransformation(),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 14.dp,
            )
        )
    }
}
