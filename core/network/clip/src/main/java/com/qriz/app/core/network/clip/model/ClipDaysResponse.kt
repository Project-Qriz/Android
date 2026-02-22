package com.qriz.app.core.network.clip.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 오답노트 - 진행한 오늘의 공부 Day 목록
 */
@Serializable
data class ClipDaysResponse(
    @SerialName("days") val days: List<String>
)
