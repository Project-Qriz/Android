package com.qriz.app.core.data.conceptbook.conceptbook_api.repository

import com.qriz.app.core.data.conceptbook.conceptbook_api.model.Subject

interface ConceptBookRepository {
    suspend fun getData(): List<Subject>
}
