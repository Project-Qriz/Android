package com.qriz.app.feature.clip.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.QrizButton
import com.qriz.app.core.designsystem.theme.Blue50
import com.qriz.app.core.designsystem.theme.Gray200
import com.qriz.app.core.designsystem.theme.Gray300
import com.qriz.app.core.designsystem.theme.Gray500
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.feature.clip.R

@Composable
internal fun EmptyClips(
    onClickToStudy: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Blue50)
            .padding(
                vertical = 24.dp,
                horizontal = 16.dp,
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Gray200,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.empty_clip_title),
                style = QrizTheme.typography.body2,
                color = Gray300
            )
            Icon(
                imageVector = ImageVector.vectorResource(com.qriz.app.core.designsystem.R.drawable.arrow_down_icon),
                tint = Gray300,
                contentDescription = null,
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                        .size(32.dp),
                    imageVector = ImageVector.vectorResource(com.qriz.app.core.designsystem.R.drawable.qriz_app_logo),
                    contentDescription = null,
                )
                Text(
                    text = stringResource(R.string.empty_clip_title),
                    style = QrizTheme.typography.subhead.copy(fontWeight = FontWeight.ExtraBold),
                    color = Gray800,
                )
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = stringResource(R.string.empty_clip_description),
                    style = QrizTheme.typography.body2,
                    color = Gray500,
                )
                QrizButton(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .width(227.dp),
                    text = stringResource(R.string.go_to_study),
                    onClick = onClickToStudy
                )
            }
        }
    }
}

@Preview
@Composable
private fun EmptyClipsPreview() {
    QrizTheme {
        EmptyClips { }
    }
}
