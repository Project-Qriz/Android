package com.qriz.app.feature.sign

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qriz.app.core.designsystem.component.NavigationType
import com.qriz.app.core.designsystem.component.QrizButton
import com.qriz.app.core.designsystem.component.QrizTextFiled
import com.qriz.app.core.designsystem.component.QrizTopBar
import com.qriz.app.core.designsystem.component.SupportingText
import com.qriz.app.core.designsystem.theme.Black
import com.qriz.app.core.designsystem.theme.Blue600
import com.qriz.app.core.designsystem.theme.Gray200
import com.qriz.app.core.designsystem.theme.Gray400
import com.qriz.app.core.designsystem.theme.Mint800
import com.qriz.app.feature.sign.model.AuthenticationState
import com.qriz.app.feature.sign.model.SignUpEffect

const val SIGN_UP_PAGE = 5

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    onSignUp: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState { SIGN_UP_PAGE }

    LaunchedEffect(state.page) {
        pagerState.animateScrollToPage(state.page)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect {
            when(it) {
                SignUpEffect.SignUpComplete -> {
                    onSignUp()
                }
            }
        }
    }

    Column {
        QrizTopBar(
            title = state.topBarTitle,
            navigationType = if (state.page == 0) NavigationType.None else NavigationType.Back,
            onNavigationClick = viewModel::previousPage
        )
        LinearProgressIndicator(
            progress = {
                val percent = (pagerState.currentPage + 1).toFloat() / SIGN_UP_PAGE.toFloat()
                percent
            },
            trackColor = Gray200,
            color = Blue600,
            strokeCap = StrokeCap.Round,
            modifier = Modifier.fillMaxWidth()
        )
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
        ) { page ->
            when (page) {
                0 -> SignUpNameContent(
                    name = state.name,
                    errorMessage = state.nameErrorMessage,
                    onNameChanged = viewModel::updateName,
                    onNext = viewModel::nextPage,
                )

                1 -> SignUpEmailContent(
                    email = state.email,
                    errorMessage = state.emailErrorMessage,
                    emailVerified = state.emailVerified,
                    onEmailChanged = viewModel::updateEmail,
                    onNext = viewModel::nextPage,
                )

                2 -> SignUpEmailAuthContent(
                    authenticationNumber = state.authenticationNumber,
                    authenticationState = state.authenticationState,
                    timer = state.timerText,
                    errorMessage = state.authenticationNumberErrorMessage,
                    onAuthenticationNumberChanged = viewModel::updateAuthenticationNumber,
                    onNext = viewModel::nextPage,
                    onRetry = viewModel::sendAuthenticationNumberEmail
                )

                3 -> SignUpIdContent(
                    id = state.id,
                    onIdChanged = viewModel::updateId,
                    onCheckDuplicate = viewModel::checkDuplicateId,
                    isAvailableId = state.isAvailableId,
                    errorMessage = state.idErrorMessage,
                    onNext = viewModel::nextPage,
                )

                4 -> SignUpPasswordContent(
                    password = state.password,
                    passwordCheck = state.passwordCheck,
                    passwordErrorMessage = state.passwordErrorMessage,
                    passwordCheckErrorMessage = state.passwordCheckErrorMessage,
                    canSignUp = state.canSignUp,
                    onChangePassword = viewModel::updatePassword,
                    onChangePasswordCheck = viewModel::updatePasswordCheck,
                    onSignUp = viewModel::signUp,
                )
            }
        }
    }
}

@Composable
private fun SignUpNameContent(
    name: String,
    errorMessage: String,
    onNameChanged: (String) -> Unit,
    onNext: () -> Unit,
) {
    val supportingText: SupportingText? = if (errorMessage.isNotEmpty()) SupportingText(
        message = errorMessage,
        color = MaterialTheme.colorScheme.error
    ) else null

    SignUpContent(
        title = "이름을 입력해주세요!",
        subTitle = "가입을 위해 실명을 입력해주세요.",
        buttonEnabled = name.length >= 2,
        buttonText = "다음",
        onButtonClick = onNext,
    ) {
        QrizTextFiled(
            value = name,
            supportingText = supportingText,
            onValueChange = onNameChanged,
            singleLine = true,
            hint = "이름을 입력",
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 14.dp,
            )
        )
    }
}

@Composable
private fun SignUpEmailContent(
    email: String,
    errorMessage: String,
    emailVerified: Boolean,
    onEmailChanged: (String) -> Unit,
    onNext: () -> Unit,
) {
    val supportingText: SupportingText? = if (errorMessage.isNotEmpty()) SupportingText(
        message = errorMessage,
        color = MaterialTheme.colorScheme.error
    ) else null

    SignUpContent(
        title = "이메일로\n본인확인을 진행할게요!",
        subTitle = "이메일 형식을 맞춰 입력해주세요.",
        buttonEnabled = emailVerified,
        buttonText = "다음",
        onButtonClick = onNext,
    ) {
        QrizTextFiled(
            value = email,
            supportingText = supportingText,
            onValueChange = onEmailChanged,
            singleLine = true,
            hint = "이메일 입력",
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 14.dp,
            )
        )
    }
}

