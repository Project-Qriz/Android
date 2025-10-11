package com.qriz.app.feature.mypage.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qriz.app.core.designsystem.component.NavigationType
import com.qriz.app.core.designsystem.component.QrizTopBar
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.feature.base.extention.collectSideEffect
import com.qriz.app.feature.mypage.R
import com.qriz.app.feature.mypage.setting.component.LogoutDialog
import com.qriz.app.feature.mypage.setting.component.SettingItemCard
import com.qriz.app.feature.mypage.setting.component.UserCard

@Composable
fun SettingScreen(
    viewModel: SettingViewModel = hiltViewModel(),
    onShowSnackBar: (String) -> Unit,
    onBack: () -> Unit,
    moveToResetPassword: () -> Unit,
    moveToLogin: () -> Unit,
    moveToWithDraw: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    viewModel.collectSideEffect {
        when (it) {
            is SettingUiEffect.ShowSnackBar -> onShowSnackBar(
                it.message ?: context.getString(it.defaultResId)
            )
            is SettingUiEffect.NavigateToResetPassword -> moveToResetPassword()
            is SettingUiEffect.NavigateToLogin -> moveToLogin()
            is SettingUiEffect.NavigateToWithdraw -> moveToWithDraw()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.process(SettingUiAction.LoadData)
    }

    SettingContent(
        userName = uiState.user.name,
        email = uiState.user.email,
        showLogoutDialog = uiState.showLogoutDialog,
        onNavigateBack = onBack,
        onClickResetPassword = { viewModel.process(SettingUiAction.ClickResetPassword) },
        onClickLogout = { viewModel.process(SettingUiAction.ClickLogout) },
        onClickWithdraw = { viewModel.process(SettingUiAction.ClickWithdraw) },
        onConfirmLogout = { viewModel.process(SettingUiAction.ConfirmLogout) },
        onDismissLogoutDialog = { viewModel.process(SettingUiAction.DismissLogoutDialog) },
    )
}

@Composable
fun SettingContent(
    userName: String,
    email: String,
    showLogoutDialog: Boolean,
    onNavigateBack: () -> Unit,
    onClickResetPassword: () -> Unit,
    onClickLogout: () -> Unit,
    onClickWithdraw: () -> Unit,
    onConfirmLogout: () -> Unit,
    onDismissLogoutDialog: () -> Unit,
) {
    if (showLogoutDialog) {
        LogoutDialog(
            onClickConfirm = onConfirmLogout,
            onClickCancel = onDismissLogoutDialog,
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        QrizTopBar(
            navigationType = NavigationType.BACK,
            title = stringResource(R.string.setting_title),
            onNavigationClick = onNavigateBack,
        )

        Column(
            modifier = Modifier.padding(
                vertical = 24.dp,
                horizontal = 18.dp
            )
        ) {
            UserCard(
                userName = userName,
                email = email,
            )

            SettingItemCard(
                modifier = Modifier.padding(top = 24.dp),
                title = stringResource(R.string.setting_reset_password),
                onClick = onClickResetPassword,
            )

            SettingItemCard(
                modifier = Modifier.padding(top = 8.dp),
                title = stringResource(R.string.setting_logout),
                onClick = onClickLogout,
            )

            SettingItemCard(
                modifier = Modifier.padding(top = 8.dp),
                title = stringResource(R.string.setting_withdraw),
                onClick = onClickWithdraw,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingContentPreview() {
    QrizTheme {
        SettingContent(
            userName = "홍길동",
            email = "hong@example.com",
            showLogoutDialog = false,
            onNavigateBack = {},
            onClickResetPassword = {},
            onClickLogout = {},
            onClickWithdraw = {},
            onConfirmLogout = {},
            onDismissLogoutDialog = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingContentWithDialogPreview() {
    QrizTheme {
        SettingContent(
            userName = "홍길동",
            email = "hong@example.com",
            showLogoutDialog = true,
            onNavigateBack = {},
            onClickResetPassword = {},
            onClickLogout = {},
            onClickWithdraw = {},
            onConfirmLogout = {},
            onDismissLogoutDialog = {},
        )
    }
}
