package com.qriz.app.core.ui.test

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.qriz.app.core.designsystem.component.QrizDialog
import com.qriz.app.core.designsystem.theme.QrizTheme

@Composable
fun TestSubmitWarningDialog(
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    QrizDialog(
        title = stringResource(R.string.submit_warning_title),
        description = stringResource(R.string.submit_warning_sub_title),
        cancelText = stringResource(R.string.cancel),
        confirmText = stringResource(R.string.confirm),
        onCancelClick = onCancelClick,
        onConfirmClick = onConfirmClick,
        onDismissRequest = onCancelClick
    )
}


@Preview(showBackground = true)
@Composable
private fun TestSubmitWarningDialogPreview() {
    QrizTheme {
        TestSubmitWarningDialog(
            onCancelClick = { },
            onConfirmClick = { },
        )
    }
}
