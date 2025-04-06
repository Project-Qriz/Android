package com.qriz.app.feature.concept_book.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.data.conceptbook.conceptbook_api.model.Category
import com.qriz.app.core.data.conceptbook.conceptbook_api.model.Subject
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.feature.concept_book.R

@Composable
internal fun SubjectItem(
    subject: Subject,
    cardStyle: CategoryCardStyle,
    rowCount: Int,
    onClickCategory: (String) -> Unit,
) {
    Column {
        Text(
            text = stringResource(id = R.string.subject, subject.number),
            style = QrizTheme.typography.heading2,
            modifier = Modifier.padding(bottom = 12.dp),
        )
        SubjectCategoryRow(
            subject = subject,
            cardStyle = cardStyle,
            rowCount = rowCount,
            onClickCategory = onClickCategory,
        )
    }
}

@Preview
@Composable
private fun SubjectItemPreview() {
    QrizTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            SubjectItem(
                subject = Subject(
                    number = 1,
                    categories = listOf(
                        Category(
                            name = "카테고리1",
                            conceptBooks = emptyList(),
                            subjectNumber = 1,
                        ),
                        Category(
                            name = "카테고리2",
                            conceptBooks = emptyList(),
                            subjectNumber = 2,
                        ),
                        Category(
                            name = "카테고리3",
                            conceptBooks = emptyList(),
                            subjectNumber = 3,
                        ),
                    ),
                ),
                cardStyle = CategoryCardStyle.light,
                rowCount = 3,
                onClickCategory = {}
            )
            SubjectItem(
                subject = Subject(
                    number = 2,
                    categories = listOf(
                        Category(
                            name = "카테고리1",
                            conceptBooks = emptyList(),
                            subjectNumber = 1,
                        ),
                        Category(
                            name = "카테고리2",
                            conceptBooks = emptyList(),
                            subjectNumber = 2,
                        ),
                    ),
                ),
                cardStyle = CategoryCardStyle.dark,
                rowCount = 3,
                onClickCategory = {}
            )
        }
    }
}
