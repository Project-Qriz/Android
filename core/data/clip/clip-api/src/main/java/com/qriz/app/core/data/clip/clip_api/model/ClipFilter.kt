package com.qriz.app.core.data.clip.clip_api.model

data class ClipFilterSubject(
    val number: Int,
    val categories: List<ClipFilterCategory>
)

data class ClipFilterCategory(
    val name: String,
    val concepts: List<ClipFilterConcept>
)

data class ClipFilterConcept(
    val name: String,
    val enable: Boolean,
)
