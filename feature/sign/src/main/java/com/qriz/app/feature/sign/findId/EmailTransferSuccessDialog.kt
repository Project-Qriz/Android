package com.qriz.app.feature.sign.findId

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.qriz.app.core.designsystem.component.QrizDialog
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.feature.sign.R

@Composable
fun EmailTransferSuccessDialog(
    onConfirmClick: () -> Unit,
) {
    QrizDialog(
        title = stringResource(R.string.send_email_success),
        description = stringResource(R.string.check_email_to_find_id),
        onConfirmClick = onConfirmClick
    )
}

@Preview(showBackground = true)
@Composable
private fun EmailTransferSuccessDialogPreview() {
    QrizTheme {
        EmailTransferSuccessDialog(
            onConfirmClick = { },
        )
    }
}
