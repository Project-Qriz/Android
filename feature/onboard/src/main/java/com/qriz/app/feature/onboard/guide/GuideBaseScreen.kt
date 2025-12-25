package com.qriz.app.feature.onboard.guide

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qriz.app.core.designsystem.component.QrizButton
import com.qriz.app.core.designsystem.theme.Gray500
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White

@Composable
fun GuideBaseScreen(
    title: String,
    subTitle: String,
    buttonText: String? = null,
    @DrawableRes image: Int,
    onNext: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .systemBarsPadding()
            .padding(bottom = 16.dp)
    ) {
        Text(
            text = title,
            style = QrizTheme.typography.title1.copy(
                lineHeight = QrizTheme.typography.title1.fontSize * 1.3f
            ),
            color = Gray800,
            modifier = Modifier.padding(
                top = 132.dp,
                bottom = 12.dp,
                start = 24.dp,
            ),
        )

        Text(
            text = subTitle,
            style = QrizTheme.typography.body1Long.copy(
                lineHeight = QrizTheme.typography.body1Long.fontSize * 1.3f
            ),
            color = Gray500,
            modifier = Modifier.padding(
                bottom = 40.dp,
                start = 24.dp,
            ),
        )

        Image(
            painter = painterResource(image),
            contentDescription = "",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        if (buttonText != null) {
            QrizButton(
                text = buttonText,
                enable = true,
                onClick = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp)
            )
        }
    }
}
