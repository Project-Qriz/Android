package com.qriz.app.core.ui.common.const

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.qriz.app.core.designsystem.component.QrizDialog
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.ui.common.R

@Composable
fun ErrorDialog(
    description: String? = null,
    onConfirmClick: () -> Unit,
) {
    QrizDialog(
        title = stringResource(R.string.error_occurs),
        description =
        if (description.isNullOrBlank()) stringResource(R.string.unknown_error_occurs)
        else description,
        onConfirmClick = onConfirmClick
    )
}

@Preview(showBackground = true)
@Composable
private fun UnknownErrorDialogPreview() {
    QrizTheme {
        ErrorDialog(
            onConfirmClick = { },
        )
    }
}
