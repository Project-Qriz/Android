package com.qriz.app.feature.sign.signUp.component

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qriz.app.core.designsystem.component.QrizTextFiled
import com.qriz.app.core.designsystem.component.SupportingText
import com.qriz.app.core.designsystem.theme.Gray400
import com.qriz.app.core.designsystem.theme.Mint800
import com.qriz.app.feature.sign.R
import com.qriz.app.feature.sign.signUp.SignUpUiState.AuthenticationState

@Composable
fun SignUpEmailAuthPage(
    emailAuthNumber: String,
    emailAuthState: AuthenticationState,
    timer: String,
    errorMessage: String,
    onChangeEmailAuthNum: (String) -> Unit,
    onClickEmailAuthNumSend: () -> Unit,
    onClickNextPage: () -> Unit,
) {
    val supportingText = if (emailAuthState == AuthenticationState.Verified) {
        SupportingText(
            message = stringResource(R.string.sign_up_auth_page_supporting_verified),
            color = Mint800
        )
    } else {
        SupportingText(
            message = errorMessage,
            color = MaterialTheme.colorScheme.error,
        )
    }

    SignUpBasePage(
        title = stringResource(R.string.sign_up_auth_page_title),
        subTitle = stringResource(R.string.sign_up_auth_page_sub_title),
        buttonEnabled = emailAuthState == AuthenticationState.Verified,
        buttonText = stringResource(R.string.sign_up_auth_page_button_text),
        onButtonClick = onClickNextPage,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            QrizTextFiled(
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
                    Text(
                        text = timer,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                },
            )
            Text(
                text = stringResource(R.string.sign_up_auth_page_retry),
                color = Gray400,
                style = TextStyle.Default.copy(
                    fontSize = 12.sp,
                    textDecoration = TextDecoration.Underline,
                ),
                modifier = Modifier
                    .padding(5.dp)
                    .clickable { onClickEmailAuthNumSend() },
            )
        }
    }
}
