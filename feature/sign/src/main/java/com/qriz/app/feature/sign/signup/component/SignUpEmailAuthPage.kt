package com.qriz.app.feature.sign.signup.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.QrizTextFiled
import com.qriz.app.core.designsystem.component.SupportingText
import com.qriz.app.core.designsystem.theme.Gray400
import com.qriz.app.core.designsystem.theme.Mint800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.feature.sign.R

@Composable
fun SignUpEmailAuthPage(
    emailAuthNumber: String,
    isVerifiedEmailAuth: Boolean,
    timer: String,
    errorMessage: String,
    onChangeEmailAuthNum: (String) -> Unit,
    onClickEmailAuthNumSend: () -> Unit,
    onClickNextPage: () -> Unit,
) {
    val supportingText = if (isVerifiedEmailAuth) {
        SupportingText(
            message = stringResource(R.string.sign_up_auth_page_supporting_verified),
            color = Mint800
        )
    } else {
        if (errorMessage.isBlank()) null
        else SupportingText(
            message = errorMessage,
            color = MaterialTheme.colorScheme.error,
        )
    }

    SignUpBasePage(
        title = stringResource(R.string.sign_up_auth_page_title),
        subTitle = stringResource(R.string.sign_up_auth_page_sub_title),
        buttonEnabled = isVerifiedEmailAuth,
        buttonText = stringResource(R.string.sign_up_auth_page_button_text),
        onButtonClick = onClickNextPage,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            QrizTextFiled(
                enabled = isVerifiedEmailAuth.not(),
                value = emailAuthNumber,
                supportingText = supportingText,
                onValueChange = onChangeEmailAuthNum,
                hint = stringResource(R.string.sign_up_auth_page_hint),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                maxLength = 6,
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 14.dp,
                ),
                trailing = {
                    if (isVerifiedEmailAuth.not()) {
                        Text(
                            text = timer,
                            style = QrizTheme.typography.body1,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                },
            )
            if (isVerifiedEmailAuth.not()) {
                Text(
                    text = stringResource(R.string.sign_up_auth_page_retry),
                    color = Gray400,
                    style = QrizTheme.typography.subhead.copy(
                        textDecoration = TextDecoration.Underline
                    ),
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .clickable(
                            enabled = isVerifiedEmailAuth.not(),
                            onClick = { onClickEmailAuthNumSend() }
                        )
                        .padding(5.dp)
                )
            }
        }
    }
}
