package com.qriz.app.feature.sign.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qriz.app.core.designsystem.component.QrizButton
import com.qriz.app.core.designsystem.component.QrizLoading
import com.qriz.app.core.designsystem.component.QrizTextFiled
import com.qriz.app.core.designsystem.theme.Gray200
import com.qriz.app.core.designsystem.theme.Gray400
import com.qriz.app.core.designsystem.theme.Gray500
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.Red800
import com.qriz.app.feature.base.extention.collectSideEffect
import com.qriz.app.feature.sign.R
import com.qriz.app.core.designsystem.R as DSR

@Composable
fun SignInScreen(
    moveToSignUp: () -> Unit,
    moveToFindId: () -> Unit,
    moveToFindPw: () -> Unit,
    moveToHome: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    viewModel: SignInViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    viewModel.collectSideEffect {
        when (it) {
            is SignInUiEffect.ShowSnackBar -> onShowSnackbar(
                it.message ?: context.getString(it.defaultResId)
            )

            SignInUiEffect.MoveToSignUp -> moveToSignUp()
            SignInUiEffect.MoveToFindId -> moveToFindId()
            SignInUiEffect.MoveToFindPw -> moveToFindPw()
            SignInUiEffect.MoveToHome -> moveToHome()
        }
    }

    SignInContent(
        id = uiState.id,
        pw = uiState.pw,
        isVisiblePw = uiState.isVisiblePw,
        isAvailableLogin = uiState.isAvailableLogin,
        loginErrorMessage = stringResource(uiState.loginErrorMessageResId),
        isLoading = uiState.isLoading,
        onChangeId = { viewModel.process(SignInUiAction.ChangeUserId(it)) },
        onChangePw = { viewModel.process(SignInUiAction.ChangeUserPw(it)) },
        onClickLogin = { viewModel.process(SignInUiAction.ClickLogin) },
        onClickPwVisibility = { viewModel.process(SignInUiAction.ClickPwVisibility(it)) },
        onClickSignUp = { viewModel.process(SignInUiAction.ClickSignUp) },
        onClickFindId = { viewModel.process(SignInUiAction.ClickFindId) },
        onClickFindPw = { viewModel.process(SignInUiAction.ClickFindPw) },
    )
}

@Composable
private fun SignInContent(
    id: String,
    pw: String,
    isVisiblePw: Boolean,
    isAvailableLogin: Boolean,
    loginErrorMessage: String,
    isLoading: Boolean,
    onChangeId: (String) -> Unit,
    onChangePw: (String) -> Unit,
    onClickLogin: () -> Unit,
    onClickPwVisibility: (Boolean) -> Unit,
    onClickSignUp: () -> Unit,
    onClickFindId: () -> Unit,
    onClickFindPw: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        if (isLoading) QrizLoading()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp)
        ) {
            Image(
                painter = painterResource(DSR.drawable.qriz_logo),
                contentDescription = null,
                modifier = Modifier
                    .padding(
                        top = 48.dp,
                        bottom = 24.dp
                    )
                    .width(135.dp)
                    .height(45.dp)
            )

            QrizTextFiled(
                value = id,
                onValueChange = onChangeId,
                hint = stringResource(R.string.please_enter_id_sign_in),
                contentPadding = PaddingValues(
                    vertical = 19.dp,
                    horizontal = 16.dp,
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )

            QrizTextFiled(
                value = pw,
                onValueChange = onChangePw,
                hint = stringResource(R.string.please_enter_pw_sign_in),
                contentPadding = PaddingValues(
                    vertical = 19.dp,
                    horizontal = 16.dp,
                ),
                visualTransformation =
                if (isVisiblePw) VisualTransformation.None
                else PasswordVisualTransformation(),
                trailing = {
                    if (pw.isNotEmpty()) {
                        IconButton(onClick = { onClickPwVisibility(isVisiblePw.not()) }) {
                            Icon(
                                painter =
                                if (isVisiblePw) painterResource(DSR.drawable.ic_visible_password)
                                else painterResource(DSR.drawable.ic_invisible_password),
                                contentDescription = null
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 12.dp,
                        bottom = 20.dp
                    ),
            )

            if (loginErrorMessage.isNotBlank()) {
                Text(
                    text = loginErrorMessage,
                    style = QrizTheme.typography.body2,
                    color = Red800,
                    modifier = Modifier.padding(bottom = 12.dp),
                )
            }

            QrizButton(
                text = stringResource(R.string.login),
                enable = isAvailableLogin,
                modifier = Modifier.fillMaxWidth(),
                onClick = onClickLogin,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                val textStyle = QrizTheme.typography.subhead
                Text(
                    stringResource(R.string.find_id),
                    style = textStyle,
                    color = Gray500,
                    modifier = Modifier
                        .clickable(onClick = onClickFindId)
                        .padding(5.dp)
                )
                VerticalDivider(
                    thickness = 1.dp,
                    color = Gray200,
                    modifier = Modifier
                        .height(13.dp)
                        .padding(horizontal = 7.dp)
                )
                Text(
                    stringResource(R.string.find_pw),
                    style = textStyle,
                    color = Gray500,
                    modifier = Modifier
                        .clickable(onClick = onClickFindPw)
                        .padding(5.dp)
                )
                VerticalDivider(
                    thickness = 1.dp,
                    color = Gray200,
                    modifier = Modifier
                        .height(13.dp)
                        .padding(horizontal = 7.dp)
                )
                Text(
                    stringResource(R.string.sing_up),
                    style = textStyle,
                    color = Gray500,
                    modifier = Modifier
                        .clickable(onClick = onClickSignUp)
                        .padding(5.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 64.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = Gray200,
                )
                Text(
                    text = stringResource(R.string.easy_login),
                    style = QrizTheme.typography.label2,
                    color = Gray400,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = Gray200,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SignInScreenPreview() {
    QrizTheme {
        SignInContent(
            id = "",
            pw = "",
            isVisiblePw = false,
            isAvailableLogin = false,
            loginErrorMessage = "",
            isLoading = false,
            onChangeId = {},
            onChangePw = {},
            onClickLogin = {},
            onClickSignUp = {},
            onClickPwVisibility = {},
            onClickFindId = {},
            onClickFindPw = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SignInScreenDataEnteredPreview() {
    QrizTheme {
        SignInContent(
            id = "test1234",
            pw = "qriztest",
            isVisiblePw = false,
            isAvailableLogin = true,
            loginErrorMessage = "",
            isLoading = false,
            onChangeId = {},
            onChangePw = {},
            onClickLogin = {},
            onClickSignUp = {},
            onClickPwVisibility = {},
            onClickFindId = {},
            onClickFindPw = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SignInScreenDataEnteredPwVisiblePreview() {
    QrizTheme {
        SignInContent(
            id = "test1234",
            pw = "qriztest",
            isVisiblePw = true,
            isAvailableLogin = true,
            loginErrorMessage = "",
            isLoading = false,
            onChangeId = {},
            onChangePw = {},
            onClickLogin = {},
            onClickSignUp = {},
            onClickPwVisibility = {},
            onClickFindId = {},
            onClickFindPw = {},
        )
    }
}
