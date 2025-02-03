package com.qriz.app.feature.onboard.guide

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.qriz.app.feature.onboard.R

@Composable
fun PreviewGuideScreen(
    onNext: () -> Unit,
) {
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
fun PreviewPreviewGuideScreen() {
    PreviewGuideScreen(
        onNext = {}
    )
}
