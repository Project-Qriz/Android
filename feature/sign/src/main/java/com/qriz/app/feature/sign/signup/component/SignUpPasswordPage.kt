package com.qriz.app.feature.sign.signup.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.QrizTextFiled
import com.qriz.app.core.designsystem.component.SupportingText
import com.qriz.app.core.designsystem.theme.Black
import com.qriz.app.core.designsystem.theme.Blue500
import com.qriz.app.core.designsystem.theme.Gray200
import com.qriz.app.core.designsystem.theme.Gray300
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.Red700
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.feature.sign.R
import com.qriz.app.feature.sign.component.FormatStateRow
import com.qriz.app.feature.sign.signup.SignUpUiState
import com.quiz.app.core.data.user.user_api.model.PW_MAX_LENGTH

@Composable
fun SignUpPasswordPage(
    password: String,
    passwordCheck: String,
    isVisiblePassword: Boolean,
    isVisiblePasswordCheck: Boolean,
    isPasswordValidFormat: Boolean,
    isPasswordValidLength: Boolean,
    isEqualsPassword: Boolean,
    focusState: SignUpUiState.FocusState,
    passwordCheckErrorMessage: String,
    canSignUp: Boolean,
    onChangeUserPw: (String) -> Unit,
    onChangeUserPwCheck: (String) -> Unit,
    onChangePasswordVisibility: (Boolean) -> Unit,
    onChangePasswordCheckVisibility: (Boolean) -> Unit,
    onClickSignUp: () -> Unit,
    onChangeFocusState: (SignUpUiState.FocusState) -> Unit,
) {
    val passwordCheckBorderColor = when {
        passwordCheckErrorMessage.isNotEmpty() -> Red700
        focusState == SignUpUiState.FocusState.PW_CHECK -> Black
        else -> Gray200
    }

    val passwordCheckSupportingText = if (passwordCheckErrorMessage.isNotEmpty()) {
        SupportingText(
            message = passwordCheckErrorMessage,
            color = Red700,
        )
    } else {
        null
    }

    SignUpBasePage(
        title = stringResource(R.string.sign_up_password_page_title),
        buttonText = stringResource(R.string.sign_up_password_page_button_text),
        buttonEnabled = canSignUp,
        onButtonClick = onClickSignUp,
    ) {
        QrizTextFiled(
            value = password,
            onValueChange = onChangeUserPw,
            hint = stringResource(R.string.sign_up_password_page_hint),
            borderStroke = BorderStroke(
                width = 1.dp,
                color = if (focusState == SignUpUiState.FocusState.PW) Gray800 else Gray200,
            ),
            containerColor = White,
            maxLength = PW_MAX_LENGTH,
            singleLine = true,
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 14.dp,
            ),
            visualTransformation = if (isVisiblePassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailing = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (password.isNotEmpty()) {
                        IconButton(
                            onClick = { onChangeUserPw("") },
                            modifier = Modifier.size(20.dp),
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.delete_icon),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = Gray300,
                            )
                        }
                    }

                    IconButton(
                        onClick = { onChangePasswordVisibility(isVisiblePassword.not()) },
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(24.dp),
                    ) {
                        Icon(
                            painter = if (isVisiblePassword) painterResource(com.qriz.app.core.designsystem.R.drawable.ic_visible_password)
                            else painterResource(com.qriz.app.core.designsystem.R.drawable.ic_invisible_password),
                            contentDescription = null,
                        )
                    }
                }
            },
            modifier = Modifier
                .padding(
                    top = 20.dp,
                    bottom = 5.dp
                )
                .onFocusChanged { if (it.isFocused) onChangeFocusState(SignUpUiState.FocusState.PW) },
        )

        FormatStateRow(
            message = stringResource(R.string.password_format),
            isFormatValid = isPasswordValidFormat,
        )

        FormatStateRow(
            message = stringResource(R.string.password_length),
            isFormatValid = isPasswordValidLength,
        )

        QrizTextFiled(
            value = passwordCheck,
            onValueChange = onChangeUserPwCheck,
            hint = stringResource(R.string.sign_up_password_page_hint_check),
            borderStroke = BorderStroke(
                width = 1.dp,
                color = passwordCheckBorderColor,
            ),
            containerColor = White,
            maxLength = PW_MAX_LENGTH,
            singleLine = true,
            visualTransformation = if (isVisiblePasswordCheck) VisualTransformation.None else PasswordVisualTransformation(),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 14.dp,
            ),
            supportingText = passwordCheckSupportingText,
            trailing = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (isEqualsPassword.not() && passwordCheck.isNotEmpty()) {
                        IconButton(
                            onClick = { onChangeUserPwCheck("") },
                            modifier = Modifier.size(20.dp),
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.delete_icon),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = Gray300,
                            )
                        }
                    }

                    if (isEqualsPassword && passwordCheck.isNotEmpty()) {
                        Icon(
                            painter = painterResource(R.drawable.check_icon),
                            contentDescription = null,
                            tint = Blue500,
                        )
                    }

                    IconButton(
                        onClick = { onChangePasswordCheckVisibility(isVisiblePasswordCheck.not()) },
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(24.dp),
                    ) {
                        Icon(
                            painter = if (isVisiblePasswordCheck) painterResource(com.qriz.app.core.designsystem.R.drawable.ic_visible_password)
                            else painterResource(com.qriz.app.core.designsystem.R.drawable.ic_invisible_password),
                            contentDescription = null
                        )
                    }
                }
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .onFocusChanged { if (it.isFocused) onChangeFocusState(SignUpUiState.FocusState.PW_CHECK) },
        )
    }
}
