package com.qriz.app.feature.mock_test.sessions.model

enum class Filter(
    val value: String
) {
    ALL("전체"),
    INCOMPLETE("학습 전"),
    COMPLETED("학습 후"),
    PAST_ORDER("과거순");
}
