package com.qriz.app.feature.clip.model

import androidx.compose.runtime.Immutable
import com.qriz.app.core.data.clip.clip_api.model.ClipDetail
import com.qriz.app.core.data.conceptbook.conceptbook_api.model.Subject

@Immutable
data class ClipDetailUiModel(
    val skillName: String,
    val questionText: String,
    val questionNum: Int,
    val description: String?,
    val option1: String,
    val option2: String,
    val option3: String,
    val option4: String,
    val answer: Int,
    val solution: String,
    val checked: Int,
    val correction: Boolean,
    val testInfo: String,
    val skillId: Long,
    val title: String,
    val keyConcepts: String,
    val subjectNumber: Int,
)

fun ClipDetail.toDetailUiModel(subjects: List<Subject>): ClipDetailUiModel {
    val subjectNumber = subjects
        .firstOrNull { subject ->
            subject.categories.any { category ->
                category.conceptBooks.any { it.name == keyConcepts }
            }
        }
        ?.number ?: 0

    return ClipDetailUiModel(
        skillName = skillName,
        questionText = questionText,
        questionNum = questionNum,
        description = description,
        option1 = option1,
        option2 = option2,
        option3 = option3,
        option4 = option4,
        answer = answer,
        solution = solution,
        checked = checked,
        correction = correction,
        testInfo = testInfo,
        skillId = skillId,
        title = title,
        keyConcepts = keyConcepts,
        subjectNumber = subjectNumber,
    )
}
