package com.qriz.app.core.data.test.test_api.model

import androidx.compose.runtime.Immutable

/**
 * [Question]에 속한 개별 선택지
 * @property description 선택지에 대한 설명
 */
@Immutable
data class Option(
    val description: String
)
