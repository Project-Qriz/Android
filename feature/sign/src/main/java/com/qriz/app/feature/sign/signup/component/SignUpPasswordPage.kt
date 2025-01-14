package com.qriz.app.feature.sign.signup.component

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
import com.qriz.app.feature.sign.signup.SignUpUiState.Companion.PW_MAX_LENGTH

@Composable
fun SignUpPasswordPage(
    password: String,
    passwordCheck: String,
    passwordErrorMessage: String,
    passwordCheckErrorMessage: String,
    canSignUp: Boolean,
    onChangeUserPw: (String) -> Unit,
    onChangeUserPwCheck: (String) -> Unit,
    onClickSignUp: () -> Unit,
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

    SignUpBasePage(
        title = stringResource(R.string.sign_up_password_page_title),
        subTitle = stringResource(R.string.sign_up_password_page_sub_title),
        buttonText = stringResource(R.string.sign_up_password_page_button_text),
        buttonEnabled = canSignUp,
        onButtonClick = onClickSignUp,
    ) {
        QrizTextFiled(
            value = password,
            onValueChange = onChangeUserPw,
            supportingText = passwordSupportingText,
            singleLine = true,
            //TODO : 변경된 비밀번호 조건에 맞는 hint UI 업데이트 필요 (디자인 수정 대기중)
            hint = stringResource(R.string.sign_up_password_page_hint),
            maxLength = PW_MAX_LENGTH,
            modifier = Modifier.padding(bottom = 12.dp),
            visualTransformation = PasswordVisualTransformation(),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 14.dp,
            )
        )

        QrizTextFiled(
            value = passwordCheck,
            onValueChange = onChangeUserPwCheck,
            supportingText = passwordCheckSupportingText,
            singleLine = true,
            hint = stringResource(R.string.sign_up_password_page_hint_check),
            maxLength = PW_MAX_LENGTH,
            visualTransformation = PasswordVisualTransformation(),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 14.dp,
            )
        )
    }
}
