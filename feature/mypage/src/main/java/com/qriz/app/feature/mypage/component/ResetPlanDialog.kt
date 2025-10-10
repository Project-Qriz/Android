package com.qriz.app.feature.mypage.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.qriz.app.core.designsystem.component.QrizDialog
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.feature.mypage.R
import com.qriz.app.core.designsystem.R as DSR

@Composable
internal fun ResetPlanDialog(
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    QrizDialog(
        title = stringResource(R.string.my_reset_plan_dialog_title),
        description = stringResource(R.string.my_reset_plan_dialog_message),
        cancelText = stringResource(DSR.string.cancel),
        onConfirmClick = onConfirm,
        onCancelClick = onDismissRequest,
        onDismissRequest = onDismissRequest,
    )
}

@Preview
@Composable
private fun ResetPlanDialogPreview() {
    QrizTheme {
        ResetPlanDialog(
            onConfirm = {},
            onDismissRequest = {}
        )
    }
}
