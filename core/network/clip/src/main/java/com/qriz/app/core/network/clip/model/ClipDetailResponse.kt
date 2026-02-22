package com.qriz.app.core.network.clip.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 오답노트 문제 상세 조회 응답
 *
 * 문제의 전체 정보, 선택지, 해설, 사용자의 답안 등을 포함하는 상세 정보 응답 모델
 */
@Serializable
data class ClipDetailResponse(
    @SerialName("skillName")
    val skillName: String,

    @SerialName("questionText")
    val questionText: String,

    @SerialName("questionNum")
    val questionNum: Int,

    @SerialName("description")
    val description: String,

    @SerialName("option1")
    val option1: String,

    @SerialName("option2")
    val option2: String,

    @SerialName("option3")
    val option3: String,

    @SerialName("option4")
    val option4: String,

    @SerialName("answer")
    val answer: Int,

    @SerialName("solution")
    val solution: String,

    @SerialName("checked")
    val checked: Int,

    @SerialName("correction")
    val correction: Boolean,

    @SerialName("testInfo")
    val testInfo: String,

    @SerialName("skillId")
    val skillId: Long,

    @SerialName("title")
    val title: String,

    @SerialName("keyConcepts")
    val keyConcepts: String
)
