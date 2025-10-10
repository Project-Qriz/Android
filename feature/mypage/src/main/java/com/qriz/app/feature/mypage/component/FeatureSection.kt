package com.qriz.app.feature.mypage.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.QrizCard
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.feature.mypage.R

@Composable
internal fun FeatureSection(
    modifier: Modifier = Modifier,
    onClickResetPlan: () -> Unit,
    onClickRegisterExam: () -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FeatureItem(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.my_plan_reset),
            iconRes = R.drawable.ic_reset,
            onClick = onClickResetPlan,
        )
        FeatureItem(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.my_exam_register),
            iconRes = R.drawable.ic_exam_register,
            onClick = onClickRegisterExam,
        )
    }
}

@Composable
private fun FeatureItem(
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int,
    title: String,
    onClick: () -> Unit,
) {
    QrizCard(
        modifier = modifier.clickable(onClick = onClick),
        cornerRadius = 16.dp,
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = ImageVector.vectorResource(iconRes),
                tint = Color.Unspecified,
                contentDescription = null,
            )

            Text(
                text = title,
                style = QrizTheme.typography.body1.copy(color = Gray800)
            )
        }
    }
}

@Preview
@Composable
private fun FeatureSectionPreview() {
    QrizTheme {
        FeatureSection(
            onClickResetPlan = {},
            onClickRegisterExam = {},
        )
    }
}
