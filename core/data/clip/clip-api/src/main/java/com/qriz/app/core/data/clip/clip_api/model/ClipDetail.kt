package com.qriz.app.core.data.clip.clip_api.model

/**
 * 오답노트 문제 상세 정보
 *
 * @property skillName 스킬명
 * @property questionText 문제 내용
 * @property questionNum 문제 번호
 * @property description 문제 설명
 * @property option1 선택지 1
 * @property option2 선택지 2
 * @property option3 선택지 3
 * @property option4 선택지 4
 * @property answer 정답 번호
 * @property solution 해설
 * @property checked 사용자가 선택한 답
 * @property correction 정답 여부
 * @property testInfo 테스트 정보 (회차)
 * @property skillId 스킬 ID
 * @property title 과목명
 * @property keyConcepts 핵심 개념
 */
data class ClipDetail(
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
    val keyConcepts: String
)
