package com.qriz.app.core.data.conceptbook.conceptbook.mapper

import com.qriz.app.core.data.conceptbook.conceptbook.data.CategoryData
import com.qriz.app.core.data.conceptbook.conceptbook.data.ConceptBookData
import com.qriz.app.core.data.conceptbook.conceptbook.data.SubjectData
import com.qriz.app.core.data.conceptbook.conceptbook_api.model.Category
import com.qriz.app.core.data.conceptbook.conceptbook_api.model.ConceptBook
import com.qriz.app.core.data.conceptbook.conceptbook_api.model.Subject

internal fun SubjectData.toSubject(): Subject {
    return Subject(
        number = number,
        categories = categories.map { it.toCategory() }
    )
}

internal fun CategoryData.toCategory(): Category {
    return Category(
        name = categoryName,
        conceptBooks = concept.map { it.toConceptBook() }
    )
}

internal fun ConceptBookData.toConceptBook(): ConceptBook {
    return ConceptBook(
        name = conceptName,
        id = id,
        file = fileName
    )
}
