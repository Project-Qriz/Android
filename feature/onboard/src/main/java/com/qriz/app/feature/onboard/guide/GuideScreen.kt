package com.qriz.app.feature.onboard.guide

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.QrizButton

@Composable
fun GuideScreen(
    title: String,
    subTitle: String,
    buttonText: String,
    @DrawableRes image: Int,
    onNext: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.W600
            ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(
                top = 132.dp,
                bottom = 8.dp,
                start = 24.dp,
            ),
        )

        Text(
            text = subTitle,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSecondary,
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

        QrizButton(
            text = buttonText,
            enable = true,
            onClick = onNext,
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 18.dp)
        )
    }
}
