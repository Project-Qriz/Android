package com.qriz.app.core.data.conceptbook.conceptbook.repository

import com.qriz.app.core.data.conceptbook.conceptbook.data.SubjectData
import com.qriz.app.core.data.conceptbook.conceptbook.mapper.toCategory
import com.qriz.app.core.data.conceptbook.conceptbook.mapper.toConceptBook
import com.qriz.app.core.data.conceptbook.conceptbook.mapper.toSubject
import com.qriz.app.core.data.conceptbook.conceptbook_api.model.Category
import com.qriz.app.core.data.conceptbook.conceptbook_api.model.ConceptBook
import com.qriz.app.core.data.conceptbook.conceptbook_api.model.Subject
import com.qriz.app.core.data.conceptbook.conceptbook_api.repository.ConceptBookRepository
import javax.inject.Inject

class ConceptBookRepositoryImpl @Inject constructor() : ConceptBookRepository {
    override suspend fun getData(): List<Subject> {
        return SubjectData.entries.map { it.toSubject() }
    }

    override suspend fun getCategoryData(categoryName: String): Category {
        for (subject in SubjectData.entries) {
            val result = subject.categories.find { it.categoryName == categoryName }
            if (result != null) {
                return result.toCategory(subjectNumber = subject.number)
            }
        }

        throw IllegalArgumentException("존재하지 않는 카테고리입니다.")
    }

    override suspend fun getConceptBook(id: Long): ConceptBook {
        for (subject in SubjectData.entries) {
            for (category in subject.categories) {
                val result = category.concept.find { it.id == id }
                if (result != null) {
                    return result.toConceptBook(
                        subjectNumber = subject.number,
                        categoryName = category.categoryName,
                    )
                }
            }
        }

        throw IllegalArgumentException("존재하지 않는 개념서입니다.")
    }
}
