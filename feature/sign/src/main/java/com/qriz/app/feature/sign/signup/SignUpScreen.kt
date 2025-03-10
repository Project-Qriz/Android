package com.qriz.app.feature.sign.signup

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.feature.base.extention.collectSideEffect
import com.qriz.app.feature.sign.signup.SignUpUiState.SignUpPage
import com.qriz.app.feature.sign.signup.SignUpUiState.SignUpPage.ID
import com.qriz.app.feature.sign.signup.SignUpUiState.SignUpPage.NAME
import com.qriz.app.feature.sign.signup.SignUpUiState.SignUpPage.PW
import com.qriz.app.feature.sign.signup.SignUpUiState.SignUpPage.AUTH
import com.qriz.app.feature.sign.signup.component.SignUpAuthPage
import com.qriz.app.feature.sign.signup.component.SignUpIdPage
import com.qriz.app.feature.sign.signup.component.SignUpNamePage
import com.qriz.app.feature.sign.signup.component.SignUpPasswordPage

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    moveToConceptCheckGuide: () -> Unit,
    onBack: () -> Unit,
    onShowSnackbar: (String) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    viewModel.collectSideEffect {
        when (it) {
            SignUpUiEffect.SignUpUiComplete -> moveToConceptCheckGuide()
            is SignUpUiEffect.ShowSnackBer -> onShowSnackbar(
                it.message ?: context.getString(it.defaultResId)
            )
            is SignUpUiEffect.MoveToBack -> onBack()
        }
    }

    BackHandler {
        viewModel.process(SignUpUiAction.ClickPreviousPage)
    }

    SignUpContent(
        uiState = state,
        onClickPreviousPage = { viewModel.process(SignUpUiAction.ClickPreviousPage) },
        onClickNextPage = { viewModel.process(SignUpUiAction.ClickNextPage) },
        onClickEmailAuthNumSend = { viewModel.process(SignUpUiAction.ClickEmailAuthNumSend) },
        onClickIdDuplicateCheck = { viewModel.process(SignUpUiAction.ClickIdDuplicateCheck) },
        onClickSignUp = { viewModel.process(SignUpUiAction.ClickSignUp) },
        onClickVerifyAuthNumber = { viewModel.process(SignUpUiAction.ClickVerifyAuthNum) },
        onChangeFocus = { viewModel.process(SignUpUiAction.ChangeFocusState(it)) },
        onChangeUserName = { viewModel.process(SignUpUiAction.ChangeUserName(it)) },
        onChangeUserId = { viewModel.process(SignUpUiAction.ChangeUserId(it)) },
        onChangeUserPw = { viewModel.process(SignUpUiAction.ChangeUserPw(it)) },
        onChangeUserPwCheck = { viewModel.process(SignUpUiAction.ChangeUserPwCheck(it)) },
        onChangeEmail = { viewModel.process(SignUpUiAction.ChangeEmail(it)) },
        onChangeEmailAuthNum = { viewModel.process(SignUpUiAction.ChangeEmailAuthNum(it)) },
        onChangePasswordVisibility = { viewModel.process(SignUpUiAction.ChangePasswordVisibility(it)) },
        onChangePasswordCheckVisibility = { viewModel.process(SignUpUiAction.ChangePasswordCheckVisibility(it)) },
    )
}

@Composable
private fun SignUpContent(
    uiState: SignUpUiState,
    onClickPreviousPage: () -> Unit,
    onClickNextPage: () -> Unit,
    onClickEmailAuthNumSend: () -> Unit,
    onClickVerifyAuthNumber: () -> Unit,
    onClickIdDuplicateCheck: () -> Unit,
    onClickSignUp: () -> Unit,
    onChangeFocus: (SignUpUiState.FocusState) -> Unit,
    onChangeUserName: (String) -> Unit,
    onChangeUserId: (String) -> Unit,
    onChangeUserPw: (String) -> Unit,
    onChangeUserPwCheck: (String) -> Unit,
    onChangeEmail: (String) -> Unit,
    onChangeEmailAuthNum: (String) -> Unit,
    onChangePasswordVisibility: (Boolean) -> Unit,
    onChangePasswordCheckVisibility: (Boolean) -> Unit,
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
            modifier = Modifier.background(color = White)
        ) { page ->
            when (page) {
                AUTH.index -> SignUpAuthPage(
                    email = uiState.email,
                    isValidEmail = uiState.isValidEmail,
                    authNumber = uiState.emailAuthNumber,
                    authTimerText = uiState.timerText,
                    showAuthNumberLayout = uiState.showAuthNumberLayout,
                    verifiedAuthNumber = uiState.isVerifiedEmailAuth,
                    focusState = uiState.focusState,
                    enableInputAuthNumber = uiState.isTimeExpiredEmailAuth.not(),
                    emailSupportingTextResId = uiState.emailSupportingTextResId,
                    authNumberSupportingTextResId = uiState.authNumberSupportingTextResId,
                    isTimeExpiredEmailAuth = uiState.isTimeExpiredEmailAuth,
                    enableAuthNumVerifyButton = uiState.enableAuthNumVerifyButton,
                    onEmailChanged = onChangeEmail,
                    onAuthNumberChanged = onChangeEmailAuthNum,
                    onSendAuthNumberEmail = onClickEmailAuthNumSend,
                    onClickNextPage = onClickNextPage,
                    onChangeFocus = onChangeFocus,
                    onVerifyAuthNumber = onClickVerifyAuthNumber,
                )

                NAME.index -> SignUpNamePage(
                    name = uiState.name,
                    isValidName = uiState.isValidName,
                    focusState = uiState.focusState,
                    errorMessage = stringResource(uiState.nameErrorMessageResId),
                    onChangeUserName = onChangeUserName,
                    onClickNextPage = onClickNextPage,
                    onFocused = { onChangeFocus(SignUpUiState.FocusState.NAME) }
                )

                ID.index -> SignUpIdPage(
                    id = uiState.id,
                    onChangeUserId = onChangeUserId,
                    isAvailableId = uiState.isAvailableId,
                    isNotAvailableId = uiState.isNotAvailableId,
                    focusState = uiState.focusState,
                    onClickIdDuplicateCheck = onClickIdDuplicateCheck,
                    errorMessage = stringResource(uiState.idErrorMessageResId),
                    onClickNextPage = onClickNextPage,
                    onFocused = { onChangeFocus(SignUpUiState.FocusState.ID) }
                )

                PW.index -> SignUpPasswordPage(
                    password = uiState.pw,
                    passwordCheck = uiState.pwCheck,
                    isVisiblePassword = uiState.isVisiblePassword,
                    isVisiblePasswordCheck = uiState.isVisiblePasswordCheck,
                    isPasswordValidFormat = uiState.isPasswordValidFormat,
                    isPasswordValidLength = uiState.isPasswordValidLength,
                    isEqualsPassword = uiState.isEqualsPassword,
                    passwordCheckErrorMessage = stringResource(uiState.pwCheckErrorMessageResId),
                    canSignUp = uiState.canSignUp,
                    onChangeUserPw = onChangeUserPw,
                    onChangeUserPwCheck = onChangeUserPwCheck,
                    onClickSignUp = onClickSignUp,
                    onChangePasswordVisibility = onChangePasswordVisibility,
                    onChangePasswordCheckVisibility = onChangePasswordCheckVisibility,
                    focusState = uiState.focusState,
                    onChangeFocusState = onChangeFocus
                )
            }
        }
    }
}
