package com.qriz.app.feature.onboard.guide

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.feature.onboard.R

@Composable
fun ConceptCheckGuideScreen(
    onNext: () -> Unit,
) {
    GuideBaseScreen(
        title = stringResource(R.string.concept_check_guide_title),
        subTitle = stringResource(R.string.concept_check_guide_sub_title),
        image = R.drawable.img_onboard_check,
        buttonText = stringResource(R.string.concept_check_guide_button_text),
        onNext = onNext
    )
}

@Preview(showBackground = true)
@Composable
private fun ConceptCheckGuideScreenPreview() {
    QrizTheme {
        ConceptCheckGuideScreen(
            onNext = {}
        )
    }
}
