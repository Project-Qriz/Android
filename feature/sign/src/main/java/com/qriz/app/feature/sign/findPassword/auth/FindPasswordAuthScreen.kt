package com.qriz.app.feature.sign.findPassword.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qriz.app.core.designsystem.component.NavigationType
import com.qriz.app.core.designsystem.component.QrizTextFiled
import com.qriz.app.core.designsystem.component.QrizTopBar
import com.qriz.app.core.designsystem.component.SupportingText
import com.qriz.app.core.designsystem.theme.Blue200
import com.qriz.app.core.designsystem.theme.Gray200
import com.qriz.app.core.designsystem.theme.Gray300
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.Mint800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.Red500
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.feature.base.extention.collectSideEffect
import com.qriz.app.feature.sign.R
import com.qriz.app.feature.sign.signup.component.SignUpBasePage
import com.quiz.app.core.data.user.user_api.model.AUTH_NUMBER_MAX_LENGTH
import com.quiz.app.core.data.user.user_api.model.ID_MAX_LENGTH

@Composable
fun FindPasswordAuthScreen(
    viewModel: FindPasswordAuthViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onNavigateReset: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.collectSideEffect {
        when (it) {
            FindPasswordAuthUiEffect.VerifiedAuthNumber -> onNavigateReset()
        }
    }

    FindPasswordAuthContent(
        email = uiState.email,
        authNumber = uiState.authNumber,
        authTimerText = uiState.authTimerText,
        showAuthNumberLayout = uiState.showAuthNumberLayout,
        enableInputAuthNumber = uiState.enableInputAuthNumber,
        verifiedAuthNumber = uiState.verifiedAuthNumber,
        emailSupportingTextResId = uiState.emailSupportingTextResId,
        authNumberSupportingTextResId = uiState.authNumberSupportingTextResId,
        onEmailChanged = {
            viewModel.process(FindPasswordAuthUiAction.OnChangeEmail(email = it))
        },
        onAuthNumberChanged = {
            viewModel.process(FindPasswordAuthUiAction.OnChangeAuthNumber(authNumber = it))
        },
        onSendAuthNumberEmail = {
            viewModel.process(FindPasswordAuthUiAction.SendAuthNumberEmail)
        },
        onVerifyAuthNumber = {
            viewModel.process(FindPasswordAuthUiAction.VerifyAuthNumber)
        },
        onBack = onBack,
        onNavigateReset = onNavigateReset,
    )

}

@Composable
private fun FindPasswordAuthContent(
    email: String,
    authNumber: String,
    authTimerText: String,
    showAuthNumberLayout: Boolean,
    verifiedAuthNumber: Boolean,
    enableInputAuthNumber: Boolean,
    emailSupportingTextResId: Int,
    authNumberSupportingTextResId: Int,
    onBack: () -> Unit,
    onEmailChanged: (String) -> Unit,
    onSendAuthNumberEmail: () -> Unit,
    onAuthNumberChanged: (String) -> Unit,
    onVerifyAuthNumber: () -> Unit,
    onNavigateReset: () -> Unit,
) {
    //TODO: Dialog 작업

    val authNumberSupportingTextColor = when(authNumberSupportingTextResId) {
        R.string.success_send_email_auth_number,
        R.string.success_verify_auth_number -> Mint800
        else -> Red500
    }

    val authNumberBorderColor = when(authNumberSupportingTextResId) {
        R.string.fail_verify_auth_number -> Red500
        else -> Gray200
    }

    val emailButtonTextColor = if (showAuthNumberLayout) Gray800 else Gray300

    Column(
        modifier = Modifier
            .background(color = White)
            .scrollable(
                state = rememberScrollState(),
                orientation = Orientation.Vertical,
            )
    ) {
        QrizTopBar(
            title = stringResource(R.string.find_password),
            navigationType = NavigationType.BACK,
            onNavigationClick = onBack,
        )

        SignUpBasePage(
            title = stringResource(R.string.find_password_auth_title),
            subTitle = stringResource(R.string.find_password_auth_guide),
            buttonText = stringResource(R.string.reset_password),
            buttonEnabled = verifiedAuthNumber,
            onButtonClick = onNavigateReset,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                QrizTextFiled(
                    value = email,
                    onValueChange = onEmailChanged,
                    supportingText = if (emailSupportingTextResId != R.string.empty) {
                        SupportingText(
                            message = stringResource(emailSupportingTextResId),
                            color = Red500,
                            isBorderColorRequired = false,
                        )
                    } else {
                        null
                    },
                    borderStroke = BorderStroke(
                        width = 1.dp,
                        color = Gray200,
                    ),
                    containerColor = White,
                    singleLine = true,
                    hint = stringResource(R.string.email_sample_hint),
                    maxLength = ID_MAX_LENGTH,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                        vertical = 14.dp,
                    )
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
                    QrizTextFiled(value = authNumber,
                        onValueChange = onAuthNumberChanged,
                        supportingText = if (authNumberSupportingTextResId != R.string.empty) {
                            SupportingText(
                                message = stringResource(authNumberSupportingTextResId),
                                color = authNumberSupportingTextColor,
                            )
                        } else {
                            null
                        },
                        borderStroke = if (authTimerText == "00:00" && verifiedAuthNumber.not()) null
                        else BorderStroke(
                            width = 1.dp,
                            color = authNumberBorderColor
                        ),
                        containerColor = if (authTimerText == "00:00" && verifiedAuthNumber.not()) Blue200
                        else White,
                        singleLine = true,
                        hint = stringResource(R.string.sign_up_auth_page_hint),
                        maxLength = AUTH_NUMBER_MAX_LENGTH,
                        enabled = enableInputAuthNumber,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
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

                                if (verifiedAuthNumber.not() && authTimerText != "00:00") {
                                    Text(
                                        text = authTimerText,
                                        style = QrizTheme.typography.caption,
                                        color = Gray800,
                                    )
                                }

                                if (verifiedAuthNumber) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.complete_icon),
                                        contentDescription = null,
                                        tint = Mint800
                                    )
                                }
                            }
                        })
                    OutlinedButton(
                        onClick = onVerifyAuthNumber,
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(vertical = 14.dp),
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .widthIn(80.dp)
                    ) {
                        Text(
                            stringResource(R.string.verify),
                            color = MaterialTheme.colorScheme.primary,
                            style = QrizTheme.typography.subhead,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FindPasswordAuthContentPreview() {
    QrizTheme {
        FindPasswordAuthContent(
            email = "",
            authNumber = "",
            authTimerText = "",
            showAuthNumberLayout = true,
            verifiedAuthNumber = true,
            enableInputAuthNumber = true,
            emailSupportingTextResId = R.string.empty,
            authNumberSupportingTextResId = R.string.empty,
            onEmailChanged = {},
            onAuthNumberChanged = {},
            onSendAuthNumberEmail = {},
            onBack = {},
            onNavigateReset = {},
            onVerifyAuthNumber = {},
        )
    }
}
