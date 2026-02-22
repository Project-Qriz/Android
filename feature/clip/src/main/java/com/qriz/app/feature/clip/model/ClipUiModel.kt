package com.qriz.app.feature.clip.model

import androidx.compose.runtime.Immutable
import com.qriz.app.core.data.clip.clip_api.model.Clip
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Immutable
data class ClipUiModel(
    val id: Long,
    val questionNum: Int,
    val question: String,
    val correction: Boolean,
    val keyConcepts: ImmutableList<String>,
    val date: String,
)

fun Clip.toUiModel(): ClipUiModel = ClipUiModel(
    id = id,
    questionNum = questionNum,
    question = question,
    correction = correction,
    keyConcepts = keyConcepts.split(",").map { it.trim() }.toImmutableList(),
    date = date,
)