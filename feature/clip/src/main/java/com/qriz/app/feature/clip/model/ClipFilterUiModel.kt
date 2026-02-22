package com.qriz.app.feature.clip.model

import com.qriz.app.core.data.clip.clip_api.model.ClipFilterCategory
import com.qriz.app.core.data.clip.clip_api.model.ClipFilterConcept
import com.qriz.app.core.data.clip.clip_api.model.ClipFilterSubject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class ClipFilterSubjectUiModel(
    val number: Int,
    val categories: ImmutableList<ClipFilterCategoryUiModel>
)

data class ClipFilterCategoryUiModel(
    val name: String,
    val concepts: ImmutableList<ClipFilterConceptUiModel>
)

data class ClipFilterConceptUiModel(
    val name: String,
    val isSelected: Boolean,
    val enabled: Boolean,
)

fun ClipFilterSubject.toUiModel(selectedConcepts: List<String>): ClipFilterSubjectUiModel =
    ClipFilterSubjectUiModel(
        number = number,
        categories = categories.map { it.toUiModel(selectedConcepts) }.toImmutableList(),
    )

fun ClipFilterCategory.toUiModel(selectedConcepts: List<String>): ClipFilterCategoryUiModel =
    ClipFilterCategoryUiModel(
        name = name,
        concepts = concepts.map { it.toUiModel(selectedConcepts) }.toImmutableList(),
    )

fun ClipFilterConcept.toUiModel(selectedConcepts: List<String>): ClipFilterConceptUiModel =
    ClipFilterConceptUiModel(
        name = name,
        isSelected = name in selectedConcepts,
        enabled = enable,
    )
