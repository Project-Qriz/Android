package com.qriz.app.feature.daily_study.status.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.theme.Gray500
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White

@Composable
internal fun PlannedSkillCard(
    modifier: Modifier = Modifier,
    keyConcept: String,
    description: String,
) {
    Column (
        modifier = modifier.fillMaxWidth()
            .background(
                color = White,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(
                vertical = 20.dp,
                horizontal = 16.dp
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = keyConcept,
            style = QrizTheme.typography.headline1.copy(color = Gray800)
        )

        Text(
            text = description,
            style = QrizTheme.typography.body2.copy(color = Gray500)
        )
    }
}
