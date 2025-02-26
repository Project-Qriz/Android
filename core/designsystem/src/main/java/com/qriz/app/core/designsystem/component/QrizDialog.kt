package com.qriz.app.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.qriz.app.core.designsystem.R
import com.qriz.app.core.designsystem.theme.Blue500
import com.qriz.app.core.designsystem.theme.Gray500
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White

@Composable
fun QrizDialog(
    title: String,
    description: String,
    confirmText: String = stringResource(R.string.confirmation),
    cancelText: String? = null,
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(color = White)
                .padding(20.dp),
        ) {
            Text(
                text = title,
                style = QrizTheme.typography.headline1,
                color = Gray800
            )

            Text(
                text = description,
                style = QrizTheme.typography.body2Long,
                color = Gray500,
                modifier = Modifier.padding(
                    top = 4.dp,
                    bottom = 16.dp
                )
            )

            Row {
                if (cancelText != null) {
                    QrizButton(
                        text = cancelText,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        containerColor = White,
                        textColor = Blue500,
                        strokeColor = Blue500,
                        onClick = onCancelClick,
                    )

                    QrizButton(
                        text = confirmText,
                        modifier = Modifier
                            .weight(1f),
                        onClick = onConfirmClick,
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = confirmText,
                        style = QrizTheme.typography.headline2
                            .copy(fontWeight = FontWeight.SemiBold),
                        color = Blue500,
                        modifier = Modifier
                            .clickable(onClick = onConfirmClick)
                    )
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun QrizDialogPreview() {
    QrizTheme {
        QrizDialog(
            title = "제출하시겠습니까?",
            description = "확인 버튼을 누르면 다시 돌아올 수 없어요.",
            cancelText = "취소",
            confirmText = "확인",
            onCancelClick = { },
            onConfirmClick = { },
            onDismissRequest = { }
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun QrizDialog2Preview() {
    QrizTheme {
        QrizDialog(
            title = "네트워크 오류 발생",
            description = "인터넷에 연결되어 있지 않는 것 같습니다. \n다시 시도해 주세요.",
            confirmText = "확인",
            onCancelClick = { },
            onConfirmClick = { },
            onDismissRequest = { }
        )
    }
}
