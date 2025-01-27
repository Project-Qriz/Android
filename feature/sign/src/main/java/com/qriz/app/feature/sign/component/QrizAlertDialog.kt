package com.qriz.app.feature.sign.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.qriz.app.core.designsystem.component.QrizButton
import com.qriz.app.core.designsystem.theme.Black
import com.qriz.app.core.designsystem.theme.Gray500
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White

@Composable
fun QrizAlertDialog(
    title: String,
    message: String,
    confirmText: String = "확인",
    onConfirmRequest: () -> Unit,
) {
    Dialog(
        onDismissRequest = onConfirmRequest,
    ) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = White,
            modifier = Modifier.padding(20.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
            ) {
                Text(
                    text = title,
                    style = QrizTheme.typography.headline1,
                    color = Black,
                )
                Text(
                    text = message,
                    style = QrizTheme.typography.body2,
                    color = Gray500,
                    modifier = Modifier.padding(top = 10.dp)
                )
                QrizButton(
                    text = confirmText,
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                    onClick = onConfirmRequest,

                )
            }
        }
    }
}

@Preview
@Composable
fun QrizAlertDialogPreview() {
    QrizAlertDialog(
        title = "테스트",
        message = "테스트 입니다.",
        onConfirmRequest = {}
    )
}
