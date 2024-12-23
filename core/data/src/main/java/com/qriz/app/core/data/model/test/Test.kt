package com.qriz.app.core.data.model.test

/**
 * 전체 테스트 내용을 담고 있는 모델
 * @property questions 문제 목록
 * @property totalTimeLimit 테스트 시간 제한
 */
data class Test(
    val questions: List<Question>,
    val totalTimeLimit: Int,
)