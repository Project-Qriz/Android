package com.qriz.app.core.data.conceptbook.conceptbook.repository

import com.qriz.app.core.data.conceptbook.conceptbook.data.SubjectData
import com.qriz.app.core.data.conceptbook.conceptbook.mapper.toSubject
import com.qriz.app.core.data.conceptbook.conceptbook_api.model.Subject
import com.qriz.app.core.data.conceptbook.conceptbook_api.repository.ConceptBookRepository
import javax.inject.Inject

class ConceptBookRepositoryImpl @Inject constructor() : ConceptBookRepository {
    override suspend fun getData(): List<Subject> {
        return SubjectData.entries.map { it.toSubject() }
    }
}
