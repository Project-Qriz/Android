package com.qriz.app.feature.mypage.setting.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.qriz.app.core.designsystem.component.QrizDialog
import com.qriz.app.feature.mypage.R
import com.qriz.app.core.designsystem.R as DSR

@Composable
internal fun LogoutDialog(
    onClickConfirm: () -> Unit,
    onClickCancel: () -> Unit,
) {
    QrizDialog(
        title = stringResource(R.string.setting_logout),
        description = stringResource(R.string.setting_logout_message),
        confirmText = stringResource(R.string.setting_logout),
        cancelText = stringResource(DSR.string.cancel),
        onCancelClick = onClickCancel,
        onDismissRequest = onClickCancel,
        onConfirmClick = onClickConfirm,
    )
}
