package com.qriz.app.feature.concept_book.list.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.R
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme

@Composable
fun ConceptBookItem(
    name: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        tonalElevation = 0.dp,
        shadowElevation = 2.dp,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.clickable { onClick() },
    ) {
        Row(
            modifier.padding(
                horizontal = 16.dp,
                vertical = 20.dp
            )
        ) {
            Text(
                text = name,
                style = QrizTheme.typography.headline2,
                color = Gray800,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_keyboard_arrow_right),
                contentDescription = null,
            )
        }
    }
}

@Preview
@Composable
private fun ConceptBookItemPreview() {
    MaterialTheme {
        ConceptBookItem(
            name = "데이터 모델의 이해",
            onClick = {}
        )
    }
}
