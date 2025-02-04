package com.qriz.app.feature.onboard.guide

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.feature.onboard.R

@Composable
fun PreviewGuideScreen(
    onNext: () -> Unit,
    moveToHome: () -> Unit,
) {
    val isVisibleWarningDialog = rememberSaveable { mutableStateOf(false) }

    BackHandler {
        isVisibleWarningDialog.value = isVisibleWarningDialog.value.not()
    }

    if (isVisibleWarningDialog.value) {
        TestEndWarningDialog(
            onCancelClick = { isVisibleWarningDialog.value = false },
            onConfirmClick = {
                moveToHome()
                isVisibleWarningDialog.value = false
            }
        )
    }

    //TODO : Survey는 완료되었으니 뒤로가기하면 경고 다이얼로그 띄우고, 홈으로 가야됨
    GuideBaseScreen(
        title = stringResource(R.string.preview_guide_title),
        subTitle = stringResource(R.string.preview_guide_sub_title),
        buttonText = stringResource(R.string.preview_guide_button_text),
        image = R.drawable.img_onboard_test,
        onNext = onNext,
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewGuideScreenPreview() {
    QrizTheme {
        PreviewGuideScreen(
            onNext = {},
            moveToHome = {}
        )
    }
}
