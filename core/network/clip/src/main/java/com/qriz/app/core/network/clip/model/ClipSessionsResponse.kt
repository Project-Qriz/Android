package com.qriz.app.core.network.clip.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 오답노트 - 진행한 모의고사 세션 목록
 */
@Serializable
data class ClipSessionsResponse(
    @SerialName("sessions") val sessions: List<String>,
    @SerialName("latestSession") val latestSession: String?,
)
