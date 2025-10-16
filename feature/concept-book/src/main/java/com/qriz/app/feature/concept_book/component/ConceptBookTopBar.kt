package com.qriz.app.feature.concept_book.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.theme.Gray500
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.core.designsystem.R as DSR

@Composable
internal fun ConceptBookTopBar(
    subTitle: String,
    title: String,
    onNavigationClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .statusBarsPadding()
            .height(48.dp)
    ) {
        IconButton(
            onClick = onNavigationClick,
            modifier = Modifier.align(Alignment.CenterStart),
            content = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = DSR.drawable.back_icon),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = Gray800
                )
            }
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center),
        ) {
            Text(
                text = subTitle,
                style = QrizTheme.typography.caption,
                color = Gray500,
            )
            Text(
                text = title,
                style = QrizTheme.typography.headline1,
                color = Gray800,
            )
        }
    }
}
