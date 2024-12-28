package com.qriz.app.core.data.onboard.onboard_api.repository

import com.qriz.app.core.data.onboard.onboard_api.model.Test

interface OnBoardRepository {
    fun submitSurvey(concepts: List<String>)

    suspend fun getPreviewTest(): Test

    fun submitPreview(answer: Map<Long, String>)
}