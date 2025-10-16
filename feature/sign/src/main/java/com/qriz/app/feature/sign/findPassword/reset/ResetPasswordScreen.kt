package com.qriz.app.feature.sign.findPassword.reset

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.qriz.app.core.designsystem.component.NavigationType
import com.qriz.app.core.designsystem.component.QrizTextFiled
import com.qriz.app.core.designsystem.component.QrizTopBar
import com.qriz.app.core.designsystem.component.SupportingText
import com.qriz.app.core.designsystem.theme.Black
import com.qriz.app.core.designsystem.theme.Blue500
import com.qriz.app.core.designsystem.theme.Gray200
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.Red700
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.core.ui.common.const.ErrorDialog
import com.qriz.app.core.ui.common.const.NetworkErrorDialog
import com.qriz.app.core.ui.common.resource.UNKNOWN_ERROR
import com.qriz.app.feature.base.extention.collectSideEffect
import com.qriz.app.feature.sign.R
import com.qriz.app.feature.sign.component.FormatStateRow
import com.qriz.app.feature.sign.signup.component.SignUpBasePage
import com.quiz.app.core.data.user.user_api.model.PW_MAX_LENGTH
import com.qriz.app.core.designsystem.R as DSR

@Composable
fun ResetPasswordScreen(
    viewmodel: ResetPasswordViewModel = hiltViewModel(),
    onShowSnackbar: (String) -> Unit,
    onBack: () -> Unit,
    moveToSignIn: () -> Unit,
) {
    val uiState by viewmodel.uiState.collectAsState()

    viewmodel.collectSideEffect {
        when (it) {
            is ResetPasswordUiEffect.ResetComplete -> moveToSignIn()
            is ResetPasswordUiEffect.OnShowSnackbar -> onShowSnackbar(it.message)
        }
    }

    ResetPasswordContent(
        uiState = uiState,
        onBack = onBack,
        onResetPassword = { viewmodel.process(ResetPasswordUiAction.ResetPassword) },
        onPasswordChange = { viewmodel.process(ResetPasswordUiAction.OnChangePassword(it)) },
        onPasswordConfirmChange = { viewmodel.process(ResetPasswordUiAction.OnChangePasswordConfirm(it)) },
        onPasswordVisibilityChange = { viewmodel.process(ResetPasswordUiAction.ChangePasswordVisibility(it)) },
        onPasswordConfirmVisibilityChange = { viewmodel.process(ResetPasswordUiAction.ChangePasswordConfirmVisibility(it)) },
        onChangeFocusPassword = { viewmodel.process(ResetPasswordUiAction.ChangeFocusPassword(it)) },
        onChangeFocusPasswordConfirm = { viewmodel.process(ResetPasswordUiAction.ChangeFocusPasswordConfirm(it)) },
        onConfirmNetworkErrorDialog = { viewmodel.process(ResetPasswordUiAction.ConfirmNetworkErrorDialog) },
        onConfirmUnknownErrorDialog = { viewmodel.process(ResetPasswordUiAction.ConfirmUnknownErrorDialog) },
    )
}

