package com.qriz.app.feature.onboard.guide

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.qriz.app.core.designsystem.component.QrizDialog
import com.qriz.app.feature.onboard.R

@Composable
fun TestEndWarningDialog(
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    QrizDialog(
        title = stringResource(R.string.test_end_warning_title),
        description = stringResource(R.string.test_end_warning_sub_title),
        cancelText = stringResource(R.string.cancel),
        confirmText = stringResource(R.string.confirm),
        onCancelClick = onCancelClick,
        onConfirmClick = onConfirmClick,
        onDismissRequest = onCancelClick
    )
}

@Preview(showBackground = true)
@Composable
private fun TestEndWarningDialogPreview() {
    TestEndWarningDialog(
        onCancelClick = { },
        onConfirmClick = { },
    )
}
