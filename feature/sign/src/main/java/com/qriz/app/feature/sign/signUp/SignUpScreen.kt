package com.qriz.app.feature.sign.signUp

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
import com.qriz.app.feature.sign.signUp.component.SignUpEmailAuthPage
import com.qriz.app.feature.sign.signUp.component.SignUpEmailPage
import com.qriz.app.feature.sign.signUp.component.SignUpIdPage
import com.qriz.app.feature.sign.signUp.component.SignUpNamePage
import com.qriz.app.feature.sign.signUp.component.SignUpPasswordPage

const val SIGN_UP_PAGE = 5

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    onSignUp: () -> Unit,
    onBack: () -> Unit,
    onShowSnackbar: (String) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    viewModel.collectSideEffect {
        when (it) {
            SignUpUiEffect.SignUpUiComplete -> onSignUp()
            is SignUpUiEffect.ShowSnackBer -> onShowSnackbar(
                it.message ?: context.getString(it.defaultResId)
            )
        }
    }

    val pagerState = rememberPagerState { SIGN_UP_PAGE }

    LaunchedEffect(state.page) {
        pagerState.animateScrollToPage(state.page)
    }

    Column {
        QrizTopBar(
            title = stringResource(state.topBarTitleResId),
            navigationType = NavigationType.BACK,
            onNavigationClick = if (state.page == 0) onBack else viewModel::previousPage
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
                0 -> SignUpNamePage(
                    name = state.name,
                    validName = state.validName,
                    errorMessage = state.nameErrorMessage,
                    onNameChanged = viewModel::updateName,
                    onNext = viewModel::nextPage,
                )

                1 -> SignUpEmailPage(
                    email = state.email,
                    errorMessage = state.emailErrorMessage,
                    emailVerified = state.emailVerified,
                    onEmailChanged = viewModel::updateEmail,
                    onNext = viewModel::nextPage,
                )

                2 -> SignUpEmailAuthPage(
                    authenticationNumber = state.authenticationNumber,
                    authenticationState = state.authenticationState,
                    timer = state.timerText,
                    errorMessage = state.authenticationNumberErrorMessage,
                    onAuthenticationNumberChanged = viewModel::updateAuthenticationNumber,
                    onNext = viewModel::nextPage,
                    onRetry = viewModel::sendAuthenticationNumberEmail
                )

                3 -> SignUpIdPage(
                    id = state.id,
                    onIdChanged = viewModel::updateId,
                    onCheckDuplicate = viewModel::checkDuplicateId,
                    isAvailableId = state.isAvailableId,
                    errorMessage = state.idErrorMessage,
                    onNext = viewModel::nextPage,
                )

                4 -> SignUpPasswordPage(
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
