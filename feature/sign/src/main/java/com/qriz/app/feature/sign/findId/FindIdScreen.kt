package com.qriz.app.feature.sign.findId

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qriz.app.core.designsystem.component.NavigationType
import com.qriz.app.core.designsystem.component.QrizTextFiled
import com.qriz.app.core.designsystem.component.QrizTopBar
import com.qriz.app.core.designsystem.component.SupportingText
import com.qriz.app.core.designsystem.theme.Gray300
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.Red700
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.feature.sign.R
import com.qriz.app.feature.sign.component.QrizAlertDialog
import com.qriz.app.feature.sign.signup.component.SignUpBasePage

@Composable
fun FindIdScreen(
    viewModel: FindIdViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect {
            when (it) {
                FindIdEffect.Complete -> onBack()
            }
        }
    }

    FindIdContent(
        email = uiState.email,
        successDialogState = uiState.successDialogState,
        errorDialogState = uiState.errorDialogState,
        errorMessageResId = uiState.errorMessageResId,
        moveToBack = onBack,
        onEmailChanged = { email ->
            viewModel.process(FindIdUiAction.OnChangeEmail(email = email))
        },
        onSendEmail = {
            viewModel.process(FindIdUiAction.SendEmailToFindId)
        },
        onConfirmSuccessDialog = {
            viewModel.process(FindIdUiAction.ConfirmSuccessDialog)
        },
        onConfirmErrorDialog = {
            viewModel.process(FindIdUiAction.ConfirmErrorDialog)
        },
    )
}

@Composable
private fun FindIdContent(
    email: String,
    successDialogState: DialogState,
    errorDialogState: DialogState,
    errorMessageResId: Int,
    onEmailChanged: (String) -> Unit,
    moveToBack: () -> Unit,
    onSendEmail: () -> Unit,
    onConfirmSuccessDialog: () -> Unit,
    onConfirmErrorDialog: () -> Unit,
) {
    if (successDialogState.shouldShow) {
        QrizAlertDialog(
            title = stringResource(R.string.send_email_success),
            message = stringResource(R.string.check_email_to_find_id),
            onConfirmRequest = onConfirmSuccessDialog
        )
    }

    if (errorDialogState.shouldShow) {
        QrizAlertDialog(
            title = errorDialogState.title,
            message = errorDialogState.message,
            onConfirmRequest = onConfirmErrorDialog,
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .background(White)
    ) {
        QrizTopBar(
            title = stringResource(R.string.find_id),
            navigationType = NavigationType.BACK,
            background = White,
            onNavigationClick = moveToBack,
        )

        SignUpBasePage(
            title = stringResource(R.string.find_id_screen_title),
            subTitle = stringResource(R.string.find_id_screen_guide),
            buttonEnabled = true,
            buttonText = stringResource(R.string.find_id),
            onButtonClick = onSendEmail,
        ) {
            QrizTextFiled(
                value = email,
                onValueChange = onEmailChanged,
                hint = stringResource(R.string.email_sample_hint),
                supportingText = if (errorMessageResId != R.string.empty) {
                    SupportingText(
                        message = stringResource(errorMessageResId),
                        color = Red700,
                    )
                } else {
                    null
                },
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
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
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
private fun FindIdContentPreview() {
    QrizTheme {
        FindIdContent(
            email = "",
            successDialogState = DialogState.EMPTY,
            errorDialogState = DialogState.EMPTY,
            errorMessageResId = R.string.empty,
            onEmailChanged = {},
            moveToBack = {},
            onSendEmail = {},
            onConfirmSuccessDialog = {},
            onConfirmErrorDialog = {},
        )
    }
}
