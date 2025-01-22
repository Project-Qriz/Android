package com.qriz.app.feature.sign.signup.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.QrizTextFiled
import com.qriz.app.core.designsystem.component.SupportingText
import com.qriz.app.core.designsystem.theme.Blue600
import com.qriz.app.core.designsystem.theme.Gray400
import com.qriz.app.core.designsystem.theme.Mint800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.Red800
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
    onSignUpEmailAuthPageInit: () -> Unit,
) {
    val isInitialized = rememberSaveable { mutableStateOf(false) }

    //TODO :해당 화면이 이전페이지에서 미리 로드되어서, 이메일이 입력되기도 전에 해당 부분이 호출되느 문제가 있음
    LaunchedEffect(Unit) {
        if (isInitialized.value.not()) {
            onSignUpEmailAuthPageInit()
            isInitialized.value = true
        }
    }

    val supportingText = if (isVerifiedEmailAuth) {
        SupportingText(
            message = stringResource(R.string.sign_up_auth_page_supporting_verified),
            color = Mint800
        )
    } else {
        if (errorMessage.isBlank()) null
        else SupportingText(
            message = errorMessage,
            color = Red800,
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
                            color = Blue600,
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
