package com.qriz.app.feature.concept_book.model

enum class Subject(
    val concepts: List<Category>,
) {
    FIRST(concepts = Category.FirstSubjectCategory.entries),
    SECOND(concepts = Category.SecondSubjectCategory.entries),
}
