package com.qriz.app.core.data.onboard.onboard_api.model

/**
 * [Test]에 속한 개별 문제
 * @property id 문제의 고유 식별자
 * @property question 문제의 텍스트
 * @property options 문제에 대한 선택지[Option] 목록
 * @property timeLimit 해당 문제의 시간 제한 (단위: 초)
 */
data class Question(
    val id: Long,
    val question: String,
    val options: List<Option>,
    val timeLimit: Int,
)
