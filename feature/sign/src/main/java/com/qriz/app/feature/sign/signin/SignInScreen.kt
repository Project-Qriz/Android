package com.qriz.app.feature.sign.signin

import android.content.Context
import android.util.Log
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.kakao.sdk.user.UserApiClient
import com.qriz.app.core.designsystem.component.QrizButton
import com.qriz.app.core.designsystem.component.QrizLoading
import com.qriz.app.core.designsystem.component.QrizTextFiled
import com.qriz.app.core.designsystem.theme.Black
import com.qriz.app.core.designsystem.theme.Gray200
import com.qriz.app.core.designsystem.theme.Gray400
import com.qriz.app.core.designsystem.theme.Gray500
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.Red700
import com.qriz.app.feature.base.extention.collectSideEffect
import com.qriz.app.feature.sign.BuildConfig
import com.qriz.app.feature.sign.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import com.qriz.app.core.designsystem.R as DSR

@Composable
fun SignInScreen(
    moveToSignUp: () -> Unit,
    moveToFindId: () -> Unit,
    moveToFindPw: () -> Unit,
    moveToHome: () -> Unit,
    moveToConceptCheckGuide: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    viewModel: SignInViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
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

            SignInUiEffect.KakaoLogin -> {
                coroutineScope.launch {
                    when(val result = kakaoLogin(context)) {
                        is SocialLoginResult.Failure -> viewModel.process(
                            SignInUiAction.ShowSnackbar(
                                result.message
                            )
                        )
                        is SocialLoginResult.Success -> viewModel.process(
                            SignInUiAction.ProcessKakaoLogin(
                                result.token
                            )
                        )
                    }
                }
            }

            SignInUiEffect.GoogleLogin -> {
                coroutineScope.launch {
                    when(val result = googleLogin(context)) {
                        is SocialLoginResult.Failure -> viewModel.process(
                            SignInUiAction.ShowSnackbar(
                                result.message
                            )
                        )
                        is SocialLoginResult.Success -> viewModel.process(
                            SignInUiAction.ProcessGoogleLogin(
                                result.token
                            )
                        )
                    }
                }
            }

            SignInUiEffect.MoveToConceptCheckGuide -> moveToConceptCheckGuide()
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
        onClickKakaoLogin = { viewModel.process(SignInUiAction.ClickKakaoLogin) },
        onClickGoogleLogin = { viewModel.process(SignInUiAction.ClickGoogleLogin) },
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
    onClickKakaoLogin: () -> Unit,
    onClickGoogleLogin: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        if (isLoading) QrizLoading()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 18.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 39.dp,
                        bottom = 31.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(DSR.drawable.qriz_app_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .padding(end = 8.dp)
                        .width(42.dp)
                        .height(42.dp),
                )
                Image(
                    painter = painterResource(DSR.drawable.qriz_text_logo_white),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Black),
                    modifier = Modifier
                        .width(81.dp)
                        .height(28.dp),
                )
            }

            QrizTextFiled(
                value = id,
                onValueChange = onChangeId,
                hint = stringResource(R.string.please_enter_id_sign_in),
                contentPadding = PaddingValues(
                    vertical = 19.dp,
                    horizontal = 16.dp,
                ),
                modifier = Modifier.fillMaxWidth()
            )

            QrizTextFiled(
                value = pw,
                onValueChange = onChangePw,
                hint = stringResource(R.string.please_enter_pw_sign_in),
                contentPadding = PaddingValues(
                    vertical = 19.dp,
                    horizontal = 16.dp,
                ),
                visualTransformation = if (isVisiblePw) VisualTransformation.None
                else PasswordVisualTransformation(),
                trailing = {
                    if (pw.isNotEmpty()) {
                        IconButton(onClick = { onClickPwVisibility(isVisiblePw.not()) }) {
                            Icon(
                                painter = if (isVisiblePw) painterResource(DSR.drawable.ic_visible_password)
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
                    color = Red700,
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

            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                Image(
                    modifier = Modifier
                        .clickable(onClick = onClickGoogleLogin)
                        .size(48.dp),
                    painter = painterResource(R.drawable.google_icon),
                    contentDescription = null,
                )

                Image(
                    modifier = Modifier
                        .clickable(onClick = onClickKakaoLogin)
                        .size(48.dp),
                    painter = painterResource(R.drawable.kakao_icon),
                    contentDescription = null,
                )
            }
        }
    }
}

private suspend fun kakaoLogin(context: Context) = suspendCancellableCoroutine{
    val isAvailable = UserApiClient.instance.isKakaoTalkLoginAvailable(context)
    if (isAvailable.not()) {
        it.resume(SocialLoginResult.Failure("카카오톡을 설치해주세요"))
    }

    UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
        if (token != null) {
            it.resume(SocialLoginResult.Success(token.accessToken))
        }

        if (error != null) {
            it.resume(SocialLoginResult.Failure(error.message ?: "카카오톡 로그인에 실패하였습니다."))
        }
    }
}

private suspend fun googleLogin(context: Context): SocialLoginResult {
    return try {
        val credentialManager = CredentialManager.create(context)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.GOOGLE_CLOUD_CONSOLE_CLIENT_ID)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val result = credentialManager.getCredential(
            request = request,
            context = context,
        )

        val credential = result.credential

        if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            try {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                SocialLoginResult.Success(googleIdTokenCredential.idToken)
            } catch (e: GoogleIdTokenParsingException) {
                SocialLoginResult.Failure("Google 로그인에 실패하였습니다.")
            }
        } else {
            SocialLoginResult.Failure("Google 로그인에 실패하였습니다.")
        }
    } catch (e: GetCredentialException) {
        SocialLoginResult.Failure(e.message ?: "Google 로그인에 실패하였습니다.")
    } catch (e: Exception) {
        SocialLoginResult.Failure("Google 로그인 중 오류가 발생하였습니다.")
    }
}

private sealed interface SocialLoginResult {
    data class Success(val token: String) : SocialLoginResult
    data class Failure(val message: String) : SocialLoginResult
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
            onClickKakaoLogin = {},
            onClickGoogleLogin = {},
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
            onClickKakaoLogin = {},
            onClickGoogleLogin = {},
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
            onClickKakaoLogin = {},
            onClickGoogleLogin = {},
        )
    }
}
