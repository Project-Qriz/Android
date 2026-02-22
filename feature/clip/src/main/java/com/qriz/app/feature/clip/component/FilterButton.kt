package com.qriz.app.feature.clip.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.theme.QrizTheme

@Composable
internal fun FilterButton(
    text: String,
    contentColor: Color,
    containerColor: Color,
    borderColor: Color,
    @DrawableRes icon: Int,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = borderColor,
                ),
                shape = RoundedCornerShape(25.dp)
            )
            .background(
                color = containerColor,
                shape = RoundedCornerShape(25.dp)
            )
            .clickable(onClick = onClick)
            .padding(
                horizontal = 12.dp,
                vertical = 6.dp,
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = QrizTheme.typography.body2,
                color = contentColor
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = ImageVector.vectorResource(id = icon),
                contentDescription = null,
                tint = contentColor,
            )
        }
    }
}
