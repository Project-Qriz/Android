package com.qriz.app.feature.mypage.withdraw

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qriz.app.core.designsystem.component.NavigationType
import com.qriz.app.core.designsystem.component.QrizButton
import com.qriz.app.core.designsystem.component.QrizCard
import com.qriz.app.core.designsystem.component.QrizTopBar
import com.qriz.app.core.designsystem.theme.Gray100
import com.qriz.app.core.designsystem.theme.Gray400
import com.qriz.app.core.designsystem.theme.Gray500
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.feature.base.extention.collectSideEffect
import com.qriz.app.feature.mypage.R
import com.qriz.app.feature.mypage.withdraw.component.WithdrawConfirmDialog

@Composable
fun WithdrawScreen(
    viewModel: WithdrawViewModel = hiltViewModel(),
    onShowSnackBar: (String) -> Unit,
    onBack: () -> Unit,
    moveToLogin: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.collectSideEffect {
        when (it) {
            is WithdrawUiEffect.ShowSnackBar -> onShowSnackBar(it.message)

            is WithdrawUiEffect.NavigateToLogin -> moveToLogin()
        }
    }

    WithdrawContent(
        onNavigateBack = onBack,
        showConfirmDialog = uiState.showConfirmDialog,
        onClickWithdraw = { viewModel.process(WithdrawUiAction.ClickWithdraw) },
        onClickConfirmWithdraw = { viewModel.process(WithdrawUiAction.ConfirmWithdraw) },
        onDismissConfirmDialog = { viewModel.process(WithdrawUiAction.DismissConfirmDialog) }
    )
}

@Composable
fun WithdrawContent(
    showConfirmDialog: Boolean,
    onNavigateBack: () -> Unit,
    onClickWithdraw: () -> Unit,
    onClickConfirmWithdraw: () -> Unit,
    onDismissConfirmDialog: () -> Unit,
) {
    if (showConfirmDialog) {
        WithdrawConfirmDialog(
            onConfirmClick = onClickConfirmWithdraw,
            onDismissRequest = onDismissConfirmDialog
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        QrizTopBar(
            navigationType = NavigationType.BACK,
            title = stringResource(R.string.setting_withdraw),
            onNavigationClick = onNavigateBack,
        )

        Text(
            modifier = Modifier
                .padding(top = 40.dp)
                .padding(horizontal = 18.dp),
            text = stringResource(R.string.withdraw_title),
            style = QrizTheme.typography.heading1.copy(color = Gray800)
        )

        QrizCard(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 20.dp),
            border = BorderStroke(
                width = 1.dp,
                color = Gray100
            ),
            elevation = 0.dp,
        ) {
            Column(
                modifier = Modifier.padding(
                    vertical = 20.dp,
                    horizontal = 16.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                DescriptionRow(description = stringResource(R.string.withdraw_description_1))
                DescriptionRow(description = stringResource(R.string.withdraw_description_2))
            }
        }

        Text(
            modifier = Modifier.padding(
                start = 18.dp,
                top = 20.dp
            ),
            text = stringResource(R.string.withdraw_confirm_message),
            style = QrizTheme.typography.subhead.copy(color = Gray500)
        )

        QrizButton(
            modifier = Modifier
                .padding(horizontal = 18.dp)
                .padding(top = 12.dp)
                .fillMaxWidth(),
            text = stringResource(R.string.setting_withdraw),
            onClick = onClickWithdraw
        )
    }
}

@Composable
private fun DescriptionRow(
    description: String,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "\u2022",
            style = QrizTheme.typography.body2.copy(color = Gray500)
        )
        Text(
            text = description,
            style = QrizTheme.typography.body2.copy(color = Gray400)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WithdrawContentPreview() {
    QrizTheme {
        WithdrawContent(
            showConfirmDialog = false,
            onNavigateBack = {},
            onClickConfirmWithdraw = {},
            onDismissConfirmDialog = {},
            onClickWithdraw = {},
        )
    }
}
