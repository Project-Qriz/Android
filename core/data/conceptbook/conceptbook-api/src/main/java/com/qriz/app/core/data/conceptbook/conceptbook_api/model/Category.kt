package com.qriz.app.core.data.conceptbook.conceptbook_api.model

data class Category(
    val name: String,
    val conceptBooks: List<ConceptBook>,
)
