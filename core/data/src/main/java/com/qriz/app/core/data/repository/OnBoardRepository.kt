package com.qriz.app.core.data.repository

import com.qriz.app.core.data.model.test.Test

interface OnBoardRepository {
    fun submitSurvey(concepts: List<String>)

    suspend fun getPreviewTest(): Test

    fun submitPreview(answer: Map<Long, String>)
}
