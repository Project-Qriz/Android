package com.qriz.app.core.ui.test

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.qriz.app.core.designsystem.component.QrizDialog

@Composable
fun TestEndWarningDialog(
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    QrizDialog(
        title = stringResource(R.string.cancel_test),
        description = stringResource(R.string.cancel_test_warning),
        cancelText = stringResource(R.string.cancel),
        confirmText = stringResource(R.string.continue_test),
        onCancelClick = onCancelClick,
        onConfirmClick = onConfirmClick,
        onDismissRequest = onCancelClick,
    )
}
