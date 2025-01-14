package com.qriz.app.feature.sign.signup

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qriz.app.core.designsystem.component.NavigationType
import com.qriz.app.core.designsystem.component.QrizTopBar
import com.qriz.app.core.designsystem.theme.Blue600
import com.qriz.app.core.designsystem.theme.Gray200
import com.qriz.app.feature.base.extention.collectSideEffect
import com.qriz.app.feature.sign.signup.SignUpUiState.SignUpPage
import com.qriz.app.feature.sign.signup.SignUpUiState.SignUpPage.EMAIL
import com.qriz.app.feature.sign.signup.SignUpUiState.SignUpPage.EMAIL_AUTH
import com.qriz.app.feature.sign.signup.SignUpUiState.SignUpPage.ID
import com.qriz.app.feature.sign.signup.SignUpUiState.SignUpPage.NAME
import com.qriz.app.feature.sign.signup.SignUpUiState.SignUpPage.PW
import com.qriz.app.feature.sign.signup.component.SignUpEmailAuthPage
import com.qriz.app.feature.sign.signup.component.SignUpEmailPage
import com.qriz.app.feature.sign.signup.component.SignUpIdPage
import com.qriz.app.feature.sign.signup.component.SignUpNamePage
import com.qriz.app.feature.sign.signup.component.SignUpPasswordPage

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    onSignUpComplete: () -> Unit,
    onBack: () -> Unit,
    onShowSnackbar: (String) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    viewModel.collectSideEffect {
        when (it) {
            SignUpUiEffect.SignUpUiComplete -> onSignUpComplete()
            is SignUpUiEffect.ShowSnackBer -> onShowSnackbar(
                it.message ?: context.getString(it.defaultResId)
            )
        }
    }

    BackHandler {
        if (state.page == NAME) onBack()
        else viewModel.process(SignUpUiAction.ClickPreviousPage)
    }

    SignUpContent(
        uiState = state,
        onClickPreviousPage = {
            if (state.page == NAME) onBack()
            else viewModel.process(SignUpUiAction.ClickPreviousPage)
        },
        onClickNextPage = { viewModel.process(SignUpUiAction.ClickNextPage) },
        onClickEmailAuthNumSend = { viewModel.process(SignUpUiAction.ClickEmailAuthNumSend) },
        onClickIdDuplicateCheck = { viewModel.process(SignUpUiAction.ClickIdDuplicateCheck) },
        onClickSignUp = { viewModel.process(SignUpUiAction.ClickSignUp) },
        onChangeUserName = { viewModel.process(SignUpUiAction.ChangeUserName(it)) },
        onChangeUserId = { viewModel.process(SignUpUiAction.ChangeUserId(it)) },
        onChangeUserPw = { viewModel.process(SignUpUiAction.ChangeUserPw(it)) },
        onChangeUserPwCheck = { viewModel.process(SignUpUiAction.ChangeUserPwCheck(it)) },
        onChangeEmail = { viewModel.process(SignUpUiAction.ChangeEmail(it)) },
        onChangeEmailAuthNum = { viewModel.process(SignUpUiAction.ChangeEmailAuthNum(it)) },
    )
}

@Composable
fun SignUpContent(
    uiState: SignUpUiState,
    onClickPreviousPage: () -> Unit,
    onClickNextPage: () -> Unit,
    onClickEmailAuthNumSend: () -> Unit,
    onClickIdDuplicateCheck: () -> Unit,
    onClickSignUp: () -> Unit,
    onChangeUserName: (String) -> Unit,
    onChangeUserId: (String) -> Unit,
    onChangeUserPw: (String) -> Unit,
    onChangeUserPwCheck: (String) -> Unit,
    onChangeEmail: (String) -> Unit,
    onChangeEmailAuthNum: (String) -> Unit,
) {
    val pagerState = rememberPagerState { SignUpPage.entries.size }
    val currentPage = uiState.page

    LaunchedEffect(currentPage) {
        pagerState.animateScrollToPage(currentPage.index)
    }

    Column {
        QrizTopBar(
            title = stringResource(uiState.topBarTitleResId),
            navigationType = NavigationType.BACK,
            onNavigationClick = onClickPreviousPage
        )
        LinearProgressIndicator(
            progress = { currentPage.percent },
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
                NAME.index -> SignUpNamePage(
                    name = uiState.name,
                    isValidName = uiState.isValidName,
                    errorMessage = stringResource(uiState.nameErrorMessageResId),
                    onChangeUserName = onChangeUserName,
                    onClickNextPage = onClickNextPage,
                )

                EMAIL.index -> SignUpEmailPage(
                    email = uiState.email,
                    errorMessage = stringResource(uiState.emailErrorMessageResId),
                    isValidEmail = uiState.isValidEmail,
                    onChangeEmail = onChangeEmail,
                    onClickNextPage = onClickNextPage,
                )

                EMAIL_AUTH.index -> SignUpEmailAuthPage(
                    emailAuthNumber = uiState.emailAuthNumber,
                    isVerifiedEmailAuth = uiState.isVerifiedEmailAuth,
                    timer = uiState.timerText,
                    errorMessage = stringResource(uiState.emailAuthNumberErrorMessageResId),
                    onChangeEmailAuthNum = onChangeEmailAuthNum,
                    onClickNextPage = onClickNextPage,
                    onClickEmailAuthNumSend = onClickEmailAuthNumSend
                )

                ID.index -> SignUpIdPage(
                    id = uiState.id,
                    onChangeUserId = onChangeUserId,
                    onClickIdDuplicateCheck = onClickIdDuplicateCheck,
                    isAvailableId = uiState.isValidId && uiState.isNotDuplicatedId,
                    errorMessage = stringResource(uiState.idErrorMessageResId),
                    onClickNextPage = onClickNextPage,
                )

                PW.index -> SignUpPasswordPage(
                    password = uiState.pw,
                    passwordCheck = uiState.pwCheck,
                    passwordErrorMessage = stringResource(uiState.pwErrorMessageResId),
                    passwordCheckErrorMessage = stringResource(uiState.pwCheckErrorMessageResId),
                    canSignUp = uiState.canSignUp,
                    onChangeUserPw = onChangeUserPw,
                    onChangeUserPwCheck = onChangeUserPwCheck,
                    onClickSignUp = onClickSignUp,
                )
            }
        }
    }
}
