package com.qriz.app.feature.sign.signup.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.QrizTextFiled
import com.qriz.app.core.designsystem.component.SupportingText
import com.qriz.app.core.designsystem.theme.Blue200
import com.qriz.app.core.designsystem.theme.Blue600
import com.qriz.app.core.designsystem.theme.Gray200
import com.qriz.app.core.designsystem.theme.Gray300
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.Mint800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.Red700
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.feature.sign.R
import com.qriz.app.feature.sign.signup.SignUpUiState
import com.quiz.app.core.data.user.user_api.model.AUTH_NUMBER_MAX_LENGTH

@Composable
fun SignUpAuthPage(
    email: String,
    isValidEmail: Boolean,
    authNumber: String,
    authTimerText: String,
    showAuthNumberLayout: Boolean,
    verifiedAuthNumber: Boolean,
    enableInputAuthNumber: Boolean,
    emailSupportingTextResId: Int,
    authNumberSupportingTextResId: Int,
    enableAuthNumVerifyButton: Boolean,
    isTimeExpiredEmailAuth: Boolean,
    focusState: SignUpUiState.FocusState,
    onChangeFocus: (SignUpUiState.FocusState) -> Unit,
    onEmailChanged: (String) -> Unit,
    onSendAuthNumberEmail: () -> Unit,
    onAuthNumberChanged: (String) -> Unit,
    onVerifyAuthNumber: () -> Unit,
    onClickNextPage: () -> Unit,
) {
    val emailFocusRequester = remember { FocusRequester() }

    val emailBorderColor = when {
        isValidEmail -> Mint800
        focusState == SignUpUiState.FocusState.EMAIL -> Gray800
        else -> Gray200
    }

    val authNumberSupportingTextColor = when (authNumberSupportingTextResId) {
        R.string.success_send_email_auth_number, R.string.success_verify_auth_number -> Mint800
        else -> Red700
    }

    val authNumberBorderColor = when {
        authNumberSupportingTextResId == R.string.fail_verify_auth_number -> Red700
        focusState == SignUpUiState.FocusState.AUTH_NUM -> Gray800
        else -> Gray200
    }

    val emailButtonTextColor = if (showAuthNumberLayout) Gray800 else Gray300

    LaunchedEffect(Unit) {
        emailFocusRequester.requestFocus()
    }

    SignUpBasePage(
        title = stringResource(R.string.sign_up_auth_page_title),
        buttonEnabled = verifiedAuthNumber,
        buttonText = stringResource(R.string.sign_up_auth_page_button_text),
        onButtonClick = onClickNextPage,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            QrizTextFiled(
                value = email,
                onValueChange = onEmailChanged,
                supportingText = if (emailSupportingTextResId != R.string.empty) {
                    SupportingText(
                        message = stringResource(emailSupportingTextResId),
                        color = Red700,
                        isBorderColorRequired = false,
                    )
                } else {
                    null
                },
                borderStroke = BorderStroke(
                    width = 1.dp,
                    color = emailBorderColor
                ),
                containerColor = White,
                singleLine = true,
                hint = stringResource(R.string.email_sample_hint),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .focusRequester(emailFocusRequester)
                    .onFocusChanged {
                        if (it.isFocused) onChangeFocus(SignUpUiState.FocusState.EMAIL)
                    },
                contentPadding = PaddingValues(
                    horizontal = 10.dp,
                    vertical = 14.dp,
                ),
                trailing = {
                    if (email.isNotEmpty()) {
                        IconButton(
                            onClick = { onEmailChanged("") },
                            modifier = Modifier.size(20.dp),
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.delete_icon),
                                contentDescription = null,
                                tint = Gray300,
                            )
                        }
                    }
                },
            )
            OutlinedButton(
                onClick = onSendAuthNumberEmail,
                border = BorderStroke(
                    width = 1.dp,
                    color = Gray200,
                ),
                contentPadding = PaddingValues(vertical = 14.dp),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .height(48.dp)
                    .padding(start = 10.dp)
                    .widthIn(80.dp)
            ) {
                Text(
                    stringResource(
                        id = if (showAuthNumberLayout) R.string.resend else R.string.send
                    ),
                    color = emailButtonTextColor,
                    style = QrizTheme.typography.subhead,
                )
            }
        }

        if (showAuthNumberLayout) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                QrizTextFiled(
                    value = authNumber,
                    onValueChange = onAuthNumberChanged,
                    supportingText = if (authNumberSupportingTextResId != R.string.empty) {
                        SupportingText(
                            message = stringResource(authNumberSupportingTextResId),
                            color = authNumberSupportingTextColor,
                        )
                    } else {
                        null
                    },
                    borderStroke = if (isTimeExpiredEmailAuth) null
                    else BorderStroke(
                        width = 1.dp,
                        color = authNumberBorderColor
                    ),
                    containerColor = if (isTimeExpiredEmailAuth) Blue200 else White,
                    singleLine = true,
                    hint = stringResource(R.string.sign_up_auth_page_hint),
                    maxLength = AUTH_NUMBER_MAX_LENGTH,
                    enabled = enableInputAuthNumber,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .onFocusChanged {
                            if (it.isFocused) onChangeFocus(SignUpUiState.FocusState.AUTH_NUM)
                        },
                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                        vertical = 14.dp,
                    ),
                    trailing = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (authNumber.isNotEmpty() && verifiedAuthNumber.not()) {
                                IconButton(
                                    onClick = { onAuthNumberChanged("") },
                                    modifier = Modifier
                                        .padding(end = if (authTimerText != "00:00") 8.dp else 0.dp)
                                        .size(20.dp),
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.delete_icon),
                                        contentDescription = null,
                                        tint = Gray300,
                                    )
                                }
                            }

                            if (isTimeExpiredEmailAuth.not() && verifiedAuthNumber.not()) {
                                Text(
                                    text = authTimerText,
                                    style = QrizTheme.typography.caption,
                                    color = Gray800,
                                )
                            }

                            if (verifiedAuthNumber) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.check_icon),
                                    contentDescription = null,
                                    tint = Mint800
                                )
                            }
                        }
                    },
                )
                OutlinedButton(
                    onClick = onVerifyAuthNumber,
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (enableAuthNumVerifyButton) Blue600 else Gray200,
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(vertical = 14.dp),
                    modifier = Modifier
                        .height(48.dp)
                        .padding(start = 10.dp)
                        .widthIn(80.dp)
                ) {
                    Text(
                        stringResource(R.string.verify),
                        color = if (enableAuthNumVerifyButton) Blue600 else Gray300,
                        style = QrizTheme.typography.subhead,
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
)
@Composable
fun SignUpEmailAuthPagePreview() {
    QrizTheme {
        SignUpAuthPage(
            email = "",
            authNumber = "",
            authTimerText = "00:00",
            isValidEmail = true,
            showAuthNumberLayout = true,
            verifiedAuthNumber = false,
            enableInputAuthNumber = true,
            isTimeExpiredEmailAuth = false,
            enableAuthNumVerifyButton = false,
            focusState = SignUpUiState.FocusState.NONE,
            emailSupportingTextResId = R.string.empty,
            authNumberSupportingTextResId = R.string.empty,
            onEmailChanged = {},
            onSendAuthNumberEmail = {},
            onAuthNumberChanged = {},
            onVerifyAuthNumber = {},
            onChangeFocus = {},
            onClickNextPage = {},
        )
    }
}
