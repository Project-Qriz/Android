package com.qriz.app.core.data.clip.clip_api.model

/**
 * 오답노트 문제 정보
 *
 * @property id 클립 ID
 * @property questionNum 문제 번호
 * @property question 문제 내용
 * @property correction 정답 여부
 * @property keyConcepts 핵심 개념
 * @property date 등록 날짜
 */
data class Clip(
    val id: Long,
    val questionNum: Int,
    val question: String,
    val correction: Boolean,
    val keyConcepts: String,
    val date: String
)