@Composable
private fun SignUpEmailAuthContent(
    authenticationNumber: String,
    authenticationState: AuthenticationState,
    timer: String,
    errorMessage: String,
    onAuthenticationNumberChanged: (String) -> Unit,
    onRetry: () -> Unit,
    onNext: () -> Unit,
) {
    val supportingText = if (authenticationState == AuthenticationState.Verified) {
        SupportingText(
            message = "인증 되었습니다",
            color = Mint800
        )
    } else {
        SupportingText(
            message = errorMessage,
            color = MaterialTheme.colorScheme.error,
        )
    }

    SignUpContent(
        title = "이메일로 받은\n인증번호를 입력해주세요",
        subTitle = "메일 수신함에서 인증번호를 확인해주세요",
        buttonEnabled = authenticationState == AuthenticationState.Verified,
        buttonText = "다음",
        onButtonClick = onNext,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            QrizTextFiled(
                value = authenticationNumber,
                supportingText = supportingText,
                onValueChange = onAuthenticationNumberChanged,
                hint = "인증번호 6자리 입력",
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
            Text(text = "인증번호 다시 받기",
                color = Gray400,
                style = TextStyle.Default.copy(
                    fontSize = 12.sp,
                    textDecoration = TextDecoration.Underline,
                ),
                modifier = Modifier
                    .padding(5.dp)
                    .clickable { onRetry() })
        }
    }
}

@Composable
private fun SignUpIdContent(
    id: String,
    isAvailableId: Boolean,
    errorMessage: String,
    onIdChanged: (String) -> Unit,
    onCheckDuplicate: () -> Unit,
    onNext: () -> Unit,
) {
    val supportingText = if (errorMessage.isNotEmpty()) {
        SupportingText(
            message = errorMessage,
            color = MaterialTheme.colorScheme.error
        )
    } else {
        SupportingText(
            message = "${id.length}/8",
            color = Black
        )
    }

    SignUpContent(
        title = "아이디를 입력해주세요!",
        subTitle = "사용할 아이디를 입력해주세요",
        buttonText = "다음",
        buttonEnabled = isAvailableId,
        onButtonClick = onNext,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            QrizTextFiled(
                value = id,
                onValueChange = onIdChanged,
                supportingText = supportingText,
                singleLine = true,
                hint = "아이디 입력",
                maxLength = 8,
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 14.dp,
                )
            )
            OutlinedButton(
                onClick = onCheckDuplicate,
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 14.dp
                ),
                modifier = Modifier.padding(start = 10.dp)
            ) {
                Text(
                    "중복확인",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
private fun SignUpPasswordContent(
    password: String,
    passwordCheck: String,
    passwordErrorMessage: String,
    passwordCheckErrorMessage: String,
    canSignUp: Boolean,
    onChangePassword: (String) -> Unit,
    onChangePasswordCheck: (String) -> Unit,
    onSignUp: () -> Unit,
) {
    val passwordSupportingText = if (passwordErrorMessage.isNotEmpty()) {
        SupportingText(
            message = passwordErrorMessage,
            color = MaterialTheme.colorScheme.error,
        )
    } else {
        null
    }

    val passwordCheckSupportingText = if (passwordCheckErrorMessage.isNotEmpty()) {
        SupportingText(
            message = passwordCheckErrorMessage,
            color = MaterialTheme.colorScheme.error,
        )
    } else {
        null
    }

    SignUpContent(
        title = "비밀번호를 입력해주세요!",
        subTitle = "사용할 비밀번호를 입력해주세요",
        buttonText = "가입하기",
        buttonEnabled = canSignUp,
        onButtonClick = onSignUp,
    ) {
        QrizTextFiled(
            value = password,
            onValueChange = onChangePassword,
            supportingText = passwordSupportingText,
            singleLine = true,
            hint = "비밀번호 입력 (영문/숫자 조합 8~10자)",
            maxLength = 10,
            modifier = Modifier.padding(bottom = 12.dp),
            visualTransformation = PasswordVisualTransformation(),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 14.dp,
            )
        )

        QrizTextFiled(
            value = passwordCheck,
            onValueChange = onChangePasswordCheck,
            supportingText = passwordCheckSupportingText,
            singleLine = true,
            hint = "비밀번호 확인",
            maxLength = 10,
            visualTransformation = PasswordVisualTransformation(),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 14.dp,
            )
        )
    }
}

@Composable
private fun SignUpContent(
    title: String,
    subTitle: String,
    buttonText: String,
    buttonEnabled: Boolean,
    onButtonClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = 24.dp,
                horizontal = 18.dp,
            )
            .imePadding()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.W600
            ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        Text(
            text = subTitle,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier.padding(bottom = 32.dp),
        )

        content()

        Spacer(modifier = Modifier.weight(1f))

        QrizButton(
            enable = buttonEnabled,
            text = buttonText,
            onClick = onButtonClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