@Composable
private fun ResetPasswordContent(
    uiState: ResetPasswordUiState,
    onBack: () -> Unit,
    onResetPassword: () -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordConfirmChange: (String) -> Unit,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    onPasswordConfirmVisibilityChange: (Boolean) -> Unit,
    onChangeFocusPassword: (Boolean) -> Unit,
    onChangeFocusPasswordConfirm: (Boolean) -> Unit,
    onConfirmNetworkErrorDialog: () -> Unit,
    onConfirmUnknownErrorDialog: () -> Unit,
) {
    if (uiState.showNetworkErrorDialog) {
        NetworkErrorDialog(onConfirmClick = onConfirmNetworkErrorDialog)
    }

    if (uiState.showUnknownErrorDialog) {
        ErrorDialog(
            description = UNKNOWN_ERROR,
            onConfirmClick = onConfirmUnknownErrorDialog
        )
    }

    Column(
        modifier = Modifier
            .background(color = White)
            .navigationBarsPadding(),
    ) {
        QrizTopBar(
            title = stringResource(R.string.find_password),
            onNavigationClick = onBack,
            navigationType = NavigationType.BACK,
        )

        SignUpBasePage(
            title = stringResource(R.string.reset_password_title),
            subTitle = stringResource(R.string.reset_password_message),
            buttonText = stringResource(R.string.reset_password),
            buttonEnabled = uiState.canResetPassword,
            onButtonClick = onResetPassword,
        ) {
            QrizTextFiled(
                value = uiState.password,
                onValueChange = onPasswordChange,
                hint = stringResource(R.string.reset_password_hint),
                borderStroke = BorderStroke(
                    width = 1.dp,
                    color = if (uiState.isFocusedPassword) Black else Gray200,
                ),
                containerColor = White,
                maxLength = PW_MAX_LENGTH,
                singleLine = true,
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 14.dp,
                ),
                visualTransformation = if (uiState.visiblePassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailing = {
                    IconButton(onClick = { onPasswordVisibilityChange(uiState.visiblePassword.not()) }) {
                        Icon(
                            painter = if (uiState.visiblePassword) painterResource(DSR.drawable.ic_visible_password)
                            else painterResource(DSR.drawable.ic_invisible_password),
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .onFocusChanged { onChangeFocusPassword(it.isFocused) },
            )

            FormatStateRow(
                message = stringResource(R.string.password_format),
                isFormatValid = uiState.isValidPasswordFormat,
            )

            FormatStateRow(
                message = stringResource(R.string.password_length),
                isFormatValid = uiState.isValidPasswordLength,
            )

            val passwordConfirmBorderColor = when {
                uiState.passwordConfirmErrorMessageResId != R.string.empty -> Red700
                uiState.isFocusedPasswordConfirm -> Black
                else -> Gray200
            }

            QrizTextFiled(
                value = uiState.passwordConfirm,
                onValueChange = onPasswordConfirmChange,
                hint = stringResource(R.string.reset_password_confirm_hint),
                borderStroke = BorderStroke(
                    width = 1.dp,
                    color = passwordConfirmBorderColor,
                ),
                containerColor = White,
                maxLength = PW_MAX_LENGTH,
                singleLine = true,
                visualTransformation = if (uiState.visiblePasswordConfirm) VisualTransformation.None else PasswordVisualTransformation(),
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 14.dp,
                ),
                supportingText = SupportingText(
                    message = stringResource(uiState.passwordConfirmErrorMessageResId),
                    color = Red700,
                ),
                trailing = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (uiState.isEqualsPassword) {
                            Icon(
                                painter = painterResource(R.drawable.check_icon),
                                contentDescription = null,
                                tint = Blue500,
                            )
                        }

                        IconButton(onClick = { onPasswordConfirmVisibilityChange(uiState.visiblePasswordConfirm.not()) }) {
                            Icon(
                                painter = if (uiState.visiblePasswordConfirm) painterResource(DSR.drawable.ic_visible_password)
                                else painterResource(DSR.drawable.ic_invisible_password),
                                contentDescription = null
                            )
                        }
                    }
                },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .onFocusChanged { onChangeFocusPasswordConfirm(it.isFocused) },
            )
        }
    }
}

@Preview
@Composable
private fun ResetPasswordContentPreview() {
    QrizTheme {
        ResetPasswordContent(
            uiState = ResetPasswordUiState.DEFAULT,
            onBack = { },
            onResetPassword = { },
            onPasswordChange = { },
            onPasswordConfirmChange = { },
            onPasswordVisibilityChange = { },
            onPasswordConfirmVisibilityChange = { },
            onChangeFocusPassword = { },
            onChangeFocusPasswordConfirm = { },
            onConfirmNetworkErrorDialog = { },
            onConfirmUnknownErrorDialog = { },
        )
    }
}
