package com.qriz.app.feature.concept_book.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.data.conceptbook.conceptbook_api.model.Category
import com.qriz.app.core.data.conceptbook.conceptbook_api.model.Subject
import com.qriz.app.core.designsystem.theme.Gray500
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.feature.concept_book.R

@Composable
internal fun SubjectCategoryRow(
    subject: Subject,
    cardStyle: CategoryCardStyle,
    rowCount: Int,
    onClickCategory: (String) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        for(index in 0 until rowCount) {
            if (index < subject.categories.size) {
                SubjectCategoryRowItem(
                    category = subject.categories[index],
                    cardStyle = cardStyle,
                    modifier = Modifier.weight(1f),
                    onClickCategory = onClickCategory,
                )
            } else {
                Box(
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun SubjectCategoryRowItem(
    category: Category,
    cardStyle: CategoryCardStyle,
    modifier: Modifier = Modifier,
    onClickCategory: (String) -> Unit,
) {
    Column(
        modifier = modifier.clickable { onClickCategory(category.name) }
    ) {
        CategoryCard(
            categoryName = category.name,
            cardStyle = cardStyle,
        )
        Text(
            text = category.name,
            style = QrizTheme.typography.body1,
            color = Gray800,
            modifier = Modifier.padding(top = 8.dp),
        )
        Text(
            text = stringResource(id = R.string.concept_count, category.conceptBooks.size),
            style = QrizTheme.typography.label2,
            color = Gray500,
        )
    }
}

@Preview
@Composable
private fun SubjectCategoryRowPreview() {
    QrizTheme {
        SubjectCategoryRow(
            subject = Subject(
                number = 1,
                categories = listOf(
                    Category(
                        name = "데이터 모델링의 이해",
                        conceptBooks = emptyList(),
                        subjectNumber = 1,
                    ),
                    Category(
                        name = "데이터 모델링의 이해",
                        conceptBooks = emptyList(),
                        subjectNumber = 2,
                    ),
                ),
            ),
            cardStyle = CategoryCardStyle(
                backgroundColor = White,
                leftIconColor = Color(0xAA3A6EFE),
                rightIconColor = Color(0xAAEF5D5D),
                textColor = Gray800,
            ),
            rowCount = 3,
            onClickCategory = {}
        )
    }
}
