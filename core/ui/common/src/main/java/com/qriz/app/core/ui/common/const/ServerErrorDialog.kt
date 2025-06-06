package com.qriz.app.core.ui.common.const

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.qriz.app.core.designsystem.component.QrizDialog
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.ui.common.R

@Composable
fun ServerErrorDialog(
    onConfirmClick: () -> Unit,
) {
    QrizDialog(
        title = stringResource(R.string.server_error_occurs),
        description = stringResource(R.string.server_error_please_try_again),
        onConfirmClick = onConfirmClick
    )
}

@Preview(showBackground = true)
@Composable
private fun ServerErrorDialogPreview() {
    QrizTheme {
        ServerErrorDialog(
            onConfirmClick = { },
        )
    }
}
