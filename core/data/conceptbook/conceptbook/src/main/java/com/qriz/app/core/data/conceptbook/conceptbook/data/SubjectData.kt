package com.qriz.app.core.data.conceptbook.conceptbook.data

internal enum class SubjectData(
    val number: Int,
    val categories: List<CategoryData>,
) {
    FIRST(number = 1, categories = CategoryData.FirstSubjectCategory.entries),
    SECOND(number = 2, categories = CategoryData.SecondSubjectCategory.entries),
}
