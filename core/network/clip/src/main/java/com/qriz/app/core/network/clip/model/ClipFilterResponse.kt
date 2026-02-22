package com.qriz.app.core.network.clip.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 오답노트 목록 조회 응답 아이템
 *
 * 사용자가 틀린 문제들의 간략한 정보를 담고 있는 응답 모델
 */
@Serializable
data class ClipFilterResponse(
    @SerialName("id")
    val id: Long,

    @SerialName("questionNum")
    val questionNum: Int,

    @SerialName("question")
    val question: String,

    @SerialName("correction")
    val correction: Boolean,

    @SerialName("keyConcepts")
    val keyConcepts: String,

    @SerialName("date")
    val date: String
)
