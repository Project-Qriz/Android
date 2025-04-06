package com.qriz.app.core.data.conceptbook.conceptbook_api.model

data class ConceptBook(
    val id: Long,
    val name: String,
    val file: String,
    val subjectNumber: Int,
    val categoryName: String,
)
