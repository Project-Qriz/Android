package com.qriz.app.feature.concept_book.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.box.QrizBox
import com.qriz.app.core.designsystem.theme.Blue100
import com.qriz.app.core.designsystem.theme.Blue500
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.Red800
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.feature.concept_book.R

@Composable
internal fun CategoryCard(
    categoryName: String,
    cardStyle: CategoryCardStyle,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = cardStyle.backgroundColor,
        modifier = modifier.aspectRatio(105.0f / 156.0f),
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 3.dp,
    ) {
        QrizBox {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.subject_category_left_icon),
                contentDescription = null,
                tint = cardStyle.leftIconColor,
                modifier = Modifier
                    .horizontalBias(0f)
                    .verticalBias(0.95f),
            )

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.subject_category_right_icon),
                contentDescription = null,
                tint = cardStyle.rightIconColor,
                modifier = Modifier
                    .horizontalBias(1f)
                    .verticalBias(0.7f),
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .horizontalBias(1f)
                    .padding(
                        horizontal = 16.dp,
                        vertical = 24.dp,
                    ),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = categoryName,
                    color = cardStyle.textColor,
                    style = QrizTheme.typography.headline1,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "QRIZ 개념서",
                    color = cardStyle.textColor,
                    style = QrizTheme.typography.label2.copy(fontWeight = FontWeight.Bold),
                )
            }
        }
    }
}

@Preview
@Composable
private fun CategoryCardPreview() {
    QrizTheme {
        Box(
            modifier = Modifier.width(100.dp),
        ) {
            CategoryCard(
                categoryName = "데이터 모델링의 이해",
                cardStyle = CategoryCardStyle(
                    backgroundColor = Color.White,
                    textColor = Gray800,
                    leftIconColor = Color(0xAA3A6EFE),
                    rightIconColor = Color(0xAAEF5D5D),
                ),
            )
        }
    }
}

internal data class CategoryCardStyle(
    val backgroundColor: Color,
    val textColor: Color,
    val leftIconColor: Color,
    val rightIconColor: Color,
) {
    companion object {
        val dark = CategoryCardStyle(
            backgroundColor = Gray800,
            textColor = White,
            rightIconColor = Blue100,
            leftIconColor = Blue100,
        )

        val light = CategoryCardStyle(
            backgroundColor = White,
            textColor = Gray800,
            leftIconColor = Blue500,
            rightIconColor = Red800,
        )
    }
}
