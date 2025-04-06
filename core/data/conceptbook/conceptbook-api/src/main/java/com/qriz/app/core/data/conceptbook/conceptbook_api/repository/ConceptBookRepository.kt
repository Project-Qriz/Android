package com.qriz.app.core.data.conceptbook.conceptbook_api.repository

import com.qriz.app.core.data.conceptbook.conceptbook_api.model.Category
import com.qriz.app.core.data.conceptbook.conceptbook_api.model.ConceptBook
import com.qriz.app.core.data.conceptbook.conceptbook_api.model.Subject

interface ConceptBookRepository {
    suspend fun getData(): List<Subject>

    suspend fun getCategoryData(categoryName: String): Category

    suspend fun getConceptBook(id: Long): ConceptBook
}
