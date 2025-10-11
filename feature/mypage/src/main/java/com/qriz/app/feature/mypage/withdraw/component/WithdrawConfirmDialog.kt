package com.qriz.app.feature.mypage.withdraw.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.qriz.app.core.designsystem.component.QrizDialog
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.feature.mypage.R

@Composable
internal fun WithdrawConfirmDialog(
    onConfirmClick: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    QrizDialog(
        title = stringResource(R.string.setting_withdraw),
        description = stringResource(R.string.withdraw_confirm_dialog_message),
        confirmText = stringResource(R.string.withdraw_confirm_dialog_confirm_text),
        cancelText = stringResource(R.string.withdraw_confirm_dialog_cancel_text),
        onConfirmClick = onConfirmClick,
        onDismissRequest = onDismissRequest,
        onCancelClick = onDismissRequest,
    )
}

@Preview(showBackground = true)
@Composable
private fun WithdrawConfirmDialogPreview() {
    QrizTheme {
        WithdrawConfirmDialog(
            onConfirmClick = {},
            onDismissRequest = {},
        )
    }
}
